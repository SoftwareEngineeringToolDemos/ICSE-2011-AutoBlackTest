/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.Transaction;

public interface ImmutableTransaction extends ImmutableModelObject {
	
	/**
	 * Returns the amouns associated with this transaction
	 * @return
	 */
	public long getAmount();
	
	/**
	 * Returns the balance from the given account at the point in time of this transaction
	 * @return
	 */
	public long getBalance(ImmutableSource source);

	/**
	 * Returns the date associated with this transaction
	 * @return
	 */
	public Date getDate();
	
	/**
	 * Returns the description associated with this transaction
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Returns the source associated with this transaction's From field
	 * @return
	 */
	public ImmutableSource getFrom();
	
	/**
	 * Returns the memo associated with this transaction
	 * @return
	 */
	public String getMemo();
	
	/**
	 * Returns the cheque number associated with this transaction
	 * @return
	 */
	public String getNumber();

	/**
	 * Returns the source associated with this transaction's To field
	 * @return
	 */
	public ImmutableSource getTo();
	
	/**
	 * Returns a list of from splits
	 * @return
	 */
	public List<ImmutableTransactionSplit> getImmutableFromSplits();

	/**
	 * Returns a list of to splits
	 * @return
	 */
	public List<ImmutableTransactionSplit> getImmutableToSplits();
	
	/**
	 * Returns the wrapped object from the underlying data model.  By 
	 * accessing this method, you bypass all protection which the Buddi API
	 * gives you; it is not recommended to use this method unless you understand
	 * the risks associated with it. 
	 * @return
	 */
	public Transaction getTransaction();
	
	/**
	 * Is this transaction marked as cleared?
	 * @return
	 */
	public boolean isClearedFrom();
	
	/**
	 * Is this transaction marked as cleared?
	 * @return
	 */
	public boolean isClearedTo();
	
	/**
	 * Does this transacton represent an inflow of cash or an outflow?
	 * @return
	 */
	public boolean isInflow();
	
	/**
	 * Is this transaction marked as reconciled?
	 * @return
	 */
	public boolean isReconciledFrom();
	
	/**
	 * Is this transaction marked as reconciled?
	 * @return
	 */
	public boolean isReconciledTo();
	
	/**
	 * Was this transaction created through a scheduled transaction?
	 * @return
	 */
	public boolean isScheduled();
	
	/**
	 * Is the transaction marked as deleted?
	 * @return
	 */
	public boolean isDeleted();
}
