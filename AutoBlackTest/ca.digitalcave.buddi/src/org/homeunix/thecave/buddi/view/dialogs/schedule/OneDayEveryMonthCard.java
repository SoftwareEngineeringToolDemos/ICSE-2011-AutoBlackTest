/*
 * Created on Aug 18, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs.schedule;

import java.awt.FlowLayout;

import javax.swing.JLabel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyFirstWeekOfMonth;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;

import ca.digitalcave.moss.swing.MossPanel;
import ca.digitalcave.moss.swing.MossScrollingComboBox;

public class OneDayEveryMonthCard extends MossPanel implements ScheduleCard {
	public static final long serialVersionUID = 0;

	private final MossScrollingComboBox monthlyFirstDayChooser;
	
	public OneDayEveryMonthCard() {
		super(true);
		monthlyFirstDayChooser = new MossScrollingComboBox(ScheduleFrequencyFirstWeekOfMonth.values());	
		open();
	}
	
	@Override
	public void init() {
		super.init();

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.AND_REPEATING_ON_THE)));
		this.add(monthlyFirstDayChooser);
		this.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.OF_EACH_MONTH)));
		
		monthlyFirstDayChooser.setRenderer(new TranslatorListCellRenderer());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		monthlyFirstDayChooser.setEnabled(enabled);
	}
	
	public int getScheduleDay() {
		//Be careful here - this depends on the order of the order of ScheduleFrequencyDayOfMonth enum.
		// Before we can change the order here (or add more special meanings), we will need to assign
		// a special value to each meaning.  Just be careful.
		return monthlyFirstDayChooser.getSelectedIndex();
	}
	
	public int getScheduleWeek() {
		return 0;
	}
	
	public int getScheduleMonth() {
		return 0; //TODO This used to be -1.  Check if this change is correct or not.
	}
	
	public void loadSchedule(ScheduledTransaction s) {
		monthlyFirstDayChooser.setSelectedIndex(s.getScheduleDay());	
	}
}
