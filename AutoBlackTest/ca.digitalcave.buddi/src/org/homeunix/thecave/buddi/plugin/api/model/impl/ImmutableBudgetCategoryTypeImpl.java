/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;

import org.homeunix.thecave.buddi.model.BudgetCategoryType;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategoryType;

public class ImmutableBudgetCategoryTypeImpl implements ImmutableBudgetCategoryType {
	private BudgetCategoryType budgetCategoryType;
	
	public ImmutableBudgetCategoryTypeImpl(BudgetCategoryType budgetCategoryType) {
		this.budgetCategoryType = budgetCategoryType;
	}
	
	public BudgetCategoryType getBudgetCategoryType(){
		return budgetCategoryType;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImmutableBudgetCategoryType)
			return getBudgetCategoryType().getName().equals(((ImmutableBudgetCategoryType) obj).getBudgetCategoryType().getName());
		return false;
	}
	
	@Override
	public int hashCode() {
		return getBudgetCategoryType().hashCode();
	}

	public Date getBudgetPeriodOffset(Date date, int offset) {
		return getBudgetCategoryType().getBudgetPeriodOffset(date, offset);
	}

	public String getDateFormat() {
		return getBudgetCategoryType().getDateFormat();
	}

	public long getDaysInPeriod(Date date) {
		return getBudgetCategoryType().getDaysInPeriod(date);
	}

	public Date getEndOfBudgetPeriod(Date date) {
		return getBudgetCategoryType().getEndOfBudgetPeriod(date);
	}

	public String getName() {
		return getBudgetCategoryType().getName();
	}

	public Date getStartOfBudgetPeriod(Date date) {
		return getBudgetCategoryType().getStartOfBudgetPeriod(date);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
