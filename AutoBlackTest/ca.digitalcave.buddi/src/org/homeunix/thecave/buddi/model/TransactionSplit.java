/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface TransactionSplit extends ModelObject {	
	
	public long getAmount();
	
	public Source getSource();
	
	public void setAmount(long amount);
	
	public void setSource(Source source) throws InvalidValueException;
}
