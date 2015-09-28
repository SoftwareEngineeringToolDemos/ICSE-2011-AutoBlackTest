/*
 * Created on Aug 22, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;

public interface MutableDocument extends ImmutableDocument {

	/**
	 * Returns the account referenced by the given name.
	 * @param name
	 * @return
	 */
	public MutableAccount getAccount(String name);
	
	/**
	 * Returns a list of all immutable accounts in the model
	 * @return
	 */
	public List<MutableAccount> getMutableAccounts();
	
	/**
	 * Returns a list of all immutable budget categories in the model
	 * @return
	 */
	public List<MutableBudgetCategory> getMutableBudgetCategories();
	
	/**
	 * Returns the budget category referenced by the given full name.
	 * @param fullName
	 * @return
	 */
	public MutableBudgetCategory getBudgetCategory(String fullName);
	
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
	public List<MutableTransaction> getMutableTransactions();
	
	/**
	 * Returns a list of all immutable transactions in the model which are
	 * between startDate and endDate
	 * @return
	 */
	public List<MutableTransaction> getMutableTransactions(Date startDate, Date endDate);
	
	/**
	 * Returns a list of all immutable transactions in the model which are
	 * associatd with the given source
	 * @return
	 */
	public List<MutableTransaction> getMutableTransactions(MutableSource source);
	
	/**
	 * Returns a list of all immutable transactions in the model which are associated with
	 * source and between startDate and endDate
	 * @return
	 */
	public List<MutableTransaction> getMutableTransactions(MutableSource source, Date startDate, Date endDate);
	
	/**
	 * Returns the type referenced by the given name.
	 * @param name
	 * @return
	 */
	public MutableAccountType getAccountType(String name);
	
	/**
	 * Returns a list of all immutable types in the model
	 * @return
	 */
	public List<MutableAccountType> getMutableAccountTypes();
	
	
	
	
	/**
	 * Adds an account to the model
	 * @param account
	 */
	public void addAccount(MutableAccount account) throws ModelException;
	
	/**
	 * Adds a type to the model
	 * @param type
	 */
	public void addAccountType(MutableAccountType type) throws ModelException;
	
	/**
	 * Adds a budget category to the model
	 * @param budgetCategory
	 */
	public void addBudgetCategory(MutableBudgetCategory budgetCategory) throws ModelException;
	
	/**
	 * Adds a scheduled transaction to the model
	 * @param scheduledTransaction
	 */
	public void addScheduledTransaction(MutableScheduledTransaction scheduledTransaction) throws ModelException;
	
	/**
	 * Adds a transaction to the model
	 * @param transaction
	 */
	public void addTransaction(MutableTransaction transaction) throws ModelException;
	
	/**
	 * Removes the given account from the model
	 * @param account
	 */
	public void removeAccount(MutableAccount account) throws ModelException;
	
	/**
	 * Removes the given budget category from the model
	 * @param budgetCategory
	 */
	public void removeBudgetCategory(MutableBudgetCategory budgetCategory) throws ModelException;
	
	/**
	 * Removes the given scheduled transaction from the model
	 * @param scheduledTransaction
	 */
	public void removeScheduledTransaction(MutableScheduledTransaction scheduledTransaction) throws ModelException;
	
	/**
	 * Removes the given transaction from the model
	 * @param transaction
	 */
	public void removeTransaction(MutableTransaction transaction) throws ModelException;
	
	/**
	 * Removes the given type from the model
	 * @param type
	 */
	public void removeType(MutableAccountType type) throws ModelException;
}
