/*
 * Created on Sep 14, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSplit;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransactionSplit;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlHelper;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlPage;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.swing.MossDocumentFrame;

/**
 * Built-in plugin.  Feel free to use this as an example on how to make
 * report plugins (although this one is kind of ugly, so you may not 
 * want to use it..)
 * 
 * @author wyatt
 *
 */
public class IncomeExpenseReportByCategory extends BuddiReportPlugin {
	
	public static final long serialVersionUID = 0;
	
	
	@Override
	public HtmlPage getReport(ImmutableDocument model, MossDocumentFrame frame, Date startDate, Date endDate) {
		StringBuilder sb = HtmlHelper.getHtmlHeader(getName(), null, startDate, endDate);

		List<ImmutableBudgetCategory> categories = model.getImmutableBudgetCategories();
		Collections.sort(categories, new Comparator<ImmutableBudgetCategory>(){
			public int compare(ImmutableBudgetCategory o1, ImmutableBudgetCategory o2) {
				//First we sort by income
				if (o1.isIncome() != o2.isIncome()){
					if (o1.isIncome()){
						return -1;
					}
					else {
						return 1;
					}
				}
								
				//Finally, we sort by Category Name
				return o1.toString().compareTo(o2.toString());
			}
		});
		
		sb.append("<h1>").append(TextFormatter.getTranslation(BuddiKeys.REPORT_SUMMARY)).append("</h1>\n");
		sb.append("<table class='main'>\n");
		
		sb.append("<tr><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.NAME));
		sb.append("</th><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.ACTUAL));
		sb.append("</th><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.BUDGETED));
		sb.append("</th><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.DIFFERENCE));
		sb.append("</th></tr>\n");
		
		long totalActual = 0, totalBudgeted = 0;
		
		for (ImmutableBudgetCategory c : categories){
			List<ImmutableTransaction> transactions = model.getImmutableTransactions(c, startDate, endDate);
			long actual = 0;
			for (ImmutableTransaction transaction : transactions) {
				if (!transaction.isDeleted()){
					
					//Figure out the actual amounts
					if (transaction.getFrom().equals(c) || transaction.getTo().equals(c)){
						actual += transaction.getAmount();						
					}
					
					for (ImmutableTransactionSplit split : transaction.getImmutableToSplits()) {
						if (split.getSource().equals(c)){
							actual += split.getAmount();
						}
					}
					for (ImmutableTransactionSplit split : transaction.getImmutableFromSplits()) {
						if (split.getSource().equals(c)){
							actual -= split.getAmount();
						}
					}

					//Add to total for non-split transactions
					if (transaction.getTo() instanceof ImmutableBudgetCategory){
						totalActual -= transaction.getAmount();
					}
					else if (transaction.getFrom() instanceof ImmutableBudgetCategory){
						totalActual += transaction.getAmount();
					}

					//Add to total for split transactions
					if (transaction.getTo() instanceof ImmutableSplit){
						for (ImmutableTransactionSplit split : transaction.getImmutableToSplits()) {
							if (split.getSource().equals(c)){
								totalActual -= split.getAmount();
							}
						}
					}
					if (transaction.getFrom() instanceof ImmutableSplit){
						for (ImmutableTransactionSplit split : transaction.getImmutableFromSplits()) {
							if (split.getSource().equals(c)){
								totalActual += split.getAmount();
							}
						}
					}
				}
			}
			
			long budgeted = c.getAmount(startDate, endDate);
			if (c.isIncome()){
				totalBudgeted += budgeted;
			}
			else {
				totalBudgeted -= budgeted;
			}
			

			if (budgeted != 0 || transactions.size() > 0){				
				sb.append("<tr>");
				sb.append("<td>");
				sb.append(TextFormatter.getTranslation(c.toString()));
				sb.append("</td><td class='right" + (TextFormatter.isRed(c, actual) ? " red'>" : "'>"));
				sb.append(TextFormatter.getFormattedCurrency(actual));
				sb.append("</td><td class='right" + (TextFormatter.isRed(c, budgeted) ? " red'>" : "'>"));
				sb.append(TextFormatter.getFormattedCurrency(budgeted));				
				long difference = actual - budgeted;
				sb.append("</td><td class='right" + (difference > 0 ^ c.isIncome() ? " red'>" : "'>"));
				sb.append(TextFormatter.getFormattedCurrency(difference, false, difference < 0));				
				sb.append("</td></tr>\n");
			}
		}
		
		sb.append("<tr><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.TOTAL));
		sb.append("</th><th class='right" + (totalActual < 0 ? " red'>" : "'>"));
		sb.append(TextFormatter.getFormattedCurrency(totalActual));
		sb.append("</th><th class='right" + (totalBudgeted < 0 ? " red'>" : "'>"));
		sb.append(TextFormatter.getFormattedCurrency(totalBudgeted));
		long totalDifference = totalActual - totalBudgeted; 
		sb.append("</th><th class='right" + (totalDifference < 0 ? " red'>" : "'>"));
		sb.append(TextFormatter.getFormattedCurrency(totalDifference, false, totalDifference < 0));				
		sb.append("</th></tr>\n");

		sb.append("</table>\n\n");
		
		sb.append("<hr>\n");
		
		sb.append("<h1>").append(TextFormatter.getTranslation(BuddiKeys.REPORT_DETAILS)).append("</h1>\n");
		
		for (ImmutableBudgetCategory c : categories){
			List<ImmutableTransaction> transactions = model.getImmutableTransactions(c, startDate, endDate);
			
			
			if (transactions.size() > 0){
				sb.append(c.isIncome() ? "<h2>" : "<h2 class='red'>");
				sb.append(TextFormatter.getTranslation(c.toString()));
				sb.append("</h2>\n");

				sb.append(HtmlHelper.getHtmlTransactionHeader());

				for (ImmutableTransaction t : transactions) {
					if (!t.isDeleted()){
						sb.append(HtmlHelper.getHtmlTransactionRow(t, c));
					}
				}

				sb.append(HtmlHelper.getHtmlTransactionFooter());
			}
		}
		
		sb.append(HtmlHelper.getHtmlFooter());
	
		return new HtmlPage(sb.toString(), null);
	}

	public String getName() {
		return TextFormatter.getTranslation(BuddiKeys.REPORT_TITLE_INCOME_AND_EXPENSES_BY_CATEGORY);
	}

	public String getDescription() {
		return BuddiKeys.REPORT_DESCRIPTION_INCOME_EXPENSES_BY_CATEGORY.toString();
	}
	@Override
	public PluginReportDateRangeChoices getDateRangeChoice() {
		return PluginReportDateRangeChoices.INTERVAL;
	}
	
	public boolean isPluginActive() {
		return true;
	}
}
