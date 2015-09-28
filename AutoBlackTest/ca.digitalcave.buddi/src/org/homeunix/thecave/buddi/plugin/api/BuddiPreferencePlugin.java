/*
 * Created on Sep 14, 2006 by wyatt
 * 
 * The interface which can be extended to create custom reports.
 */
package org.homeunix.thecave.buddi.plugin.api;

import javax.swing.JPanel;

import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;

import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.common.Version;

/**
 * A Buddi plugin which will be loaded into the Preferences screen.  If your plugin needs
 * user configuration, it is highly recommended to use this class to perform that.
 * 
 * It is quite simple to do this; you need to create a JPanel of the component layout
 * in getPreferencesPanel(), and provide the logic to load and save these values in
 * the load() and save() methods.
 * 
 * See the documentation for BuddiPluginPreference to see how to access the preferences file.
 * 
 * @author wyatt
 * 
 */
public abstract class BuddiPreferencePlugin extends PreferenceAccess implements MossPlugin {
	
	/**
	 * Saves the preferences which this panel is responsible for.  If the changes nessecitate
	 * a restart, return true; otherwise, return false.
	 */
	public abstract boolean save() throws PluginException;
	
	/**
	 * Loads the preferences which this panel is responsible for.
	 */
	public abstract void load() throws PluginException;

	/**
	 * If true, we put a JPanel wrapper around this JPanel, so that it keeps 
	 * all the components at the top of the window, even if there is extra
	 * room.  This is correct behaviour for most preference panes, especially
	 * if they contain multiple small widgets (buttons, check boxes, etc).
	 * 
	 * If the panel is to contain a large widget which is to take up the entire
	 * frame (such as seen in the built in Plugins preference pane), override this
	 * and set to false.
	 * @return
	 */
	public boolean isUseWrapper(){
		return true;
	}
	
	/**
	 * Create the JPanel to show in the Preferences.
	 * @return A JPanel object which can be put into the Preferences panel
	 */
	public abstract JPanel getPreferencesPanel();
	
	/**
	 * Not used in PreferencePlugin
	 */
	public boolean isPluginActive() {
		return true;
	}
	
	/**
	 * Not used in PreferencePlugin
	 */
	public String getDescription() {
		return null;
	}
	
	public Version getMaximumVersion() {
		return null;
	}

	public Version getMinimumVersion() {
		return null;
	}
}
