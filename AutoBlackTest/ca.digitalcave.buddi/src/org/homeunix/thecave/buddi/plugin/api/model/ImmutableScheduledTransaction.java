/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;

import org.homeunix.thecave.buddi.model.ScheduledTransaction;

public interface ImmutableScheduledTransaction extends ImmutableTransaction {
	
	/**
	 * Returns the wrapped object from the underlying data model.  By 
	 * accessing this method, you bypass all protection which the Buddi API
	 * gives you; it is not recommended to use this method unless you understand
	 * the risks associated with it. 
	 * @return
	 */
	public ScheduledTransaction getScheduledTransaction();
	
	/**
	 * Returns the end date of the scheduled transaction.  After this date, 
	 * the transaction is no longer active (although it stays in the list).
	 * As of August 31 2007, this method is not used, but is here for 
	 * a future release which will support it.
	 * @return
	 */
	public Date getEndDate();
	
	/**
	 * Returns the frequncy type.  This is a string representation of
	 * one of the ScheduleFrequency enum values.
	 * @return
	 */
	public String getFrequencyType();

	/**
	 * Returns the last day which this scheduled transaction was used on.  This
	 * is used to determine where to start looking for the next iteration.
	 * @return
	 */
	public Date getLastDayCreated();
	
	/**
	 * Returns the message associated with the scheduled transaction.
	 * @return
	 */
	public String getMessage();

	/**
	 * Returns an integer representation of the scheduled transaction day.  Depending
	 * on the value of getFrequencyType(), this value may mean different things, or may
	 * not be used at all. 
	 * @return
	 */
	public int getScheduleDay();
	/**
	 * Returns an integer representation of the scheduled transaction month.  Depending
	 * on the value of getFrequencyType(), this value may mean different things, or may
	 * not be used at all. 
	 * @return
	 */
	public int getScheduleMonth();

	/**
	 * Returns the name of this scheduled transaction.  This is the name
	 * which will appear in the list of scheduled transactions. 
	 * @return
	 */
	public String getScheduleName();

	/**
	 * Returns an integer representation of the scheduled transaction week.  Depending
	 * on the value of getFrequencyType(), this value may mean different things, or may
	 * not be used at all. 
	 * @return
	 */
	public int getScheduleWeek();
	
	/**
	 * Returns the starting day of this scheduled transaction.  No transactions
	 * will be added before this date.
	 * @return
	 */
	public Date getStartDate();
}
