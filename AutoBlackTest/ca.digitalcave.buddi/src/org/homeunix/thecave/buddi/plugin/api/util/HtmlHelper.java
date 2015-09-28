/*
 * Created on Oct 7, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSplit;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransactionSplit;

import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.common.StreamUtil;

public class HtmlHelper {


	/**
	 * Get a StringBuilder with an HTML header, including some CSS for a 
	 * basic printable page. 
	 * @param title The main title - appears on the browser title, as well as on the top of the page
	 * @param subtitle The subtitle - optional - appears right below main title.  Set to null to not use.
	 * @param startDate Start date.  Set to null if this is not a report with date range.
	 * @param endDate End date.  Set to null if this is not a report with date range. 
	 * @return
	 */
	public static StringBuilder getHtmlHeader(String title, String subtitle, Date startDate, Date endDate){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"); 
		sb.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append("<html>\n");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
		sb.append("<head>\n");
		sb.append("<title>");
		sb.append(TextFormatter.getTranslation(title));
		sb.append("</title>\n");

		//Screen CSS
		sb.append("<style type='text/css' media='screen'>\n");
		includeCss("screen.css", sb);
		sb.append("</style>\n");

		//Printing CSS
		sb.append("<style type='text/css' media='print'>\n");
		includeCss("print.css", sb);
		sb.append("</style>\n");

//		sb.append("<SCRIPT LANGUAGE=\"JavaScript\">\n");
//		sb.append("<!--\n");
//		sb.append("function launchTransactions(link) {\n");
//		sb.append("window.open(link, \"\", \"height=1,width=1\");\n");
//		sb.append("return false;\n");
//		sb.append("}\n");
//		sb.append("//-->\n");
//		sb.append("</SCRIPT>\n");

		sb.append("</head>\n");
		if ("Hebrew".equals(PrefsModel.getInstance().getLanguage()))
			sb.append("<body dir='rtl'>\n");
		else
			sb.append("<body>\n");
		sb.append("<div class='separator'></div>\n");

		sb.append("<div class='header'>\n<div class='empty'>&nbsp;</div>");
		sb.append("<h1>").append(TextFormatter.getTranslation(title)).append("</h1>\n");

		if (subtitle != null){
			sb.append("<h2>").append(TextFormatter.getTranslation(subtitle)).append("</h2>\n");
		}

		if (startDate != null && endDate != null){
			sb.append("<h2>"); 
			sb.append(TextFormatter.getDateFormat().format(startDate));
			sb.append(" - ");
			sb.append(TextFormatter.getDateFormat().format(endDate));
			sb.append("</h2>\n");
		}
		else if (startDate != null){
			sb.append("<h2>"); 
			sb.append(TextFormatter.getDateFormat().format(startDate));
			sb.append("</h2>\n");			
		}
		else if (endDate != null){
			sb.append("<h2>"); 
			sb.append(TextFormatter.getDateFormat().format(endDate));
			sb.append("</h2>\n");	
		}

		sb.append("</div>\n<div class='separator'></div>\n<div class='content'>\n\n");

		return sb;
	}

	/**
	 * Get the HTML footer, matched to the header supplied from getHtmlHeader()
	 * @return
	 */
	public static StringBuilder getHtmlFooter(){
		StringBuilder sb = new StringBuilder();

		sb.append("</div>\n</body>\n</html>");

		return sb;
	}

	/**
	 * Returns an HTML table row consisting of information from the given transaction. 
	 * @param t Transaction to display.
	 * @param source Associated source.  This would be the account which 
	 * the transaction frame is associated with, for instance.  This can be null
	 * if there is none.
	 * @return
	 */
	public static StringBuilder getHtmlTransactionRow(ImmutableTransaction t, ImmutableSource source){
		StringBuilder sb = new StringBuilder();

		sb.append("<tr><td width='15%'>");
		sb.append(TextFormatter.getDateFormat().format(t.getDate()));

		sb.append("</td><td width='20%'>");
		sb.append(TextFormatter.getTranslation(t.getDescription()));

		sb.append("</td><td width='30%'>");
		if (t.getFrom() instanceof ImmutableSplit)
			//We assume that if there is an immutable split, then the given source is the from / to source.  Is this right?
			sb.append(TextFormatter.getTranslation(source + ""));
		else 
			sb.append(TextFormatter.getTranslation(t.getFrom().toString()));
		
		sb.append(TextFormatter.getTranslation(BuddiKeys.HTML_TO));
		
		if (t.getTo() instanceof ImmutableSplit)
			//We assume that if there is an immutable split, then the given source is the from / to source.  Is this right?
			sb.append(TextFormatter.getTranslation(source + ""));
		else 
			sb.append(TextFormatter.getTranslation(t.getTo().toString()));
			

		boolean red;
		if (source != null)
			red = TextFormatter.isRed(t, t.getFrom().equals(source));
		else 
			red = TextFormatter.isRed(t);

		sb.append("</td><td width='15%' class='right" + (red ? " red'" : "'") + "'>");
		if (t.getFrom().equals(source) || t.getTo().equals(source))
			sb.append(TextFormatter.getFormattedCurrency(t.getAmount()));
		else {
			long amount = 0;
			for (ImmutableTransactionSplit split : t.getImmutableFromSplits()) {
				if (split.getSource().equals(source)){
					amount += split.getAmount();
				}
			}
			for (ImmutableTransactionSplit split : t.getImmutableToSplits()) {
				if (split.getSource().equals(source)){
					amount += split.getAmount();
				}
			}
			sb.append(TextFormatter.getFormattedCurrency(amount));
		}
		sb.append("</td><td width='20%'>");
		sb.append(t.getMemo());
		sb.append("</td></tr>\n");

		return sb;		
	}

	/**
	 * Returns the start of a table for displaying transactions,
	 * including the header row.
	 * @return
	 */
	public static StringBuilder getHtmlTransactionHeader(){
		StringBuilder sb = new StringBuilder();

		sb.append("<table class='main'>\n");
		sb.append("<tr><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.DATE));
		sb.append("</th><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.DESCRIPTION));
		sb.append("</th><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.SOURCE_TO_FROM));
		sb.append("</th><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.AMOUNT));
		sb.append("</th><th>");
		sb.append(TextFormatter.getTranslation(BuddiKeys.MEMO));
		sb.append("</th></tr>\n");

		return sb;
	}

	/**
	 * Returns the end of a table for displaying transactions.
	 * @return
	 */
	public static StringBuilder getHtmlTransactionFooter(){
		return new StringBuilder("</table>\n\n");
	}
	
	private static void includeCss(String cssName, StringBuilder sb){
		InputStream is = HtmlHelper.class.getResourceAsStream("/css/" + cssName);
		File css = OperatingSystemUtil.getUserFile("Buddi", cssName);
		if (css.exists()){
			try {
				FileInputStream cssStream = new FileInputStream(css);
				is = cssStream;
			}
			catch (FileNotFoundException fnfe){
				Logger.getLogger(HtmlHelper.class.getName()).warning("Error opening css file " + css.getAbsolutePath() + "; using default css for report.");
			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			StreamUtil.copyStream(is, baos);
			sb.append(baos.toString());
		}
		catch (IOException ioe){
			Logger.getLogger(HtmlHelper.class.getName()).warning("Error reading CSS input stream; you are probably not running this from an official Buddi release bundle.  I am therefore not using CSS for the report.");
		}
	}
}
