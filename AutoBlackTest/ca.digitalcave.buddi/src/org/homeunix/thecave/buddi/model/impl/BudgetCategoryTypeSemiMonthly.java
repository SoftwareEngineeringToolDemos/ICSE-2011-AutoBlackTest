package org.homeunix.thecave.buddi.model.impl;

import java.util.Calendar;
import java.util.Date;

import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;

import ca.digitalcave.moss.common.DateUtil;

/**
 * 
 * @author mpeccorini
 */
public class BudgetCategoryTypeSemiMonthly extends BudgetCategoryType {

	@Override
	public Date getBudgetPeriodOffset(Date date, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, offset / 2);
		if (offset % 2 != 0) {
			if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
				cal.set(Calendar.DAY_OF_MONTH, 16);
				if (offset < 0) {
					cal.add(Calendar.MONTH, -1);
				}
			} else {
				cal.set(Calendar.DAY_OF_MONTH, 1);
				if (offset > 0) {
					cal.add(Calendar.MONTH, 1);
				}
			}
		}
		return getStartOfBudgetPeriod(cal.getTime());
	}

	@Override
	public String getDateFormat() {
		return "dd MMM yyyy";
	}

	@Override
	public long getDaysInPeriod(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
			return 15;
		}
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 15;
	}

	@Override
	public String getName() {
		return BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_SEMI_MONTH.toString();
	}

	@Override
	public Date getStartOfBudgetPeriod(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
		} else {
			cal.set(Calendar.DAY_OF_MONTH, 16);
		}
		return DateUtil.getStartOfDay(cal.getTime());
	}

	@Override
	public Date getEndOfBudgetPeriod(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_MONTH) <= 15) {
			cal.set(Calendar.DAY_OF_MONTH, 15);
		} else {
			cal.set(Calendar.DAY_OF_MONTH, cal
					.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return DateUtil.getEndOfDay(cal.getTime());
	}

}
