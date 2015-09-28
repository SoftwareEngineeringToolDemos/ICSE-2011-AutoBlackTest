/*
 * Created on Aug 18, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs.schedule;

import java.awt.FlowLayout;

import javax.swing.JLabel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyDayOfMonth;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;

import ca.digitalcave.moss.swing.MossPanel;
import ca.digitalcave.moss.swing.MossScrollingComboBox;

public class MonthlyByDateCard extends MossPanel implements ScheduleCard {
	public static final long serialVersionUID = 0;

	private final MossScrollingComboBox monthlyDateChooser;
	
	public MonthlyByDateCard() {
		super(true);
		monthlyDateChooser = new MossScrollingComboBox(ScheduleFrequencyDayOfMonth.values());
		open();
	}
	
	@Override
	public void init() {
		super.init();

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.AND_REPEATING_ON_THE)));
		this.add(monthlyDateChooser);
		this.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.OF_EACH_MONTH)));
		monthlyDateChooser.setRenderer(new TranslatorListCellRenderer());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		monthlyDateChooser.setEnabled(enabled);
	}
	
	public int getScheduleDay() {
		//To make it nicer to read in the data file, 
		// we add 1 to the index.  Don't forget to 
		// subtract one when we load it!
		return monthlyDateChooser.getSelectedIndex() + 1;
	}
	
	public int getScheduleWeek() {
		return 0;
	}
	
	public int getScheduleMonth() {
		return 0; //TODO This used to be -1.  Check if this change is correct or not.
	}
	
	public void loadSchedule(ScheduledTransaction s) {
		monthlyDateChooser.setSelectedIndex(s.getScheduleDay() - 1);
	}
}
