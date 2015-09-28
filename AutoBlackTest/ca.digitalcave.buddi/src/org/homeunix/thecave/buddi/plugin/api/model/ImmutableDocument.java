/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.model.Document;

/**
 * The API version of DataModel.  This contains methods to access all other  
 * objects in the model.  This is the object passed to plugins which do not
 * require write access (such as reports and export plugins)
 * 
 * @author wyatt
 *
 */
public interface ImmutableDocument extends ImmutableModelObject {
	
	/**
	 * Returns the account referenced by the given name.
	 * @param name
	 * @return
	 */
	public ImmutableAccount getAccount(String name);
	
	/**
	 * Returns a list of all immutable accounts in the model
	 * @return
	 */
	public List<ImmutableAccount> getImmutableAccounts();
	
	/**
	 * Returns a list of all immutable budget categories in the model
	 * @return
	 */
	public List<ImmutableBudgetCategory> getImmutableBudgetCategories();
	
	/**
	 * Returns the budget category referenced by the given full name.
	 * @param fullName
	 * @return
	 */
	public ImmutableBudgetCategory getBudgetCategory(String fullName);
	
	/**
	 * Returns the wrapped object from the underlying data model.  By 
	 * accessing this method, you bypass all protection which the Buddi API
	 * gives you; it is not recommended to use this method unless you understand
	 * the risks associated with it. 
	 * @return
	 */
	public Document getModel();
	
	/**
	 * Returns a list of all immutable transactions in the model
	 * @return
	 */
	public List<ImmutableTransaction> getImmutableTransactions();
	
	/**
	 * Returns a list of all immutable transactions in the model which are
	 * between startDate and endDate
	 * @return
	 */
	public List<ImmutableTransaction> getImmutableTransactions(Date startDate, Date endDate);
	
	/**
	 * Returns a list of all immutable transactions in the model which are
	 * associatd with the given source
	 * @return
	 */
	public List<ImmutableTransaction> getImmutableTransactions(ImmutableSource source);
	
	/**
	 * Returns a list of all immutable transactions in the model which are associated with
	 * source and between startDate and endDate
	 * @return
	 */
	public List<ImmutableTransaction> getImmutableTransactions(ImmutableSource source, Date startDate, Date endDate);
	
	/**
	 * Returns the type referenced by the given name.
	 * @param name
	 * @return
	 */
	public ImmutableAccountType getAccountType(String name);
	
	/**
	 * Returns a list of all immutable types in the model
	 * @return
	 */
	public List<ImmutableAccountType> getImmutableAccountTypes();
	
	/**
	 * Returns an ImmutableBudgetCategoryType object, with the given name.  If the name is
	 * not a valid one, returns null.
	 * @param name
	 * @return
	 */
	public ImmutableBudgetCategoryType getBudgetCategoryType(BudgetCategoryTypes name);
	
	/**
	 * Returns an ImmutableBudgetCategoryType object, with the given name.  If the name is
	 * not a valid one, returns null.
	 * 
	 * Although this method gives the same results as the other one, given the same input,
	 * it is recommeneded to use the one which takes an enum if possible.  That will ensure
	 * that you do not make any mistakes with spelling, which may cause a null to be returned.
	 * @param name
	 * @return
	 */
	public ImmutableBudgetCategoryType getBudgetCategoryType(String name);
	
	/**
	 * Returns the net worth in the model as of the given date.  This is calculated by 
	 * summing the account balances for all accounts as of the given date.  If the given 
	 * date is null, return the current balance of the account, as of the last transaction 
	 * (regardless of date).
	 * @param date The date on which to calculate the net worth.  Set this to null to return
	 * the balance as of the last transaction. 
	 * @return
	 */
	public long getNetWorth(Date date);
}
