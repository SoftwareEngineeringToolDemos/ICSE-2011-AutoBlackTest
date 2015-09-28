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
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyDayOfMonth;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyMonth;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;

import ca.digitalcave.moss.swing.MossPanel;
import ca.digitalcave.moss.swing.MossScrollingComboBox;

public class MultipleMonthsEachYearCard extends MossPanel implements ScheduleCard {
	public static final long serialVersionUID = 0;

	private final MossScrollingComboBox multipleMonthsDateChooser = new MossScrollingComboBox(ScheduleFrequencyDayOfMonth.values());
	
	private final JCheckBox multipleMonthsYearlyJanuary;
	private final JCheckBox multipleMonthsYearlyFebruary;
	private final JCheckBox multipleMonthsYearlyMarch;
	private final JCheckBox multipleMonthsYearlyApril;
	private final JCheckBox multipleMonthsYearlyMay;
	private final JCheckBox multipleMonthsYearlyJune;
	private final JCheckBox multipleMonthsYearlyJuly;
	private final JCheckBox multipleMonthsYearlyAugust;
	private final JCheckBox multipleMonthsYearlySeptember;
	private final JCheckBox multipleMonthsYearlyOctober;
	private final JCheckBox multipleMonthsYearlyNovember;
	private final JCheckBox multipleMonthsYearlyDecember;

	public MultipleMonthsEachYearCard() {
		super(true);
		
		multipleMonthsYearlyJanuary = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_JANUARY.toString()));
		multipleMonthsYearlyFebruary = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_FEBRUARY.toString()));
		multipleMonthsYearlyMarch = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_MARCH.toString()));
		multipleMonthsYearlyApril = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_APRIL.toString()));
		multipleMonthsYearlyMay = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_MAY.toString()));
		multipleMonthsYearlyJune = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_JUNE.toString()));
		multipleMonthsYearlyJuly = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_JULY.toString()));
		multipleMonthsYearlyAugust = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_AUGUST.toString()));
		multipleMonthsYearlySeptember = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_SEPTEMBER.toString()));
		multipleMonthsYearlyOctober = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_OCTOBER.toString()));
		multipleMonthsYearlyNovember = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_NOVEMBER.toString()));
		multipleMonthsYearlyDecember = new JCheckBox(TextFormatter.getTranslation(ScheduleFrequencyMonth.SCHEDULE_MONTH_DECEMBER.toString()));

		open();
	}
	
	@Override
	public void init() {
		super.init();

		this.setLayout(new BorderLayout());
		JPanel multipleMonthsYearlyTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		multipleMonthsYearlyTop.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.AND_REPEATING_ON_THE)));
		multipleMonthsYearlyTop.add(multipleMonthsDateChooser);
		multipleMonthsYearlyTop.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.ON_EACH_OF_THE_FOLLOWING_MONTHS)));
		this.add(multipleMonthsYearlyTop, BorderLayout.NORTH);
		JPanel multipleMonthsYearlyCheckboxes = new JPanel(new GridLayout(4, 0));
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyJanuary);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyFebruary);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyMarch);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyApril);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyMay);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyJune);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyJuly);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyAugust);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlySeptember);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyOctober);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyNovember);
		multipleMonthsYearlyCheckboxes.add(multipleMonthsYearlyDecember);
		this.add(multipleMonthsYearlyCheckboxes, BorderLayout.EAST);
		
		multipleMonthsDateChooser.setRenderer(new TranslatorListCellRenderer());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		multipleMonthsDateChooser.setEnabled(enabled);
		
		multipleMonthsYearlyJanuary.setEnabled(enabled);
		multipleMonthsYearlyFebruary.setEnabled(enabled);
		multipleMonthsYearlyMarch.setEnabled(enabled);
		multipleMonthsYearlyApril.setEnabled(enabled);
		multipleMonthsYearlyMay.setEnabled(enabled);
		multipleMonthsYearlyJune.setEnabled(enabled);
		multipleMonthsYearlyJuly.setEnabled(enabled);
		multipleMonthsYearlyAugust.setEnabled(enabled);
		multipleMonthsYearlySeptember.setEnabled(enabled);
		multipleMonthsYearlyOctober.setEnabled(enabled);
		multipleMonthsYearlyNovember.setEnabled(enabled);
		multipleMonthsYearlyDecember.setEnabled(enabled);
	}
	
	public int getScheduleDay() {
		//To make it nicer to read in the data file, we add 1 
		// to the index.  Don't forget to subtract one when we load it!
		return multipleMonthsDateChooser.getSelectedIndex() + 1;
	}
	
	public int getScheduleWeek() {
		return 0;
	}
	
	public int getScheduleMonth() {
		int value = 0;

		//Returns a value representing all selected months.  We store these
		// in the bit values: January = 1, February = 2, March = 4, April = 8,
		// etc up to December = 2048.  To extract these again, you just have
		// to AND the value with a bitmask of January = 1, etc; if the
		// resulting value is 0, that month is not selected, if != 0, it is.
		value += (multipleMonthsYearlyJanuary.isSelected() ? 1 : 0 );
		value += (multipleMonthsYearlyFebruary.isSelected() ? 2 : 0 );
		value += (multipleMonthsYearlyMarch.isSelected() ? 4 : 0 );
		value += (multipleMonthsYearlyApril.isSelected() ? 8 : 0 );
		value += (multipleMonthsYearlyMay.isSelected() ? 16 : 0 );
		value += (multipleMonthsYearlyJune.isSelected() ? 32 : 0 );
		value += (multipleMonthsYearlyJuly.isSelected() ? 64 : 0 );
		value += (multipleMonthsYearlyAugust.isSelected() ? 128 : 0 );
		value += (multipleMonthsYearlySeptember.isSelected() ? 256 : 0 );
		value += (multipleMonthsYearlyOctober.isSelected() ? 512 : 0 );
		value += (multipleMonthsYearlyNovember.isSelected() ? 1024 : 0 );
		value += (multipleMonthsYearlyDecember.isSelected() ? 2048 : 0 );

		return value;
	}
	
	public void loadSchedule(ScheduledTransaction s) {
		//To make it nicer to read in the data file, we subtract 1 
		// from the index.  Don't forget to add one when we get it!
		multipleMonthsDateChooser.setSelectedIndex(s.getScheduleDay() - 1);
		
		//Load the checkmarks, using bit bashing logic
		multipleMonthsYearlyJanuary.setSelected((s.getScheduleMonth() & 1) != 0);
		multipleMonthsYearlyFebruary.setSelected((s.getScheduleMonth() & 2) != 0);
		multipleMonthsYearlyMarch.setSelected((s.getScheduleMonth() & 4) != 0);
		multipleMonthsYearlyApril.setSelected((s.getScheduleMonth() & 8) != 0);
		multipleMonthsYearlyMay.setSelected((s.getScheduleMonth() & 16) != 0);
		multipleMonthsYearlyJune.setSelected((s.getScheduleMonth() & 32) != 0);
		multipleMonthsYearlyJuly.setSelected((s.getScheduleMonth() & 64) != 0);
		multipleMonthsYearlyAugust.setSelected((s.getScheduleMonth() & 128) != 0);
		multipleMonthsYearlySeptember.setSelected((s.getScheduleMonth() & 256) != 0);
		multipleMonthsYearlyOctober.setSelected((s.getScheduleMonth() & 512) != 0);
		multipleMonthsYearlyNovember.setSelected((s.getScheduleMonth() & 1024) != 0);
		multipleMonthsYearlyDecember.setSelected((s.getScheduleMonth() & 2048) != 0);	
	}
}
