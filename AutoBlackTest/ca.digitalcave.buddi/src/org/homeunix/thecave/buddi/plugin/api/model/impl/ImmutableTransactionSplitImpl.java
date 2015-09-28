/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransactionSplit;

public class ImmutableTransactionSplitImpl extends ImmutableTransactionImpl implements ImmutableTransactionSplit {
	
	public ImmutableTransactionSplitImpl(Transaction transaction) {
		super(transaction);
	}
	
	public TransactionSplit getTransactionSplit(){
		return (TransactionSplit) getRaw();
	}
	
	public ImmutableSource getSource(){
		if (getTransactionSplit().getSource() instanceof Account)
			return new MutableAccountImpl((Account) getTransaction().getFrom());
		if (getTransactionSplit().getSource() instanceof BudgetCategory)
			return new MutableBudgetCategoryImpl((BudgetCategory) getTransaction().getFrom());
		return null;
	}
	public long getAmount(){
		return getTransactionSplit().getAmount();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImmutableTransactionSplit)
			return getUid().equals(((ImmutableTransactionSplit) obj).getUid());
		return false;
	}
}
