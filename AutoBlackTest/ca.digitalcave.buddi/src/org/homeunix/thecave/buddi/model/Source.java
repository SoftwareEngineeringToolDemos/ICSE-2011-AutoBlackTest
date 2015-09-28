/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface Source extends ModelObject {

	/**
	 * Returns the full name of this source.  The details of this will differ 
	 * for Accounts and Budget Categories.
	 * @return
	 */
	public String getFullName();
	
	/**
	 * Returns the name of the source 
	 * @return
	 */
	public String getName();
	
	/**
	 * Return the notes associated with this source.
	 * @return
	 */
	public String getNotes();
	
	/**
	 * Is this source marked as deleted?  When the user tries to delete a source,
	 * we try first to remove it from the model; if that cannot work (due to 
	 * transactions referencing it), we mark it as deleted.  Depending on the 
	 * preferences, we may or may not display sources which are marked as deleted.
	 * @return
	 */
	public boolean isDeleted();

	/**
	 * Sets the deleted status of this source.
	 * @param deleted
	 * @throws InvalidValueException
	 */
	public void setDeleted(boolean deleted) throws InvalidValueException;

	/**
	 * Sets the name of this source.
	 * @param name
	 * @throws InvalidValueException
	 */
	public void setName(String name) throws InvalidValueException;
	
	/**
	 * Sets the free form notes associated with this account.
	 * @param notes
	 * @throws InvalidValueException
	 */
	public void setNotes(String notes) throws InvalidValueException;
}
