/*
 * Created on Aug 23, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.impl.WrapperLists;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccountType;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableBudgetCategoryType;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccountType;
import org.homeunix.thecave.buddi.plugin.api.model.MutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.MutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.MutableScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.MutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.MutableTransaction;

public class MutableDocumentImpl extends MutableModelObjectImpl implements MutableDocument, ImmutableDocument {

	private final Document model;
	
	public MutableDocumentImpl(Document model) {
		super(model);
		
		this.model = model;
	}

	public void addAccount(MutableAccount account) throws ModelException{
		getModel().addAccount(account.getAccount());
	}

	public void addBudgetCategory(MutableBudgetCategory budgetCategory) throws ModelException{
		getModel().addBudgetCategory(budgetCategory.getBudgetCategory());
	}

	public void addScheduledTransaction(MutableScheduledTransaction scheduledTransaction) throws ModelException{
		getModel().addScheduledTransaction(scheduledTransaction.getScheduledTransaction());		
	}

	public void addTransaction(MutableTransaction transaction) throws ModelException{
		getModel().addTransaction(transaction.getTransaction());
	}

	public void addAccountType(MutableAccountType type) throws ModelException{
		getModel().addAccountType(type.getType());
	}

	public void removeAccount(MutableAccount account) throws ModelException{
		getModel().removeAccount(account.getAccount());
	}

	public void removeBudgetCategory(MutableBudgetCategory budgetCategory) throws ModelException{
		getModel().removeBudgetCategory(budgetCategory.getBudgetCategory());
	}

	public void removeScheduledTransaction(MutableScheduledTransaction scheduledTransaction) throws ModelException{
		getModel().removeScheduledTransaction(scheduledTransaction.getScheduledTransaction());		
	}

	public void removeTransaction(MutableTransaction transaction) throws ModelException{
		getModel().removeTransaction(transaction.getTransaction());
	}

	public void removeType(MutableAccountType type) throws ModelException{
		getModel().removeAccountType(type.getType());
	}
	
	
	
	public MutableAccount getAccount(String name) {
		if (getModel().getAccount(name) == null)
			return null;
		return new MutableAccountImpl(getModel().getAccount(name));
	}
	
	public List<MutableAccount> getMutableAccounts(){
		return new WrapperLists.ImmutableObjectWrapperList<MutableAccount, Account>(getModel(), getModel().getAccounts());
	}
	
	public List<MutableBudgetCategory> getMutableBudgetCategories(){
		return new WrapperLists.ImmutableObjectWrapperList<MutableBudgetCategory, BudgetCategory>(getModel(), getModel().getBudgetCategories());
	}
	
	public MutableBudgetCategory getBudgetCategory(String fullName) {
		BudgetCategory bc = getModel().getBudgetCategory(fullName);
		if (bc == null)
			return null;
		return new MutableBudgetCategoryImpl(bc);
	}
	
	public Document getModel(){
		return model;
	}
	
	public List<MutableTransaction> getMutableTransactions(){
		return new WrapperLists.ImmutableObjectWrapperList<MutableTransaction, Transaction>(getModel(), getModel().getTransactions());
	}
	
	public List<MutableTransaction> getMutableTransactions(Date startDate, Date endDate){
		return new WrapperLists.ImmutableObjectWrapperList<MutableTransaction, Transaction>(getModel(), getModel().getTransactions(startDate, endDate));
	}
	
	public List<MutableTransaction> getMutableTransactions(MutableSource source){
		return new WrapperLists.ImmutableObjectWrapperList<MutableTransaction, Transaction>(getModel(), getModel().getTransactions((Source) source.getRaw()));
	}
	
	public List<MutableTransaction> getMutableTransactions(MutableSource source, Date startDate, Date endDate){
		return new WrapperLists.ImmutableObjectWrapperList<MutableTransaction, Transaction>(getModel(), getModel().getTransactions((Source) source.getRaw(), startDate, endDate));
	}
	
	public MutableAccountType getAccountType(String name) {
		if (getModel().getAccountType(name) == null)
			return null;
		return new MutableAccountTypeImpl(getModel().getAccountType(name));
	}
	
	public List<MutableAccountType> getMutableAccountTypes(){
		return new WrapperLists.ImmutableObjectWrapperList<MutableAccountType, AccountType>(getModel(), getModel().getAccountTypes());		
	}
	
	
	
	
	public List<ImmutableAccount> getImmutableAccounts(){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableAccount, Account>(getModel(), getModel().getAccounts());
	}
	
	public List<ImmutableBudgetCategory> getImmutableBudgetCategories(){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableBudgetCategory, BudgetCategory>(getModel(), getModel().getBudgetCategories());
	}
	
	public List<ImmutableTransaction> getImmutableTransactions(){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableTransaction, Transaction>(getModel(), getModel().getTransactions());
	}
	
	public List<ImmutableTransaction> getImmutableTransactions(Date startDate, Date endDate){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableTransaction, Transaction>(getModel(), getModel().getTransactions(startDate, endDate));
	}
	
	public List<ImmutableTransaction> getImmutableTransactions(ImmutableSource source){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableTransaction, Transaction>(getModel(), getModel().getTransactions((Source) source.getRaw()));
	}
	
	public List<ImmutableTransaction> getImmutableTransactions(ImmutableSource source, Date startDate, Date endDate){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableTransaction, Transaction>(getModel(), getModel().getTransactions((Source) source.getRaw(), startDate, endDate));
	}
	
	public List<ImmutableAccountType> getImmutableAccountTypes(){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableAccountType, AccountType>(getModel(), getModel().getAccountTypes());		
	}
	
	public ImmutableBudgetCategoryType getBudgetCategoryType(BudgetCategoryTypes name){
		return getBudgetCategoryType(name.toString());
	}
	
	public ImmutableBudgetCategoryType getBudgetCategoryType(String name) {
		BudgetCategoryType type = ModelFactory.getBudgetCategoryType(name);
		if (type == null)
			return null;
		return new ImmutableBudgetCategoryTypeImpl(type);
	}
	public long getNetWorth(Date date) {
		return getModel().getNetWorth(date);
	}
}
