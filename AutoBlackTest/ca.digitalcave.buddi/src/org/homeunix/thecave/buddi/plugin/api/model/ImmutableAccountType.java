/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import org.homeunix.thecave.buddi.model.AccountType;

public interface ImmutableAccountType extends ImmutableModelObject {
	
	/**
	 * Returns the name associated with this type
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the wrapped object from the underlying data model.  By 
	 * accessing this method, you bypass all protection which the Buddi API
	 * gives you; it is not recommended to use this method unless you understand
	 * the risks associated with it. 
	 * @return
	 */
	public AccountType getType();
	
	/**
	 * Does this type represent credit accounts? 
	 * @return
	 */
	public boolean isCredit();
}
