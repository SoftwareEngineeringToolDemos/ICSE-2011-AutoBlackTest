/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.Date;

import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;

import ca.digitalcave.moss.common.DateUtil;

/**
 * Definition of a Monthly BudgetCategoryType.
 *  
 * @author wyatt
 *
 */
public class BudgetCategoryTypeMonthly extends BudgetCategoryType {
	
	public Date getStartOfBudgetPeriod(Date date) {
		return DateUtil.getStartOfMonth(date);
	}
	
	public Date getEndOfBudgetPeriod(Date date) {
		return DateUtil.getEndOfMonth(date);
	}
	
	public Date getBudgetPeriodOffset(Date date, int offset) {
		return getStartOfBudgetPeriod(DateUtil.addMonths(DateUtil.getStartOfMonth(date), 1 * offset));
	}
	
	public long getDaysInPeriod(Date date) {
		return DateUtil.getDaysInMonth(date);
	}
	
	public String getDateFormat() {
		return "MMM yyyy";
	}
			
	public String getName() {
		return BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH.toString();
	}
}