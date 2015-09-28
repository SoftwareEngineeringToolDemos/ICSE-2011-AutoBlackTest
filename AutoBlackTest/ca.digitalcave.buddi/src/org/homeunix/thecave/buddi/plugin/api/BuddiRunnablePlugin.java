/*
 * Created on Sep 14, 2006 by wyatt
 * 
 * The interface which can be extended to create custom reports.
 */
package org.homeunix.thecave.buddi.plugin.api;

import ca.digitalcave.moss.application.plugin.MossRunnablePlugin;
import ca.digitalcave.moss.common.Version;

/**
 * A generic Buddi plugin, which will be loaded at the beginning of 
 * the session, just after the init() of MainFrame is completed.
 * You can use this plugin type for extending main Buddi
 * functionality, instead of adding well defined features 
 * such as Reports and Export abilities.
 * 
 * To aid in running this (and to potentially allow it to be run in 
 * a thread, if desired) this plugin implements Runnable.  When implementing
 * this class, just create the run() method with whatever code you wish.
 * 
 * 
 * @author wyatt
 */
public abstract class BuddiRunnablePlugin extends PreferenceAccess implements MossRunnablePlugin {

	public Version getMaximumVersion() {
		return null;
	}

	public Version getMinimumVersion() {
		return null;
	}
}
