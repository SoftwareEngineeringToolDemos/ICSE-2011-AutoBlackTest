/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

import java.util.Date;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

/**
 * @author wyatt
 *
 */
public interface ScheduledTransaction extends Transaction {

	/**
	 * Returns the end date
	 * @return
	 */
	public Date getEndDate();
	
	/**
	 * Sets the frequency type
	 * @return
	 */
	public String getFrequencyType();
	
	/**
	 * Gets the last day on which a transaction was created.  If there have been
	 * no transactions created from this schedule yet, return null.
	 * @return
	 */
	public Date getLastDayCreated();
	
	/**
	 * Returns the message with this scheduled transaction.
	 * @return
	 */
	public String getMessage();
	
	/**
	 * Returns the scheduled day.  This may have different meanings depending on the 
	 * frequency type.
	 * @return
	 */
	public int getScheduleDay();
	
	/**
	 * Returns the scheduled month.  This may have different meanings depending on the
	 *frequency type.
	 * @return
	 */
	public int getScheduleMonth();
	
	/**
	 * Returns the name of this scheduled transaction.  This is the name which shows up
	 * in the main scheduled transaction list.
	 * @return
	 */
	public String getScheduleName();
	
	/**
	 * Returns the scheduled week.  This may have different meanings depending on the
	 * frequency type.
	 * @return
	 */
	public int getScheduleWeek();
	
	/**
	 * Returns the date on which this scheduled transaction is to start. 
	 * @return
	 */
	public Date getStartDate();
	
	/**
	 * Sets the end date for this scheduled transaction.
	 * @param endDate
	 * @throws InvalidValueException
	 */
	public void setEndDate(Date endDate) throws InvalidValueException;
	
	/**
	 * Sets the frequency type for this scheduled transaction.  This must
	 * be the string representation of one of the ScheduleFrequency enum
	 * values.
	 * @param frequencyType
	 * @throws InvalidValueException
	 */
	public void setFrequencyType(String frequencyType) throws InvalidValueException;
	
	/**
	 * Sets the last day created.  This is used to keep track of where we left 
	 * off with the last scheduled transaction, to avoid duplicates and keep
	 * the time needed to run schedule checks to a minimum.
	 * @param lastDayCreated
	 * @throws InvalidValueException
	 */
	public void setLastDayCreated(Date lastDayCreated) throws InvalidValueException;
	
	/**
	 * Sets the message
	 * @param message
	 * @throws InvalidValueException
	 */
	public void setMessage(String message) throws InvalidValueException;
	
	/**
	 * Sets the schedule day.  This has different meanings based on the schedule frequency.
	 * @param scheduleDay
	 * @throws InvalidValueException
	 */
	public void setScheduleDay(int scheduleDay) throws InvalidValueException;
	
	/**
	 * Sets the schedule month.  This has different meanings based on the schedule frequency.
	 * @param scheduleMonth
	 * @throws InvalidValueException
	 */
	public void setScheduleMonth(int scheduleMonth) throws InvalidValueException;
	
	/**
	 * Sets the scheduled transaction name
	 * @param scheduleName
	 * @throws InvalidValueException
	 */
	public void setScheduleName(String scheduleName) throws InvalidValueException;
	
	/**
	 * Sets the schedule week.  This has different meanings based on the schedule frequency.
	 * @param scheduleWeek
	 * @throws InvalidValueException
	 */
	public void setScheduleWeek(int scheduleWeek) throws InvalidValueException;
	
	/**
	 * Sets the start date for this scheduled transaction.
	 * @param startDate
	 * @throws InvalidValueException
	 */
	public void setStartDate(Date startDate) throws InvalidValueException;
}
