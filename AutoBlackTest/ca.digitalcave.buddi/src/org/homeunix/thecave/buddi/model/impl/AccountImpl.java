/*
 * Created on Jul 29, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ModelObject;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;

/**
 * Default implementation of an Account.  You should not create this object directly; 
 * instead, please use the ModelFactory to create it, as this will ensure that all
 * required fields are correctly set.
 * @author wyatt
 *
 */
public class AccountImpl extends SourceImpl implements Account {
	private long startingBalance;
	private long balance;
	private long overdraftCreditLimit;
	private long interestRate;
	private Day startDate;
	private AccountType type;

	public long getStartingBalance() {
		return startingBalance;
	}
	public void setStartingBalance(long startingBalance) {
		if (this.startingBalance != startingBalance)
			setChanged();
		this.startingBalance = startingBalance;
	}
	public Date getStartDate() {
		List<Transaction> ts = getDocument().getTransactions(this);
		if (ts.size() > 0){
			if (startDate == null || startDate.after(ts.get(0).getDate()))
				return ts.get(0).getDate();
			else 
				return startDate;
		}
		if (startDate == null)
			return new Date();
		return startDate;
	}
	public void setStartDate(Date startDate) {
		if (startDate != null)
			this.startDate = new Day(startDate);
		else
			this.startDate = null;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		if (this.balance != balance)
			setChanged();
		this.balance = balance;
	}
	public AccountType getAccountType() {
		return type;
	}
	public void setAccountType(AccountType type) {
		this.type = type;
		setChanged();
	}
	@Override
	public void setName(String name) throws InvalidValueException {
		//		if (getDocument() != null){
		//		for (Account a : ((Document) getDocument()).getAccounts()) {
		//		if (a.getName().equals(name) && !a.equals(this))
		//		throw new InvalidValueException("The account name must be unique");
		//		}
		//		}
		super.setName(name);
	}
	public void updateBalance(){		
		if (getDocument() == null)			
			return;
		long balance = this.getStartingBalance();

		List<Transaction> transactions = getDocument().getTransactions(this);

		for (Transaction transaction : transactions) {			
			try {
				if (!transaction.isDeleted()){					
					//We are moving money *to* this account					
					if (transaction.getTo().equals(this)){
						balance += transaction.getAmount();						
						transaction.setBalance(this.getUid(), balance);
					} 					
					//We are moving money *from* this account					
					else if (transaction.getFrom().equals(this)){
						balance -= transaction.getAmount();						
						transaction.setBalance(this.getUid(), balance);					
					}					
					//We are moving money *to* this account					
					for (TransactionSplit split : transaction.getToSplits()) {
						if(split.getSource().equals(this))							
							balance += split.getAmount();					
					}					
					//We are moving money *from* this account					
					for (TransactionSplit split : transaction.getFromSplits()) {
						if(split.getSource().equals(this))							
							balance -= split.getAmount();					
					}				
				}			
			}			
			catch (InvalidValueException ive){				
				Logger.getLogger(AccountImpl.class.getName()).log(Level.WARNING, "Incorrect value", ive);			
			}		
		}
		setBalance(balance);	
	}

	public String getFullName() {
		return this.getName() + " (" + getAccountType().getName() + ")";
	}
	public long getBalance(Date d) {
		if (getDocument() == null)
			return 0; //Document not set; not valid.  Possibly throw exception?
		if (d.before(getStartDate()))
			//This used to be defined to return starting balance... I think that it makes more
			// sense to return 0, though - if the account has not been defined, there is by
			// definition, no balance.
			//Update (Dec 5 2010): I reverted back to using getStartingBalance, as after
			// re-thinking I think this makes more sense.
			return getStartingBalance();
		//			return 0;
		List<Transaction> ts = getDocument().getTransactions(this, getStartDate(), d);
		if (ts.size() > 0){
			Transaction t = ts.get(ts.size() - 1);
			if (t.getFrom().equals(this) || t.getTo().equals(this))
				return t.getBalance(this.getUid());
		}
		else {
			//If there are no transactions, just return the starting balance.
			return getStartingBalance();
		}

		//		Logger.getLogger().error("AccountImpl.getBalance(Date) - Something is wrong... we should not be here.");
		return 0;
	}
	@Override
	public int compareTo(ModelObject arg0) {
		if (arg0 instanceof AccountImpl){
			AccountImpl a = (AccountImpl) arg0;
			if (this.getAccountType().isCredit() != a.getAccountType().isCredit()){
				if (this.getAccountType().isCredit())
					return 1;
				return -1;
			}
			return this.getName().compareTo(a.getName());
		}
		return super.compareTo(arg0);
	}

	public long getOverdraftCreditLimit() {
		return overdraftCreditLimit;
	}

	public void setOverdraftCreditLimit(long overdraftCreditLimit) throws InvalidValueException {
		if (overdraftCreditLimit < 0)
			throw new InvalidValueException("Overdraft limit must be positive");
		if (this.overdraftCreditLimit != overdraftCreditLimit)
			setChanged();
		this.overdraftCreditLimit = overdraftCreditLimit;
	}

	Account clone(Map<ModelObject, ModelObject> originalToCloneMap) throws CloneNotSupportedException {

		if (originalToCloneMap.get(this) != null)
			return (Account) originalToCloneMap.get(this);

		AccountImpl a = new AccountImpl();

		a.document = (Document) originalToCloneMap.get(document);
		a.type = (AccountType) originalToCloneMap.get(getAccountType());
		a.balance = balance;
		a.deleted = isDeleted();
		if (modifiedTime != null)
			a.modifiedTime = new Time(modifiedTime);
		a.name = name;
		a.notes = notes;
		a.overdraftCreditLimit = overdraftCreditLimit;
		a.interestRate = interestRate;
		if (startDate != null)
			a.startDate = new Day(startDate);
		a.startingBalance = startingBalance;

		originalToCloneMap.put(this, a);

		return a;
	}

	public long getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(long interestRate) throws InvalidValueException {
		if (interestRate < 0) //Should only affect API setters, as the decimal input field does not allow negatives.
			throw new InvalidValueException("Interest rate must be positive");
		//TODO Should we check for this?  Perhaps at a later date; for now, since this value is not used
		// for anything other than 'FYI', it's probably not needed.
		//		if (interestRate > 100000)
		//			throw new InvalidValueException("Interest rate cannot be greater than 100%");
		if (this.interestRate != interestRate)
			setChanged();
		this.interestRate = interestRate;
	}
}
