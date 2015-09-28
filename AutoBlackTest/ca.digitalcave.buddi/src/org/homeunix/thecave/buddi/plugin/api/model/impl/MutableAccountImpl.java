/*
 * Created on Aug 23, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccountType;

public class MutableAccountImpl extends MutableSourceImpl implements MutableAccount {

	public MutableAccountImpl(Account account) {
		super(account);
	}
//	
//	public MutableAccountImpl(String name, long startingBalance, MutableType type) {
//		super(new Account(type.getType().getModel(), name, startingBalance, type.getType()));
//	}

	public void setStartingBalance(long startingBalance) throws InvalidValueException {
		getAccount().setStartingBalance(startingBalance);
	}

	public void setAccountType(MutableAccountType type) throws InvalidValueException {
		getAccount().setAccountType(type.getType());
	}

	public Account getAccount() {
		return (Account) getSource();
	}

	public long getBalance() {
		return getAccount().getBalance();
	}

	public long getStartingBalance() {
		return getAccount().getStartingBalance();
	}

	public MutableAccountType getAccountType() {
		if (getAccount().getAccountType() != null)
			return new MutableAccountTypeImpl(getAccount().getAccountType());
		return null;
	}
	public Date getStartDate() {
		return getAccount().getStartDate();
	}
	public long getBalance(Date d) {
		return getAccount().getBalance(d);
	}
	@Override
	public String toString() {
		return getFullName();
	}
	
	public long getOverdraftCreditLimit() {
		return getAccount().getOverdraftCreditLimit();
	}
	
	public void setOverdraftCreditLimit(long overdraftCreditLimit) throws InvalidValueException {
		getAccount().setOverdraftCreditLimit(overdraftCreditLimit);
	}
	
	public void setStartDate(Date startDate) {
		getAccount().setStartDate(startDate);
	}
	
	public void setInterestRate(long interestRate) throws InvalidValueException {
		getAccount().setInterestRate(interestRate);
	}
	
	public long getInterestRate() {
		return getAccount().getInterestRate();
	}
}
