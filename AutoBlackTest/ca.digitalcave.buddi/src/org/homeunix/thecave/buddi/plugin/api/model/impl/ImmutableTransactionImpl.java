/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Split;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.model.impl.WrapperLists;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransactionSplit;

public class ImmutableTransactionImpl extends ImmutableModelObjectImpl implements ImmutableTransaction {
	
	public ImmutableTransactionImpl(Transaction transaction) {
		super(transaction);
	}
	
	public Transaction getTransaction(){
		return (Transaction) getRaw();
	}
	
	public boolean isClearedFrom() {
		return getTransaction().isClearedFrom();
	}
	public boolean isClearedTo() {
		return getTransaction().isClearedTo();
	}
	public Date getDate() {
		return getTransaction().getDate();
	}
	public String getDescription() {
		return getTransaction().getDescription();
	}
	public String getMemo() {
		return getTransaction().getMemo();
	}
	public String getNumber() {
		return getTransaction().getNumber();
	}
	public boolean isReconciledFrom() {
		return getTransaction().isReconciledFrom();
	}
	public boolean isReconciledTo() {
		return getTransaction().isReconciledTo();
	}
	public boolean isScheduled() {
		return getTransaction().isScheduled();
	}
	public List<ImmutableTransactionSplit> getImmutableFromSplits(){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableTransactionSplit, TransactionSplit>(getRaw().getDocument(), ((Transaction) getRaw()).getFromSplits());
	}
	public List<ImmutableTransactionSplit> getImmutableToSplits(){
		return new WrapperLists.ImmutableObjectWrapperList<ImmutableTransactionSplit, TransactionSplit>(getRaw().getDocument(), ((Transaction) getRaw()).getToSplits());
	}
	public ImmutableSource getFrom(){
		if (getTransaction().getFrom() instanceof Account)
			return new MutableAccountImpl((Account) getTransaction().getFrom());
		if (getTransaction().getFrom() instanceof BudgetCategory)
			return new MutableBudgetCategoryImpl((BudgetCategory) getTransaction().getFrom());
		if (getTransaction().getFrom() instanceof Split)
			return new ImmutableSplitImpl((Split) getTransaction().getFrom());
		return null;
	}
	public ImmutableSource getTo(){
		if (getTransaction().getTo() instanceof Account)
			return new MutableAccountImpl((Account) getTransaction().getTo());
		if (getTransaction().getTo() instanceof BudgetCategory)
			return new MutableBudgetCategoryImpl((BudgetCategory) getTransaction().getTo());
		if (getTransaction().getTo() instanceof Split)
			return new ImmutableSplitImpl((Split) getTransaction().getTo());
		return null;
	}
	public long getBalance(ImmutableSource source) {
		return getTransaction().getBalance(source.getUid());
	}
	public boolean isInflow(){
		return getTransaction().isInflow();
	}
	public long getAmount(){
		return getTransaction().getAmount();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImmutableTransaction)
			return getUid().equals(((ImmutableTransaction) obj).getUid());
		return false;
	}
	
	public boolean isDeleted() {
		return getTransaction().isDeleted();
	}
}
