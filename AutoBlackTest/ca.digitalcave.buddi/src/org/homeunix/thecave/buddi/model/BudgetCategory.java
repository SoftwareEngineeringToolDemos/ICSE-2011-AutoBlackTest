/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface BudgetCategory extends Source, Expandable {	
	
	/**
	 * Returns the budgeted amount associated with this given budget category, for 
	 * the date in which the given period date exists.
	 * @param periodDate
	 * @return
	 */
	public long getAmount(Date periodDate);
	
	/**
	 * Returns the budgeted amount associated with this budget category, over
	 * the given date range.  If the start and / or end dates do not fall exactly
	 * on a budget period, then we calculate how much of the budget period
	 * is contained.
	 * 
	 * For instance, assume that we pass in April 15 and July 20 as the dates, and that
	 * the associated budget category type is 'Monthly'.  Assume that for April we have
	 * budgeted $100, for May and June we have budgeted $200 each, and for July we have 
	 * budgeted $300.
	 * 
	 * To find the final amount, we will add $50 (15 days / 30 days for April * $100)
	 * and $200 (May) and $200 (June) and $193 (20 days / 31 days for July * $300), for a
	 * total of $643.
	 * 
	 * If you pass in the start date as the first day in the period, and the end day
	 * as the last day, you will get back the same number as if you called getAmount(Date)
	 * with a day in the same budget period. 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long getAmount(Date startDate, Date endDate);
		
	/**
	 * Returns the Budget Period type.  One of the values in Enum BudgePeriodKeys.
	 * @return
	 */
	public BudgetCategoryType getBudgetPeriodType();
	
	/**
	 * Returns the parent budget category
	 * @return
	 */
	public BudgetCategory getParent();
		
	/**
	 * Returns a list of children for this budget category.  The contents of this list
	 * will include only children which match the current preferences for deleted items.
	 * This means that if the user has specified to only show non-deleted sources, we
	 * will not return deleted children here.  To get a list of all children, regardles 
	 * of deleted state, use the getAllChildren() method. 
	 * @return
	 */
	public List<BudgetCategory> getChildren();
	
	/**
	 * Returns a list of all children for this budget category, inluding deleted ones.
	 * @return
	 */
	public List<BudgetCategory> getAllChildren();
	
	/**
	 * Does this budget category represent an income category?
	 * @return
	 */
	public boolean isIncome();
	
	/**
	 * Sets the budgeted amount for the given time period.
	 * @param periodDate
	 * @param amount
	 */
	public void setAmount(Date periodDate, long amount) throws InvalidValueException;
	
	/**
	 * Sets the income flag on this budget category
	 * @param income
	 * @throws InvalidValueException
	 */
	public void setIncome(boolean income) throws InvalidValueException;
	
	/**
	 * Sets the parent for this budget category.
	 * @param parent
	 * @throws InvalidValueException
	 */
	public void setParent(BudgetCategory parent) throws InvalidValueException;
	
	/**
	 * Sets the Budget Period type. 
	 * @param periodType
	 */
	public void setPeriodType(BudgetCategoryType periodType) throws InvalidValueException;
	
	/**
	 * Returns a list of all dates for this budget period which have budget information
	 * set for them.  We basically look at the budget backing map, and return all keys for
	 * non-zero values. 
	 * @return
	 */
	public List<Date> getBudgetedDates();
}
