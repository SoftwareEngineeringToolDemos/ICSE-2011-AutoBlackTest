/*
 * Created on Aug 22, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface MutableSource extends ImmutableSource {
	
	/**
	 * Sets this source as deleted
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) throws InvalidValueException;
		
	/**
	 * Sets the name of this source
	 * @param name
	 */
	public void setName(String name) throws InvalidValueException;
	
	/**
	 * Sets the notes associated with this source
	 * @param notes
	 */
	public void setNotes(String notes) throws InvalidValueException;

}
