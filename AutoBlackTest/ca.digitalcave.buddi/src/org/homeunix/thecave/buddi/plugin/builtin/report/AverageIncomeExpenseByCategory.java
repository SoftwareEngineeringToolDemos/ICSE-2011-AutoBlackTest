/*
 * Created on Sep 14, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.report;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
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

import ca.digitalcave.moss.common.DateUtil;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDocumentFrame;

/**
 * Built-in plugin.  Feel free to use this as an example on how to make
 * report plugins
 * 
 * @author wyatt
 *
 */
public class AverageIncomeExpenseByCategory extends BuddiReportPlugin {
	
	public static final long serialVersionUID = 0;
	
	public Version getMaximumVersion() {
		return null; //This is integrated, so there is no need to pick this.
	}

	public Version getMinimumVersion() {
		return null;
	}

	public boolean isPluginActive() {
		return true;
	}

	@Override
	public HtmlPage getReport(ImmutableDocument model, MossDocumentFrame callingFrame, Date startDate, Date endDate) {
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
		sb.append(TextFormatter.getTranslation(BuddiKeys.AVERAGE));
		sb.append(" / ");
		sb.append(TextFormatter.getTranslation(model.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH).getName()));
		sb.append("</th></tr>\n");
		
		long totalActual = 0, totalAverage = 0;
		
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
                        //////the following code has been modified for the average calculation bug:
			long average;
                       
                       if(isQuarter(startDate, endDate,model.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH).getDaysInPeriod(endDate)))
                           average = (long) ((double) actual / 3.0);
                       else 
			average = (long) ((double) actual / (double) DateUtil.getDaysBetween(startDate, endDate, true) * 
				model.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH).getDaysInPeriod(endDate));
                        //////////////////
			if (c.isIncome()){
				totalAverage += average;
			}
			else {
				totalAverage -= average;
			}
			

			if (transactions.size() > 0){				
				sb.append("<tr>");
				sb.append("<td>");
				sb.append(TextFormatter.getTranslation(c.toString()));
				sb.append("</td><td class='right" + (TextFormatter.isRed(c, actual) ? " red'" : "'") + ">");
				sb.append(TextFormatter.getFormattedCurrency(actual));
				sb.append("</td><td class='right" + (TextFormatter.isRed(c, average) ? " red'" : "'") + "'>");
				sb.append(TextFormatter.getFormattedCurrency(average));				
				sb.append("</td></tr>\n");
			}
		}
		
		sb.append("<tr><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.TOTAL));
		sb.append("</th><th class='right" + (TextFormatter.isRed(totalActual) ? " red'" : "'") + "'>");
		sb.append(TextFormatter.getFormattedCurrency(totalActual));
		sb.append("</th><th class='right" + (TextFormatter.isRed(totalAverage) ? " red'" : "'") + "'>");
		sb.append(TextFormatter.getFormattedCurrency(totalAverage));
		sb.append("</th></tr>\n");

		sb.append("</table>\n\n");
		
		sb.append("<hr>\n");
		
		sb.append("<h1>").append(TextFormatter.getTranslation(BuddiKeys.REPORT_DETAILS)).append("</h1>\n");
		
		for (ImmutableBudgetCategory bc : categories){
			List<ImmutableTransaction> transactions = model.getImmutableTransactions(bc, startDate, endDate);
			
			
			if (transactions.size() > 0){
				sb.append(bc.isIncome() ? "<h2>" : "<h2 class='red'>");
				sb.append(TextFormatter.getTranslation(bc.toString()));
				sb.append("</h2>\n");

				sb.append(HtmlHelper.getHtmlTransactionHeader());


				for (ImmutableTransaction t : transactions) {
					if (!t.isDeleted())
						sb.append(HtmlHelper.getHtmlTransactionRow(t, bc));
				}

				sb.append(HtmlHelper.getHtmlTransactionFooter());
			}
		}
		
		sb.append(HtmlHelper.getHtmlFooter());
	
		return new HtmlPage(sb.toString(), null);
	}
	
	public String getName() {
		return TextFormatter.getTranslation(BuddiKeys.REPORT_TITLE_AVERAGE_INCOME_AND_EXPENSES_BY_CATEGORY);
	}

	public String getDescription() {
		return BuddiKeys.REPORT_DESCRIPTION_AVERAGE_INCOME_AND_EXPENSES_BY_CATEGORY.toString();
	}
	
	@Override
	public PluginReportDateRangeChoices getDateRangeChoice() {
		return PluginReportDateRangeChoices.INTERVAL;
	}
        
        ////////////////////////////////////////////////////The following code has been added to fix the average calculations bug
        public boolean isQuarter(Date startDate, Date endDate , long days)
        {
              if(!DateUtil.isSameYear(startDate, endDate))
                  return false;
              if(DateUtil.getDay(startDate) != 1)
                  return false;
              if(DateUtil.getDay(endDate) != days)
                  return false;
              if(DateUtil.getMonthsBetween(startDate, endDate, false) != 2)
                  return false;
              return true;
        }
        ////////////////////////////////////////////////////
}
