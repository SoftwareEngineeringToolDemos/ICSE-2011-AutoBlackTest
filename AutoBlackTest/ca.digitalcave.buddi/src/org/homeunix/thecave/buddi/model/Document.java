/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;

import ca.digitalcave.moss.application.document.StandardDocument;
import ca.digitalcave.moss.application.document.exception.DocumentSaveException;

public interface Document extends ModelObject, StandardDocument {

	public static final int RESET_PASSWORD = 1;  //Should we change the current password?
	public static final int CHANGE_PASSWORD = 2; //Should we prompt for a password?
	
	public void addAccount(Account account) throws ModelException;
	public void addAccountType(AccountType type) throws ModelException;
	public void addBudgetCategory(BudgetCategory budgetCategory) throws ModelException;
	public void addScheduledTransaction(ScheduledTransaction scheduledTransaction) throws ModelException;
	public void addTransaction(Transaction transaction) throws ModelException;
	public String doSanityChecks();
	public Account getAccount(String name);
	public List<Account> getAccounts();
	public AccountType getAccountType(String name);
	public List<AccountType> getAccountTypes();
	public List<BudgetCategory> getBudgetCategories();
	public BudgetCategory getBudgetCategory(String fullName);
	public long getNetWorth(Date date);
	public ModelObject getObjectByUid(String uid);
	public List<ScheduledTransaction> getScheduledTransactions();
	public List<Source> getSources();
	public List<Transaction> getTransactions();
	public List<Transaction> getTransactions(Date startDate, Date endDate);
	public List<Transaction> getTransactions(Source source);
	public List<Transaction> getTransactions(Source source, Date startDate, Date endDate);
	public void refreshUidMap() throws ModelException;
	public void removeAccount(Account account) throws ModelException;
	public void removeAccountType(AccountType type) throws ModelException;
	public void removeBudgetCategory(BudgetCategory budgetCategory) throws ModelException;
	public void removeScheduledTransaction(ScheduledTransaction scheduledTransaction) throws ModelException;
	public void removeTransaction(Transaction transaction) throws ModelException;
	public void save() throws DocumentSaveException;
	public void saveAs(File file) throws DocumentSaveException;
	public void saveToStream(OutputStream os) throws DocumentSaveException;
	public void setFlag(int flag, boolean set);
	public void updateAllBalances();
	public void updateScheduledTransactions();
	public Document clone() throws CloneNotSupportedException;
}
