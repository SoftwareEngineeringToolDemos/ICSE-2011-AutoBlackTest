/*
 * Created on Aug 18, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs.schedule;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyDayOfWeek;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyWeek;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;

import ca.digitalcave.moss.swing.MossPanel;
import ca.digitalcave.moss.swing.MossScrollingComboBox;

public class MultipleWeeksEachMonthCard extends MossPanel implements ScheduleCard {
	public static final long serialVersionUID = 0;

	private final MossScrollingComboBox multipleWeeksDayChooser = new MossScrollingComboBox(ScheduleFrequencyDayOfWeek.values());
	
	private final JCheckBox multipleWeeksMonthlyFirstWeek;
	private final JCheckBox multipleWeeksMonthlySecondWeek;
	private final JCheckBox multipleWeeksMonthlyThirdWeek;
	private final JCheckBox multipleWeeksMonthlyFourthWeek;
	
	public MultipleWeeksEachMonthCard() {
		super(true);
		
		multipleWeeksMonthlyFirstWeek = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyWeek.SCHEDULE_WEEK_FIRST.toString()));
		multipleWeeksMonthlySecondWeek = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyWeek.SCHEDULE_WEEK_SECOND.toString()));
		multipleWeeksMonthlyThirdWeek = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyWeek.SCHEDULE_WEEK_THIRD.toString()));
		multipleWeeksMonthlyFourthWeek = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyWeek.SCHEDULE_WEEK_FOURTH.toString()));	
		
		open();
	}
	
	@Override
	public void init() {
		super.init();

		this.setLayout(new BorderLayout());
		JPanel multipleWeeksMonthlyTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		multipleWeeksMonthlyTop.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.AND_REPEATING_EVERY)));
		multipleWeeksMonthlyTop.add(multipleWeeksDayChooser);
		multipleWeeksMonthlyTop.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.ON_EACH_OF_THE_FOLLOWING_WEEKS)));
		this.add(multipleWeeksMonthlyTop, BorderLayout.NORTH);
		JPanel multipleWeeksMonthlyCheckboxes = new JPanel(new GridLayout(0, 1));
		multipleWeeksMonthlyCheckboxes.add(multipleWeeksMonthlyFirstWeek);
		multipleWeeksMonthlyCheckboxes.add(multipleWeeksMonthlySecondWeek);
		multipleWeeksMonthlyCheckboxes.add(multipleWeeksMonthlyThirdWeek);
		multipleWeeksMonthlyCheckboxes.add(multipleWeeksMonthlyFourthWeek);
		this.add(multipleWeeksMonthlyCheckboxes, BorderLayout.EAST);
		
		multipleWeeksDayChooser.setRenderer(new TranslatorListCellRenderer());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		multipleWeeksDayChooser.setEnabled(enabled);
		
		multipleWeeksMonthlyFirstWeek.setEnabled(enabled);
		multipleWeeksMonthlySecondWeek.setEnabled(enabled);
		multipleWeeksMonthlyThirdWeek.setEnabled(enabled);
		multipleWeeksMonthlyFourthWeek.setEnabled(enabled);
	}
	
	public int getScheduleDay() {
		return multipleWeeksDayChooser.getSelectedIndex();
	}
	
	public int getScheduleWeek() {
		int scheduleWeek = 0;

		scheduleWeek += (multipleWeeksMonthlyFirstWeek.isSelected() ? 1 : 0);
		scheduleWeek += (multipleWeeksMonthlySecondWeek.isSelected() ? 2 : 0);
		scheduleWeek += (multipleWeeksMonthlyThirdWeek.isSelected() ? 4 : 0);
		scheduleWeek += (multipleWeeksMonthlyFourthWeek.isSelected() ? 8 : 0);

		return scheduleWeek;
	}
	
	public int getScheduleMonth() {
		return 0; //TODO This used to be -1.  Check if this change is correct or not.
	}
	
	public void loadSchedule(ScheduledTransaction s) {
		multipleWeeksDayChooser.setSelectedIndex(s.getScheduleDay());
		
		//Load the checkmarks, using bit bashing logic
		multipleWeeksMonthlyFirstWeek.setSelected((s.getScheduleWeek() & 1) != 0);
		multipleWeeksMonthlySecondWeek.setSelected((s.getScheduleWeek() & 2) != 0);
		multipleWeeksMonthlyThirdWeek.setSelected((s.getScheduleWeek() & 4) != 0);
		multipleWeeksMonthlyFourthWeek.setSelected((s.getScheduleWeek() & 8) != 0);
	}
}
