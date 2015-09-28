/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.io.File;
import java.util.Date;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.model.impl.ImmutableSplitImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableAccountImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableAccountTypeImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableBudgetCategoryImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableDocumentImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableScheduledTransactionImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableTransactionImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableTransactionSplitImpl;
import org.homeunix.thecave.buddi.util.OperationCancelledException;

import ca.digitalcave.moss.application.document.exception.DocumentLoadException;

public class MutableModelFactory {
	/**
	 * Creates a new MutableModel, with the default budget categories, types, etc.
	 * @return
	 * @throws ModelException
	 */
	public static MutableDocument createDocument() throws ModelException {
		return new MutableDocumentImpl(ModelFactory.createDocument());
	}
	
	/**
	 * Loads an existing MutableModel from the specified file.
	 * @param file
	 * @return
	 * @throws DocumentLoadException
	 * @throws OperationCancelledException
	 */
	public static MutableDocument createDocument(File file) throws DocumentLoadException, OperationCancelledException {
		return new MutableDocumentImpl(ModelFactory.createDocument(file));
	}
	
	/**
	 * Creates a new MutableAccount with the given details.
	 * @param name
	 * @param startingBalance
	 * @param type
	 * @return
	 * @throws ModelException
	 */
	public static MutableAccount createMutableAccount(String name, long startingBalance, MutableAccountType type)  throws ModelException {
		Account a = ModelFactory.createAccount(name, type.getType());
		a.setStartingBalance(startingBalance);
		
		return new MutableAccountImpl(a);
	}
	
	/**
	 * Creates a new Split object.  Split objects are used for exactly one purpose: to 
	 * signal that instead of having a single source for the to / from of a transaction,
	 * we instead will use the toSplits / fromSplits.  Thus, there is no need to set any
	 * fields in this Split object, and thus we will just use an immutable one.
	 * 
	 * In fact, since there is no necessary information contained within a split object 
	 * other than the fact that it is of type Split, the ModelFactory will reuse a
	 * single Split object each time you call the method.
	 * @return
	 * @throws ModelException
	 */
	public static ImmutableSplit createImmutableSplit() throws ModelException {
		return new ImmutableSplitImpl(ModelFactory.createSplit());
	}
	
	/**
	 * Creates a new MutableAccountType with the given details.
	 * @param name
	 * @param credit
	 * @return
	 * @throws ModelException
	 */
	public static MutableAccountType createMutableAccountType(String name, boolean credit) throws ModelException {
		AccountType t = ModelFactory.createAccountType(name, credit);
		
		return new MutableAccountTypeImpl(t);
	}
	
	/**
	 * Creates a new MutableBudgetCategory with the given details.
	 * @param name
	 * @param periodType
	 * @param isIncome
	 * @return
	 * @throws ModelException
	 */
	public static MutableBudgetCategory createMutableBudgetCategory(String name, ImmutableBudgetCategoryType periodType, boolean isIncome) throws ModelException {
		BudgetCategory bc = ModelFactory.createBudgetCategory(name, periodType.getBudgetCategoryType(), isIncome);
		
		return new MutableBudgetCategoryImpl(bc);
	}
	
	/**
	 * Creates a new MutableScheduledTransaction
	 * @return
	 * @throws ModelException
	 */
	public static MutableScheduledTransaction createMutableScheduledTransaction(String name, String message, Date startDate, Date endDate, String frequencyType, int scheduleDay, int scheduleWeek, int scheduleMonth, Date date, String description, long amount, MutableSource from, MutableSource to) throws ModelException{
		ScheduledTransaction st = ModelFactory.createScheduledTransaction(name, message, startDate, endDate, frequencyType, scheduleDay, scheduleWeek, scheduleMonth, description, amount, from.getSource(), to.getSource());
		
		return new MutableScheduledTransactionImpl(st);
	}
	
	/**
	 * Creates a new MutableTransaction with the given details.
	 * @param date
	 * @param description
	 * @param amount
	 * @param from
	 * @param to
	 * @return
	 * @throws ModelException
	 */
	public static MutableTransaction createMutableTransaction(Date date, String description, long amount, MutableSource from, MutableSource to) throws ModelException {
		Transaction t = ModelFactory.createTransaction(date, description, amount, from.getSource(), to.getSource());
		
		return new MutableTransactionImpl(t);
	}
	
	public static MutableTransactionSplit createMutableTransactionSplit(MutableSource source, long amount) throws ModelException {
		TransactionSplit t = ModelFactory.createTransactionSplit(source.getSource(), amount);
		return new MutableTransactionSplitImpl(t);
	}
}
