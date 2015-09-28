/*
 * Created on Aug 22, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;

import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

public interface MutableAccount extends ImmutableAccount, MutableSource {
		
	/**
	 * Sets the type for this account
	 * @param accountType
	 */
	public void setAccountType(MutableAccountType accountType) throws InvalidValueException;
	
	/**
	 * Sets the starting balance for this account
	 * @param startingBalance
	 */
	public void setStartingBalance(long startingBalance) throws InvalidValueException;
	/**
	 * Returns the Type object associated with the account.
	 * @return
	 */
	public MutableAccountType getAccountType();
	
	/**
	 * Sets the Overdraft Limit (for debit accounts) or Credit Limit (for credit accounts)
	 * @param overdraftCreditLimit
	 */
	public void setOverdraftCreditLimit(long overdraftCreditLimit) throws InvalidValueException;
	
	/**
	 * Sets the starting date for the account.
	 * @param startDate
	 */
	public void setStartDate(Date startDate);
	
	/**
	 * Sets the interest rate.  This is a three decimal-place value interpreted as a long; 
	 * for instance, the value "6123" would mean "6.123%".  This value cannot be negative,
	 * and should not be greater than 100% (100000 long) (we currently don't check that
	 * the value is less than 100%, but we may do this in the future, so to be safe, don't rely
	 * on being able to do this).
	 * @param interestRate
	 */
	public void setInterestRate(long interestRate) throws InvalidValueException;
}
