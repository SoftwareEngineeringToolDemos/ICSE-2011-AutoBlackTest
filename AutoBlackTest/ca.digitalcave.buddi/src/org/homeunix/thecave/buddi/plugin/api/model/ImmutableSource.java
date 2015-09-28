/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import org.homeunix.thecave.buddi.model.Source;

public interface ImmutableSource extends ImmutableModelObject {
	
	/**
	 * Returns the full name of this source.  This differs for Account and Budget
	 * Categories: for Accounts, it returns getName() and getType().getName();
	 * for Budget Categories, it just returns getName().
	 * @return
	 */
	public String getFullName();
	
	/**
	 * Returns the name of this source 
	 * @return
	 */
	public String getName();
	
	/**
	 * Return any notes associated with this source
	 * @return
	 */
	public String getNotes();
	
	/**
	 * Returns the wrapped object from the underlying data model.  By 
	 * accessing this method, you bypass all protection which the Buddi API
	 * gives you; it is not recommended to use this method unless you understand
	 * the risks associated with it. 
	 * @return
	 */
	public Source getSource();
	
	/**
	 * Is this source deleted?
	 * @return
	 */
	public boolean isDeleted();
}
