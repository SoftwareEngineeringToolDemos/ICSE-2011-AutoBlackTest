/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.BudgetCategory;

public interface ImmutableBudgetCategory extends ImmutableSource {
		
	/**
	 * Returns the budgeted amount for the given date.
	 * 
	 * @param date
	 * @return
	 */
	public long getAmount(Date date);
	
	/**
	 * Returns the budgeted amount spread across the date range.  For instance, given 
	 * a monthly budget period, and the date range Sept 15 - Sept 30, and September
	 * had a value of 100 for the given budget category, we would return 50.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long getAmount(Date startDate, Date endDate);
	
	/**
	 * Returns the wrapped object from the underlying data model.  By 
	 * accessing this method, you bypass all protection which the Buddi API
	 * gives you; it is not recommended to use this method unless you understand
	 * the risks associated with it. 
	 * @return
	 */
	public BudgetCategory getBudgetCategory();
	
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
	public List<ImmutableBudgetCategory> getImmutableChildren();
	
	/**
	 * Returns all children of this budget category, regardless of delete flag state. 
	 * @return
	 */
	public List<ImmutableBudgetCategory> getAllImmutableChildren();
	
	/**
	 * Returns the budget period type associated with this budget category.
	 * @return
	 */
	public ImmutableBudgetCategoryType getBudgetPeriodType();
	
	/**
	 * Returns the parent of this ImmutableBudgetCategory, or null if there is no parent.
	 * @return
	 */
	public ImmutableBudgetCategory getParent();
	
	/**
	 * Does this ImmutableBudgetCategory represent an income category?
	 * @return
	 */
	public boolean isIncome();
	
	/**
	 * Returns a list of all dates for this budget period which have budget information
	 * set for them.  Any date for this budget period which has an associated amount
	 * of anything other than zero will be returned here. 
	 * @return
	 */
	public List<Date> getBudgetedDates();
}
