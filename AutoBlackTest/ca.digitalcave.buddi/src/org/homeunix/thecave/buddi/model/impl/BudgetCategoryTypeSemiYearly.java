/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.Calendar;
import java.util.Date;

import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;

import ca.digitalcave.moss.common.DateUtil;

/**
 * Definition of a Semi Yearly BudgetCategoryType
 * 
 * @author wyatt
 *
 */
public class BudgetCategoryTypeSemiYearly extends BudgetCategoryType {
	
	public Date getStartOfBudgetPeriod(Date date) {
		if (DateUtil.getMonth(date) <= Calendar.JUNE)
			return DateUtil.getStartOfYear(date);
		else
			return DateUtil.getDate(DateUtil.getYear(date), Calendar.JULY);
	}
	
	public Date getEndOfBudgetPeriod(Date date) {
		if (DateUtil.getMonth(date) <= Calendar.JUNE)
			return DateUtil.getEndOfMonth(DateUtil.getDate(DateUtil.getYear(date), Calendar.JUNE));
		else
			return DateUtil.getEndOfYear(date);
	}
	
	public Date getBudgetPeriodOffset(Date date, int offset) {
		return getStartOfBudgetPeriod(DateUtil.addMonths(date, offset * 6));
	}
	
	public long getDaysInPeriod(Date date) {
		return DateUtil.getDaysBetween(getStartOfBudgetPeriod(date), getEndOfBudgetPeriod(date), true);
	}
	
	public String getDateFormat() {
		return "MMM yyyy";
	}
			
	public String getName() {
		return BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_SEMI_YEAR.toString();
	}
}