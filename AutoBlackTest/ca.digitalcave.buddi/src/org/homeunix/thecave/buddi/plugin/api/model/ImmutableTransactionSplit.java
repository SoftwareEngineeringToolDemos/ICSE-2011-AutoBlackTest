/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;


public interface ImmutableTransactionSplit extends ImmutableModelObject {
	
	/**
	 * Returns the amounts associated with this transaction split
	 * @return
	 */
	public long getAmount();
	
	/**
	 * Returns the source associated with this transaction split
	 * @return
	 */
	public ImmutableSource getSource();

}
