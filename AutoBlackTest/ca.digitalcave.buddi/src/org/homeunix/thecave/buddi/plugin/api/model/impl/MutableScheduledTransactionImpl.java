/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;

import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.model.MutableScheduledTransaction;

public class MutableScheduledTransactionImpl extends MutableTransactionImpl implements MutableScheduledTransaction {

	public MutableScheduledTransactionImpl(ScheduledTransaction scheduledTransaction) {
		super(scheduledTransaction);
	}
	
	public ScheduledTransaction getScheduledTransaction(){
		return (ScheduledTransaction) getRaw();
	}
	
	public Date getEndDate() {
		return getScheduledTransaction().getEndDate();
	}

	public String getFrequencyType() {
		return getScheduledTransaction().getFrequencyType();
	}

	public Date getLastDayCreated() {
		return getScheduledTransaction().getLastDayCreated();
	}

	public String getMessage() {
		return getScheduledTransaction().getMessage();
	}

	public int getScheduleDay() {
		return getScheduledTransaction().getScheduleDay();
	}

	public int getScheduleMonth() {
		return getScheduledTransaction().getScheduleMonth();
	}

	public String getScheduleName() {
		return getScheduledTransaction().getScheduleName();
	}

	public int getScheduleWeek() {
		return getScheduledTransaction().getScheduleWeek();
	}

	public Date getStartDate() {
		return getScheduledTransaction().getStartDate();
	}

	public void setEndDate(Date endDate) throws InvalidValueException{
		getScheduledTransaction().setEndDate(endDate);
	}

	public void setFrequencyType(String frequencyType) throws InvalidValueException{
		getScheduledTransaction().setFrequencyType(frequencyType);		
	}

	public void setLastDayCreated(Date lastDayCreated) throws InvalidValueException {
		getScheduledTransaction().setLastDayCreated(lastDayCreated);		
	}

	public void setMessage(String message) throws InvalidValueException{
		getScheduledTransaction().setMessage(message);		
	}

	public void setScheduleDay(int scheduleDay) throws InvalidValueException{
		getScheduledTransaction().setScheduleDay(scheduleDay);		
	}

	public void setScheduleMonth(int scheduleMonth) throws InvalidValueException{
		getScheduledTransaction().setScheduleMonth(scheduleMonth);		
	}

	public void setScheduleName(String scheduleName) throws InvalidValueException{
		getScheduledTransaction().setScheduleName(scheduleName);		
	}

	public void setScheduleWeek(int scheduleWeek) throws InvalidValueException{
		getScheduledTransaction().setScheduleWeek(scheduleWeek);
	}

	public void setStartDate(Date startDate) throws InvalidValueException{
		getScheduledTransaction().setStartDate(startDate);		
	}
	
	
}
