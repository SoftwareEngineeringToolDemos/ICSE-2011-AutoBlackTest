/*
 * Created on Aug 11, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.digitalcave.moss.common.DateUtil;

/**
 * @author wyatt
 * @deprecated
 */
public class YearMonthSpinner extends JPanel {
	public static final long serialVersionUID = 0;
	
	private final JSpinner year;
	private final JSpinner month;
	private final SpinnerDateModel yearModel;
	private final SpinnerDateModel monthModel;
	
	public YearMonthSpinner() {
		year = new JSpinner();
		month = new JSpinner();
		
		yearModel = new SpinnerDateModel(new Date(), DateUtil.getDate(1900, Calendar.JANUARY), DateUtil.getDate(3000, Calendar.DECEMBER), Calendar.MONTH);
		monthModel = new SpinnerDateModel(new Date(), DateUtil.getDate(1900, Calendar.JANUARY), DateUtil.getDate(3000, Calendar.DECEMBER), Calendar.MONTH);
		year.setModel(yearModel);
		month.setModel(monthModel);
		
		year.setEditor(new JSpinner.DateEditor(year, "yyyy"));
		month.setEditor(new JSpinner.DateEditor(month, "MMMM"));
		
		this.setLayout(new FlowLayout());
		this.add(year);
		this.add(month);
		
		yearModel.setValue(new Date());
		monthModel.setValue(new Date());
		
		yearModel.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				monthModel.setValue(yearModel.getValue());
			}
		});
		monthModel.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				yearModel.setValue(monthModel.getValue());
			}
		});
	}
	
	public Date getDate(){
		return yearModel.getDate(); 
	}
	
	public void setDate(Date date){
		yearModel.setValue(date);
		monthModel.setValue(date);
	}
	
	public void addChangeListener(ChangeListener listener){
		yearModel.addChangeListener(listener);
		monthModel.addChangeListener(listener);
	}
	
	public void removeChangeListener(ChangeListener listener){
		yearModel.removeChangeListener(listener);
		monthModel.removeChangeListener(listener);
	}	
}
