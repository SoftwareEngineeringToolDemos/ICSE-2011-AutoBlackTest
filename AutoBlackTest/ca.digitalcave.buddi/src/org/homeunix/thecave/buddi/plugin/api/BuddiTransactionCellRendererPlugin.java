/*
 * Created on Nov 5, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api;

import javax.swing.DefaultListCellRenderer;

import org.homeunix.thecave.buddi.model.Account;

import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.common.Version;

/**
 * Create a plugin of this type to allow custom cell renderers for the transaction
 * window.  If there are no custom renderers defined, we use the default renderer;
 * if there is exactly one custom renderer, we use it; if there are more than one
 * custom renderers, we use the first one which is loaded (most likely the one
 * which is first in alphabetical order, but this may differ by platform).
 *  
 * @author wyatt
 *
 */
public abstract class BuddiTransactionCellRendererPlugin extends DefaultListCellRenderer implements MossPlugin {
	private static final long serialVersionUID = 0L;
	private Account account;
	
	public String getDescription() {
		return null;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Version getMaximumVersion() {
		return null;
	}
	
	public Version getMinimumVersion() {
		//This plugin type was not implemented until 2.9.15.0
		return new Version("2.9.15.0");
	}
	
	public boolean isPluginActive() {
		return true;
	}
	
	@Override
	public String getName() {
		return "Change Me";
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BuddiTransactionCellRendererPlugin)
			return obj.hashCode() == this.hashCode();
		return false;
	}
}
