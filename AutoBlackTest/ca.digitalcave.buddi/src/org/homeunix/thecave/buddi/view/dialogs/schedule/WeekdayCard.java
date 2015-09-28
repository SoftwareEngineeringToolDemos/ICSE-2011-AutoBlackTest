/*
 * Created on Aug 18, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs.schedule;

import org.homeunix.thecave.buddi.model.ScheduledTransaction;

import ca.digitalcave.moss.swing.MossPanel;

public class WeekdayCard extends MossPanel implements ScheduleCard {
	public static final long serialVersionUID = 0;
	
	public int getScheduleDay() {
		return 0;
	}
	
	public int getScheduleWeek() {
		return 0;
	}
	
	public int getScheduleMonth() {
		return 0; //TODO This used to be -1.  Check if this change is correct or not.
	}
	
	public void loadSchedule(ScheduledTransaction s) {
		
	}
}
