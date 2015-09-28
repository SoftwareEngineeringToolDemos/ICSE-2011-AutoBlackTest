/*
 * Created on Sep 14, 2006 by wyatt
 * 
 * The interface which can be extended to create export files
 */
package org.homeunix.thecave.buddi.plugin.api;

import java.io.File;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginMessage;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.MutableDocument;

import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.swing.MossDocumentFrame;

/**
 * The abstract class to extend when creating an export plugin.  The method exportData() 
 * is the one which is called by Buddi when executing the plugin.  In this method,
 * you have access to the main document object, the frame from which the plugin was
 * called, and the file to export to. 
 * 
 * @author wyatt
 *
 */
public abstract class BuddiExportPlugin extends MenuPlugin implements MossPlugin, FileAccess {

	/**
	 * Exports data as required.  The plugin launch code will prompt for a file
	 * and pass it in (unless you override the isPromptForFile() method to return false).
	 * 
	 * This method is executed from the launch code within a SwingWorker thread.  Once this
	 * method is completed, the launch code will update all windows with any new information
	 * which may have come from this plugin.  Thus, it is not a good idea to use any threads
	 * in this method, unless you can guarantee that this method will not complete until 
	 * all data processing is completed.
	 * 
	 * As well, since this method is not run from the AWT Event thread, you should not display
	 * any Swing objects here, as it can potentially cause deadlocks in some situations.  This
	 * should not be a problem for most plugins, as they just need a file (provided), and that is
	 * all.  If you do need to display some Swing objects, you can do so using the 
	 * SwingUtilities.invokeLater() method.
	 */
	public abstract void exportData(ImmutableDocument model, MossDocumentFrame callingFrame, File file) throws PluginException, PluginMessage;
	
	@Override
	public void processData(MutableDocument model, MossDocumentFrame callingFrame, File file) throws PluginException, PluginMessage {
		exportData(model, callingFrame, file);
	}
	
	public String getDescription() {
		return BuddiKeys.FILE_DESCRIPTION_BUDDI_EXPORT_FILES.toString();
	}
	
	public String getProcessingMessage() {
		return BuddiKeys.MESSAGE_EXPORTING_FILE.toString();
	}
	
	@Override
	public boolean isFileChooserSave() {
		return true;
	}
}
