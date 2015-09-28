/*
 * Created on Aug 23, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.impl.WrapperLists;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategoryType;
import org.homeunix.thecave.buddi.plugin.api.model.MutableBudgetCategory;

public class MutableBudgetCategoryImpl extends MutableSourceImpl implements MutableBudgetCategory {

	public MutableBudgetCategoryImpl(BudgetCategory budgetCategory) {
		super(budgetCategory);
	}

	public void setIncome(boolean income) throws InvalidValueException {
		getBudgetCategory().setIncome(income);
	}

	public void setParent(MutableBudgetCategory budgetCategory) throws InvalidValueException{
		if (budgetCategory == null)
			getBudgetCategory().setParent(null);
		else
			getBudgetCategory().setParent(budgetCategory.getBudgetCategory());
	}

	public BudgetCategory getBudgetCategory() {
		return (BudgetCategory) getSource();
	}

	public long getAmount(Date startDate, Date endDate) {
		return getBudgetCategory().getAmount(startDate, endDate);
	}
	
	public void setAmount(Date date, long amount) throws InvalidValueException{
		getBudgetCategory().setAmount(date, amount);
	}
	
	public long getAmount(Date date) {
		return getBudgetCategory().getAmount(date);
	}
	
	public ImmutableBudgetCategoryType getBudgetPeriodType() {
		return new ImmutableBudgetCategoryTypeImpl(getBudgetCategory().getBudgetPeriodType());
	}

	public void setBudgetCategoryType(ImmutableBudgetCategoryType budgetCategoryType) throws InvalidValueException{
		if (budgetCategoryType != null)
			getBudgetCategory().setPeriodType(budgetCategoryType.getBudgetCategoryType());
	}
	
	public MutableBudgetCategory getParent() {
		if (getBudgetCategory().getParent() != null)
			return new MutableBudgetCategoryImpl(getBudgetCategory().getParent());
		return null;
	}

	public boolean isIncome() {
		return getBudgetCategory().isIncome();
	}
	@Override
	public String toString() {
		return getFullName();
	}
	
	public List<MutableBudgetCategory> getAllChildren() {
		return null;
	}
	
	public List<MutableBudgetCategory> getChildren() {
		return null;
	}
	
	public List<MutableBudgetCategory> getAllMutableChildren() {
		return new WrapperLists.ImmutableObjectWrapperList<MutableBudgetCategory, BudgetCategory>(getBudgetCategory().getDocument(), getBudgetCategory().getAllChildren());
	}
	
	public List<MutableBudgetCategory> getMutableChildren() {
		return new WrapperLists.ImmutableObjectWrapperList<MutableBudgetCategory, BudgetCategory>(getBudgetCategory().getDocument(), getBudgetCategory().getChildren());
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
