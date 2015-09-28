/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.impl.WrapperLists;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategoryType;

public class ImmutableBudgetCategoryImpl extends ImmutableSourceImpl implements ImmutableBudgetCategory {
	
	public ImmutableBudgetCategoryImpl(BudgetCategory budgetCategory) {
		super(budgetCategory);
	}
	
	public boolean isIncome() {
		return getBudgetCategory().isIncome();
	}
	public ImmutableBudgetCategory getParent() {
		if (getBudgetCategory().getParent() != null)
			return new MutableBudgetCategoryImpl(getBudgetCategory().getParent());
		return null;
	}
	
	public BudgetCategory getBudgetCategory(){
		return (BudgetCategory) getRaw();
	}
	
	public long getAmount(Date startDate, Date endDate) {
		return getBudgetCategory().getAmount(startDate, endDate);
	}
	
	public long getAmount(Date date) {
		return getBudgetCategory().getAmount(date);
	}
	
	public ImmutableBudgetCategoryType getBudgetPeriodType() {
		return new ImmutableBudgetCategoryTypeImpl(getBudgetCategory().getBudgetPeriodType());
	}
	
	@Override
	public String toString() {
		return getFullName();
	}

	public List<ImmutableBudgetCategory> getAllImmutableChildren() {
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableBudgetCategory, BudgetCategory>(getBudgetCategory().getDocument(), getBudgetCategory().getAllChildren());
	}
	
	public List<ImmutableBudgetCategory> getImmutableChildren() {
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableBudgetCategory, BudgetCategory>(getBudgetCategory().getDocument(), getBudgetCategory().getChildren());
	}
	
	public List<Date> getBudgetedDates() {
		return getBudgetCategory().getBudgetedDates();
	}
}
