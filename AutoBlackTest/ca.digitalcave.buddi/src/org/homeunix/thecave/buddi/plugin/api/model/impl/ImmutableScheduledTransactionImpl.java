/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;

import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableScheduledTransaction;

public class ImmutableScheduledTransactionImpl extends ImmutableTransactionImpl implements ImmutableScheduledTransaction {

	public ImmutableScheduledTransactionImpl(ScheduledTransaction scheduledTransaction) {
		super(scheduledTransaction);
	}
	
	public ScheduledTransaction getTransaction(){
		return (ScheduledTransaction) getRaw();
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
}
