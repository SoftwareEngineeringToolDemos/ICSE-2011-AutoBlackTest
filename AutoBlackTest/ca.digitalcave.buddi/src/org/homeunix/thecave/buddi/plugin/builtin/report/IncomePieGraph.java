/*
 * Created on Sep 14, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.report;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDocumentFrame;

public class IncomePieGraph extends BuddiReportPlugin {
	
	public static final long serialVersionUID = 0;
	
	@Override
	public HtmlPage getReport(ImmutableDocument model, MossDocumentFrame frame, Date startDate, Date endDate) {
		DefaultPieDataset pieData = new DefaultPieDataset();
		
		final Map<ImmutableBudgetCategory, Long> categories = getIncomeBetween(model, startDate, endDate);
		
		List<ImmutableBudgetCategory> cats = new LinkedList<ImmutableBudgetCategory>(categories.keySet());
		Collections.sort(cats);
		
		long totalIncome = 0;
		
		for (ImmutableBudgetCategory c : cats) {
			totalIncome += categories.get(c);
		}
		
		Collections.sort(cats, new Comparator<ImmutableBudgetCategory>(){
			public int compare(ImmutableBudgetCategory o1,
					ImmutableBudgetCategory o2) {
				return categories.get(o1).compareTo(categories.get(o2));
			}
		});
		
		for (ImmutableBudgetCategory c : cats) {
			if (categories.get(c) > 0)
				pieData.setValue(TextFormatter.getTranslation(c.toString()) + " (" + ((10000 * categories.get(c)) / totalIncome) / 100.0 + "%)", (double) categories.get(c));
		}
		
				
		JFreeChart chart = ChartFactory.createPieChart(
				"",
				pieData,             // data
				true,               // include legend
				true,
				false
		);
		
		chart.setBackgroundPaint(Color.WHITE);
		chart.setBorderStroke(new BasicStroke(0));
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		chart.getPlot().setOutlinePaint(Color.WHITE);

		((PiePlot) chart.getPlot()).setLabelGenerator(new BuddiPieSectionLabelGenerator());
				
		StringBuilder sb = HtmlHelper.getHtmlHeader(
				TextFormatter.getTranslation(BuddiKeys.GRAPH_TITLE_INCOME_PIE_GRAPH), 
				TextFormatter.getFormattedCurrency(totalIncome), 
				startDate, 
				endDate);

		sb.append("<img class='center_img' src='graph.png' />");
		sb.append(HtmlHelper.getHtmlFooter());
		
		Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
		images.put("graph.png", 
				chart.createBufferedImage(
						Math.min(Toolkit.getDefaultToolkit().getScreenSize().width - 200, 1000), 
						Toolkit.getDefaultToolkit().getScreenSize().height - 100));
		
		return new HtmlPage(sb.toString(), images);
	}
		
	private Map<ImmutableBudgetCategory, Long> getIncomeBetween(ImmutableDocument model, Date startDate, Date endDate){
		List<ImmutableTransaction> transactions = model.getImmutableTransactions(startDate, endDate);
		Map<ImmutableBudgetCategory, Long> categories = new HashMap<ImmutableBudgetCategory, Long>();
		
		//This map is where we store the totals for this time period.
		for (ImmutableBudgetCategory category : model.getImmutableBudgetCategories()) {
			if (category.isIncome())
				categories.put(category, Long.valueOf(0));
		}
		
		for (ImmutableTransaction transaction : transactions) {
			if (!transaction.isDeleted()){
				//Sum up the amounts for each category.
				if (transaction.getFrom() instanceof ImmutableBudgetCategory){
					ImmutableBudgetCategory c = (ImmutableBudgetCategory) transaction.getFrom();
					if (c.isIncome()){
						Long l = categories.get(c);
						l += transaction.getAmount();
						categories.put(c, l);
					}
				}
				else if (transaction.getTo() instanceof ImmutableBudgetCategory){
					ImmutableBudgetCategory c = (ImmutableBudgetCategory) transaction.getTo();
					if (c.isIncome()){
						Long l = categories.get(c);
						l += transaction.getAmount();
						categories.put(c, l);
					}
				}
				
				//Check for splits as well
				if (transaction.getTo() instanceof ImmutableSplit){
					for (ImmutableTransactionSplit split : transaction.getImmutableToSplits()) {
						if (split.getSource() instanceof ImmutableBudgetCategory){
							ImmutableBudgetCategory c = (ImmutableBudgetCategory) split.getSource();
							if (c.isIncome()){
								Long l = categories.get(c);
								l += split.getAmount();
								categories.put(c, l);
							}
						}
					}
				}
				if (transaction.getFrom() instanceof ImmutableSplit){
					for (ImmutableTransactionSplit split : transaction.getImmutableFromSplits()) {
						if (split.getSource() instanceof ImmutableBudgetCategory){
							ImmutableBudgetCategory c = (ImmutableBudgetCategory) split.getSource();
							if (c.isIncome()){
								Long l = categories.get(c);
								l += split.getAmount();
								categories.put(c, l);
							}
						}
					}
				}
			}
		}
				
		return categories;
	}

	@Override
	public PluginReportDateRangeChoices getDateRangeChoice() {
		return PluginReportDateRangeChoices.INTERVAL;
	}
	
	public String getDescription() {
		return BuddiKeys.GRAPH_DESCRIPTION_INCOME_PIE_GRAPH.toString();
	}

	public String getName() {
		return BuddiKeys.GRAPH_TITLE_INCOME_PIE_GRAPH.toString();
	}

	public boolean isPluginActive() {
		return true;
	}

	public Version getMaximumVersion() {
		return null;
	}
	
	public Version getMinimumVersion() {
		return null;
	}
}
