/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;

import org.homeunix.thecave.buddi.model.Account;

public interface ImmutableAccount extends ImmutableSource {
		
	/**
	 * Returns the wrapped object from the underlying data model.  By 
	 * accessing this method, you bypass all protection which the Buddi API
	 * gives you; it is not recommended to use this method unless you understand
	 * the risks associated with it. 
	 * @return
	 */
	public Account getAccount();
	
	/**
	 * Returns the Type object associated with the account.
	 * @return
	 */
	public ImmutableAccountType getAccountType();
	
	/**
	 * Returns the current balance of the account. 
	 * @return
	 */
	public long getBalance();
	
	/**
	 * Returns the balance as of the given date.
	 * @param d
	 * @return
	 */
	public long getBalance(Date d);
	
	/**
	 * Returns the earliest date assoicated with this source.  This is obtained 
	 * by looking at the associated transactions / period dates.
	 * @return
	 */
	public Date getStartDate();
	
	/**
	 * Returns the starting balance for the account.
	 * @return
	 */
	public long getStartingBalance();
	
	
	/**
	 * Returns the Overdraft Limit (for debit accounts) or Credit Limit (for credit accounts)
	 * @return
	 */
	public long getOverdraftCreditLimit();
	
	/**
	 * Returns the interest rate.  This is a three decimal-place value interpreted as a long; 
	 * for instance, the value "6123" would mean "6.123%".
	 * @return
	 */
	public long getInterestRate();
}
