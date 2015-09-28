/*
 * Created on Aug 22, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface MutableBudgetCategory extends ImmutableBudgetCategory, MutableSource {
	/**
	 * Sets the amount for the budget period which contains the given date.  This is 
	 * defined by the currently selected BudgetCategoryType object associated with
	 * this category.  
	 * @param date
	 * @param amount
	 * @throws InvalidValueException
	 */
	public void setAmount(Date date, long amount) throws InvalidValueException;
	
	/**
	 * Sets the budget period type associated with this budget category.
	 * @return
	 */
	public void setBudgetCategoryType(ImmutableBudgetCategoryType budgetCategoryType) throws InvalidValueException;
	
	/**
	 * Sets whether this budget category represents income or not.
	 * @param income
	 */
	public void setIncome(boolean income) throws InvalidValueException;
	
	/**
	 * Sets the parent of this budget category.  Set to null for no parent.
	 * @param parent
	 */
	public void setParent(MutableBudgetCategory parent) throws InvalidValueException;	
	/**
	 * Returns the parent of this ImmutableBudgetCategory, or null if there is no parent.
	 * @return
	 */
	public MutableBudgetCategory getParent();
	
	/**
	 * Returns all visible children of this budget category.  'Visible Children' are
	 * defined to be all children which do not have the deleted flag set, plus all 
	 * children which have the deleted flag set IIF the Preferences define that the user
	 * wants to see deleted sources.
	 * 
	 * This method is mostly used for GUI functions, such as reports and graphs; if you
	 * want to access the model it is usually a better idea to use the getAllChildren()
	 * method, which will return all children regardless of delete flag state.  
	 * @return
	 */
	public List<MutableBudgetCategory> getMutableChildren();
	
	/**
	 * Returns all children of this budget category, regardless of delete flag state. 
	 * @return
	 */
	public List<MutableBudgetCategory> getAllMutableChildren();
}
