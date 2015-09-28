/*
 * Created on Aug 25, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import java.util.Date;

import javax.swing.SpinnerDateModel;

public class BudgetDateSpinnerModel extends SpinnerDateModel {
	public static final long serialVersionUID = 0;
	
	private final MyBudgetTreeTableModel budgetModel;
	
	public BudgetDateSpinnerModel(MyBudgetTreeTableModel budgetModel) {
		super();
		this.setValue(new Date());
		this.budgetModel = budgetModel;
	}
		
	public Object getNextValue() {
		return budgetModel.getSelectedBudgetPeriodType().getBudgetPeriodOffset(getDate(), 1);
	}
	
	public Object getPreviousValue() {
		return budgetModel.getSelectedBudgetPeriodType().getBudgetPeriodOffset(getDate(), -1);
	}
	
	@Override
	public void setCalendarField(int calendarField) {}
}
