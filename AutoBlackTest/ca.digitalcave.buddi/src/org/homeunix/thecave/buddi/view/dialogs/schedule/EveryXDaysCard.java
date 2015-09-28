/*
 * Created on Aug 18, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs.schedule;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.swing.MossDecimalField;
import ca.digitalcave.moss.swing.MossPanel;

public class EveryXDaysCard extends MossPanel implements ScheduleCard {
	public static final long serialVersionUID = 0;
	
	private final MossDecimalField howFrequently;
	
	public EveryXDaysCard() {
		super(true);
		howFrequently = new MossDecimalField(7, false, 0);	
		howFrequently.setPreferredSize(new Dimension(100, howFrequently.getPreferredSize().height));
		open();
	}
	
	@Override
	public void init() {
		super.init();

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.AND_REPEATING_EVERY)));
		this.add(howFrequently);
		this.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.DAYS)));
	}
	
	public int getScheduleDay() {
		try {
			return Integer.parseInt(howFrequently.getText());
		}
		catch (RuntimeException re){}
		catch (Exception e){}
		return 7;	//If we can't parse it, return 7.  TODO This is stupid, do some error handling at the component level
	}
	
	public int getScheduleWeek() {
		return 0;
	}
	
	public int getScheduleMonth() {
		return 0;
	}
	
	public void loadSchedule(ScheduledTransaction s) {
		howFrequently.setText(s.getScheduleDay() + "");
	}
}
