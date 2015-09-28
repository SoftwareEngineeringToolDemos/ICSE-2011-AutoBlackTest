/*
 * Created on Sep 14, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.report;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlHelper;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlPage;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.Formatter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import ca.digitalcave.moss.common.DateUtil;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDocumentFrame;

public class NetWorthOverTime extends BuddiReportPlugin {

	public static final long serialVersionUID = 0;

	@Override
	public HtmlPage getReport(ImmutableDocument model, MossDocumentFrame frame, Date startDate, Date endDate) {
		final int NUM_SAMPLES = 12;

		DefaultCategoryDataset barData = new DefaultCategoryDataset();

		List<Date> dates = new LinkedList<Date>();
		Date date = startDate;

		int numberOfDaysBetween = DateUtil.getDaysBetween(startDate, new Date(), false);
		int daysBetweenReport = numberOfDaysBetween / NUM_SAMPLES;

		for (int i = 0; i < NUM_SAMPLES; i++){
			date = DateUtil.addDays(startDate, i * daysBetweenReport);
			if (date.before(new Date()))
				dates.add(date);
			Logger.getLogger(this.getClass().getName()).finest("Added date: " + date);			
		}

		dates.add(new Date());

		for (Date d : dates) {
			long netWorth = model.getNetWorth(d);
			
//			barData.addValue((Number) new Double(accounts.get(a) / 100.0), TextFormatter.getFormattedDate(d), a.getName());
			barData.addValue((Number) new Double(netWorth / 100.0), 
					TextFormatter.getTranslation(BuddiKeys.NET_WORTH), 
					(Formatter.getDateFormat("MM/dd").format(d)));
		}



		JFreeChart chart = ChartFactory.createLineChart(
				"", //Title
				"", //Domain axis label
				"", //Range axis label
				barData,             // data
				PlotOrientation.VERTICAL,
				true,               // include legend
				true,
				false
		);

		chart.setBackgroundPaint(Color.WHITE);
		chart.setBorderStroke(new BasicStroke(0));
		chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
		chart.getCategoryPlot().setDomainGridlinePaint(Color.BLACK);
		chart.getCategoryPlot().setRangeGridlinePaint(Color.BLACK);

		StringBuilder sb = HtmlHelper.getHtmlHeader(
				TextFormatter.getTranslation(BuddiKeys.GRAPH_TITLE_NET_WORTH_OVER_TIME), 
				null, 
				startDate, 
				new Date());

		sb.append("<img class='center_img' src='graph.png' />");
		sb.append(HtmlHelper.getHtmlFooter());
		
		Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
		images.put("graph.png", 
				chart.createBufferedImage(
						Math.min(Toolkit.getDefaultToolkit().getScreenSize().width - 200, 1000),
						Toolkit.getDefaultToolkit().getScreenSize().height - 100));

		return new HtmlPage(sb.toString(), images);
	}

	public String getName() {
		return BuddiKeys.GRAPH_TITLE_NET_WORTH_OVER_TIME.toString();
	}

	@Override
	public PluginReportDateRangeChoices getDateRangeChoice() {
		return PluginReportDateRangeChoices.START_ONLY;
	}

	public String getDescription() {
		return BuddiKeys.NETWORTH_LINE_GRAPH.toString();
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
