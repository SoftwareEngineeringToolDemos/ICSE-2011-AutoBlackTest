/*
 * Created on Aug 11, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

import java.util.Date;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface ModelObject extends Comparable<ModelObject> {
	
	public int compareTo(ModelObject o);
	
	/**
	 * Returns the document associated with this model object, or null if
	 * there is no document. 
	 * @return
	 */
	public Document getDocument();
	
	/**
	 * Returns the UID string for this object.
	 * @return
	 */
	public String getUid();
	
	/**
	 * Sets the object as modified.  Optionally will fire a change event, if the change
	 * is major enough to warrant it.
	 */
	public void setChanged();
	
	/**
	 * Gets the last modified date of the object
	 * @return
	 */
	public Date getModified();
	
	/**
	 * Sets the document.
	 * @param document
	 */
	public void setDocument(Document document) throws InvalidValueException;
}
