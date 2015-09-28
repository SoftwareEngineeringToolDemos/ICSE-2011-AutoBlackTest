/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface MutableScheduledTransaction extends MutableTransaction, ImmutableScheduledTransaction {
	/**
	 * Sets the end date of the scheduled transaction.  After this date, 
	 * the transaction is no longer active (although it stays in the list).
	 * As of August 31 2007, this method is not used, but is here for 
	 * a future release which will support it.
	 * @param endDate
	 * @throws InvalidValueException
	 */
	public void setEndDate(Date endDate) throws InvalidValueException;

	/**
	 * Sets the frequncy type.  This is a string representation of
	 * one of the ScheduleFrequency enum values.
	 * @param frequencyType
	 * @throws InvalidValueException
	 */
	public void setFrequencyType(String frequencyType) throws InvalidValueException;

	/**
	 * Sets the last day which this scheduled transaction was used on.  This
	 * is used to determine where to start looking for the next iteration.
	 * @param lastDayCreated
	 * @throws InvalidValueException
	 */
	public void setLastDayCreated(Date lastDayCreated) throws InvalidValueException;

	/**
	 * Sets the message associated with the scheduled transaction.
	 * @param message
	 * @throws InvalidValueException
	 */
	public void setMessage(String message) throws InvalidValueException;
	
	/**
	 * Sets an integer representation of the scheduled transaction day.  Depending
	 * on the value of getFrequencyType(), this value may mean different things, or may
	 * not be used at all. 
	 * @param scheduleDay
	 * @throws InvalidValueException
	 */
	public void setScheduleDay(int scheduleDay) throws InvalidValueException;
	
	/**
	 * Sets an integer representation of the scheduled transaction month.  Depending
	 * on the value of getFrequencyType(), this value may mean different things, or may
	 * not be used at all. 
	 * @param scheduleMonth
	 * @throws InvalidValueException
	 */
	public void setScheduleMonth(int scheduleMonth) throws InvalidValueException;

	/**
	 * Sets the name of this scheduled transaction.  This is the name
	 * which will appear in the list of scheduled transactions. 
	 * @param scheduleName
	 * @throws InvalidValueException
	 */
	public void setScheduleName(String scheduleName) throws InvalidValueException;

	/**
	 * Sets an integer representation of the scheduled transaction week.  Depending
	 * on the value of getFrequencyType(), this value may mean different things, or may
	 * not be used at all. 
	 * @param scheduleWeek
	 * @throws InvalidValueException
	 */
	public void setScheduleWeek(int scheduleWeek) throws InvalidValueException;
	
	/**
	 * Returns the starting day of this scheduled transaction.  No transactions
	 * will be added before this date.
	 * @param startDate
	 * @throws InvalidValueException
	 */
	public void setStartDate(Date startDate) throws InvalidValueException;
}
