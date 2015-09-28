/*
 * Created on Aug 22, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface MutableTransactionSplit extends ImmutableTransactionSplit {
	
	/**
	 * Sets the amount associated with this transaction split
	 * @param amount
	 */
	public void setAmount(long amount) throws InvalidValueException;
	
	/**
	 * Sets the given source
	 * @param from
	 */
	public void setSource(MutableSource from) throws InvalidValueException;
}
