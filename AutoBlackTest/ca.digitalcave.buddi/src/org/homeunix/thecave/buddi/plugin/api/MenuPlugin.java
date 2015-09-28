/*
 * Created on Sep 14, 2006 by wyatt
 * 
 * The interface which can be extended to create import filters
 */
package org.homeunix.thecave.buddi.plugin.api;

import java.io.File;

import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginMessage;
import org.homeunix.thecave.buddi.plugin.api.model.MutableDocument;

import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDocumentFrame;

/**
 * The abstract class to extend when creating an import plugin.  The method importData() 
 * is the one which is called by Buddi when executing the plugin.  In this method,
 * you have access to the main document object, the frame from which the plugin was
 * called, and the file to import from.
 * 
 * @author wyatt
 *
 */
public abstract class MenuPlugin extends PreferenceAccess implements MossPlugin, FileAccess {
	
	/**
	 * Processes data as required.  This method will call one of exportData(), importData(), or
	 * synchronizeData(), depending on the plugin type.  This method is only included to make
	 * it easier to call the plugins from the File menu.  Plugin implementors should *not*
	 * override this method, as you will get unpredictable results if you do.  Instead,
	 * override the correct method for your plugin type.
	 */
	public abstract void processData(MutableDocument model, MossDocumentFrame callingFrame, File file) throws PluginException, PluginMessage;
	
	/**
	 * If we show a file chooser, should it be of type 'Save'?  If 
	 * false, it will be of type Open.  This method is meant to be
	 * implemented in the Buddi*Plugin classes; plugin authors:
	 * do not implement this unless you are POSITIVE that you know 
	 * exactly what you are doing.  Also be aware that this method 
	 * may be subject to change in future versions without any notice.
	 * @return
	 */
	public abstract boolean isFileChooserSave();
	
	/**
	 * Returns the description for the plugin.  This is used in the File Chooser, as the
	 * name for the file filter.
	 * @see org.homeunix.thecave.moss.plugin.MossPlugin#getDescription()
	 */
	public abstract String getDescription();
	
	public boolean isPluginActive() {
		return true;
	}
	
	public boolean isPromptForFile(){
		return true;
	}
	
	public String[] getFileExtensions(){
		return null;
	}
	
	public Version getMaximumVersion() {
		return null;
	}

	public Version getMinimumVersion() {
		return null;
	}
}
