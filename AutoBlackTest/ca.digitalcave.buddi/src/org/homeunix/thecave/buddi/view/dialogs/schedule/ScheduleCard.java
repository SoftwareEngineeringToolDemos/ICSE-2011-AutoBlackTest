/*
 * Created on Aug 18, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs.schedule;

import org.homeunix.thecave.buddi.model.ScheduledTransaction;

public interface ScheduleCard {
	/**
	 * Get the currently scheduled day value.  The meaning of this depends on the type of card.
	 * @return
	 */
	public int getScheduleDay();
	
	
	/**
	 * Get the currently scheduled week value.  The meaning of this depends on the type of card.
	 * @return
	 */
	public int getScheduleWeek();
	
	/**
	 * Get the currently scheduled week value.  The meaning of this depends on the type of card.
	 * @return
	 */
	public int getScheduleMonth();
	
	/**
	 * Loads the scheduled transaction, and updates the fields, buttons, check boxes, etc on the card.
	 * @param s
	 */
	public void loadSchedule(ScheduledTransaction s);
}
