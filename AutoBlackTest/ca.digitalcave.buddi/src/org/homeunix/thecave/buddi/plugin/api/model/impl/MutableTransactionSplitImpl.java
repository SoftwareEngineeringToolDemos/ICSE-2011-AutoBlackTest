/*
 * Created on Aug 23, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.MutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.MutableTransactionSplit;

public class MutableTransactionSplitImpl extends MutableModelObjectImpl implements MutableTransactionSplit {

	public MutableTransactionSplitImpl(TransactionSplit transactionSplit) {
		super(transactionSplit);
	}
	
	public TransactionSplit getTransactionSplit(){
		return (TransactionSplit) getRaw();
	}

	public void setAmount(long amount) {
		getTransactionSplit().setAmount(amount);
	}

	public void setSource(MutableSource source) throws InvalidValueException{
		if (source == null)
			getTransactionSplit().setSource(null);
		else
			getTransactionSplit().setSource(source.getSource());
	}
	
	public ImmutableSource getSource(){
		if (getTransactionSplit().getSource() instanceof Account)
			return new MutableAccountImpl((Account) getTransactionSplit().getSource());
		if (getTransactionSplit().getSource() instanceof BudgetCategory)
			return new MutableBudgetCategoryImpl((BudgetCategory) getTransactionSplit().getSource());
		return null;
	}
	public long getAmount(){
		return getTransactionSplit().getAmount();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MutableTransactionSplit)
			return getUid().equals(((MutableTransactionSplit) obj).getUid());
		return false;
	}
}
