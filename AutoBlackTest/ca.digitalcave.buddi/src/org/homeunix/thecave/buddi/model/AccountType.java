/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;


public interface AccountType extends ModelObject, Expandable {
	
	/**
	 * Returns the name of this account type
	 * @return
	 */
	public String getName();
	
	/**
	 * Does this account type represent a credit account? 
	 * @return
	 */
	public boolean isCredit();
	
	/**
	 * Set the credit value of this account type
	 * @param credit
	 * @throws InvalidValueException
	 */
	public void setCredit(boolean credit) throws InvalidValueException;
	
	/**
	 * Sets the name of this account type
	 * @param name
	 * @throws InvalidValueException
	 */
	public void setName(String name) throws InvalidValueException;
}
