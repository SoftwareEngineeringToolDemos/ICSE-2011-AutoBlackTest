/*
 * Created on Aug 22, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface MutableTransaction extends ImmutableTransaction {
	
	/**
	 * Sets the amount associated with this transaction
	 * @param amount
	 */
	public void setAmount(long amount) throws InvalidValueException;
	
	/**
	 * Marks this transaction as cleared
	 * @param cleared
	 */
	public void setClearedFrom(boolean cleared) throws InvalidValueException;

	/**
	 * Marks this transaction as cleared
	 * @param cleared
	 */
	public void setClearedTo(boolean cleared) throws InvalidValueException;
	
	/**
	 * Sets the date associated with this account
	 * @param date
	 */
	public void setDate(Date date) throws InvalidValueException;
	
	/**
	 * Sets the description associated with this account
	 * @param description
	 */
	public void setDescription(String description) throws InvalidValueException;
	
	/**
	 * Add a split.  You must also set exactly one of 'to' or 'from
	 * to an instance of ImmutableSplit.
	 * @param splits
	 */
	public void addFromSplit(MutableTransactionSplit split) throws InvalidValueException;
	
	/**
	 * Remove a split.
	 * @param split
	 */
	public void removeFromSplit(MutableTransactionSplit split) throws InvalidValueException;
	
	/**
	 * Add a split.  You must also set exactly one of 'to' or 'from
	 * to an instance of ImmutableSplit.
	 * @param splits
	 */
	public void addToSplit(MutableTransactionSplit split) throws InvalidValueException;
	
	/**
	 * Remove a split.
	 * @param split
	 */
	public void removeToSplit(MutableTransactionSplit split) throws InvalidValueException;
	
	/**
	 * Sets the given source as the From field
	 * @param from
	 */
	public void setFrom(MutableSource from) throws InvalidValueException;
	
	/**
	 * Sets the given memo for this transaction
	 * @param memo
	 */
	public void setMemo(String memo) throws InvalidValueException;
	
	/**
	 * Sets the cheque number for this transaction
	 * @param number
	 */
	public void setNumber(String number) throws InvalidValueException;
	
	/**
	 * Marks this transactin as reconciled
	 * @param reconciled
	 */
	public void setReconciledFrom(boolean reconciled) throws InvalidValueException;

	/**
	 * Marks this transactin as reconciled
	 * @param reconciled
	 */
	public void setReconciledTo(boolean reconciled) throws InvalidValueException;
	
	/**
	 * Set this flag if the transaction was created via a scheduled transaction.
	 * @param scheduled
	 */
	public void setScheduled(boolean scheduled) throws InvalidValueException;
	
	/**
	 * Sets the given source as the To field
	 * @param to
	 */
	public void setTo(MutableSource to) throws InvalidValueException;
	
	
	
	/**
	 * Returns the source associated with this transaction's From field
	 * @return
	 */
	public MutableSource getFrom();

	/**
	 * Returns the source associated with this transaction's To field
	 * @return
	 */
	public MutableSource getTo();
	
	/**
	 * Sets the deleted state of the transaction
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) throws InvalidValueException;
}
