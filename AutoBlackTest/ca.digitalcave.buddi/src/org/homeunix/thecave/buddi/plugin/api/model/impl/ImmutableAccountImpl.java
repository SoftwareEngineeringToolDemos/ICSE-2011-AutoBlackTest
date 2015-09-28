/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccountType;

public class ImmutableAccountImpl extends ImmutableSourceImpl implements ImmutableAccount {
	
	public ImmutableAccountImpl(Account account) {
		super(account);
	}
	
	public long getStartingBalance() {
		return getAccount().getStartingBalance();
	}
	public long getBalance() {
		return getAccount().getBalance();
	}
	public ImmutableAccountType getAccountType() {
		return new MutableAccountTypeImpl(getAccount().getAccountType());
	}
	public Account getAccount(){
		return (Account) getRaw(); 
	}
	public Date getStartDate() {
		return getAccount().getStartDate();
	}
	
	@Override
	public String toString() {
		return getFullName();
	}
	
	public long getBalance(Date d) {
		return getAccount().getBalance(d);
	}
	
	public long getOverdraftCreditLimit() {
		return getAccount().getOverdraftCreditLimit();
	}
	
	public long getInterestRate() {
		return getAccount().getInterestRate();
	}
}
