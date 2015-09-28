/*
 * Created on Aug 22, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface MutableAccountType extends ImmutableAccountType {
	
	
	/**
	 * Sets the name of this type
	 * @param name
	 */
	public void setName(String name) throws InvalidValueException;
}
