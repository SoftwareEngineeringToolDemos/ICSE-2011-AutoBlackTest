/*
 * Created on Sep 14, 2006 by wyatt
 * 
 * The interface which can be extended to create custom reports.
 */
package org.homeunix.thecave.buddi.plugin.api;

import java.util.Date;

import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlPage;

import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDocumentFrame;


/**
 * This class is the defintion of a Buddi report, which writes out HTML to disk and opens
 * it with a browser.  The main method which needs to be implemented is the getReport()
 * method.  This method gives you the document object, the frame which called this plugin,
 * and the date range on which to report.
 *  
 * @author wyatt
 *
 */
public abstract class BuddiReportPlugin extends PreferenceAccess implements MossPlugin {
	/**
	 * Returns an in-memory version of the printed page, as an HTML
	 * file.  An HtmlPage object is just a small class containing a string
	 * with the HTML text in it, and a map of String to BufferedImage
	 * containing all the images referenced in the HTML, by name.
	 * 
	 * See HtmlPage for more information on what is needed. 
	 * @param startDate Start of report period
	 * @param endDate End of report period
	 * @return
	 */
	public abstract HtmlPage getReport(ImmutableDocument model, MossDocumentFrame callingFrame, Date startDate, Date endDate) throws PluginException;
	
	/**
	 * This method will be called on the EventDispatch thread, before the 
	 * getReport() method is run.  Since this method is run on the 
	 * EventDispatch thread, and is the place to put any graphical forms or
	 * other prompts for the user.  For instance, if you wish to show a 
	 * window in which the user can pick options, put it here.
	 * 
	 * This method should return true if the plugin is to continue; false
	 * if the user has canceled the plugin exection.
	 * 
	 * The default instance of the plugin returns true, without showing anything.
	 * 
	 * @return
	 */
	public boolean getReportGUI(ImmutableDocument model, MossDocumentFrame callingFrame, Date startDate, Date endDate){
		return true;
	}
	
	/**
	 * The type of plugin this should be.  Can choose between one of the following enum
	 * values:
	 * 
	 * 	INTERVAL,
	 *  START_ONLY,
	 *  END_ONLY
	 *  
	 * @return A constant enum value which determines how the plugin loader should display this plugin.
	 */
	public abstract PluginReportDateRangeChoices getDateRangeChoice();
	
	public Version getMaximumVersion() {
		return null;
	}

	public Version getMinimumVersion() {
		return null;
	}
}
