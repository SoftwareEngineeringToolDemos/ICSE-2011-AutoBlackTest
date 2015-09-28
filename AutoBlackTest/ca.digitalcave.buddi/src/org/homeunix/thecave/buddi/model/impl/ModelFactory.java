/*
 * Created on Aug 28, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.i18n.keys.BudgetExpenseDefaultKeys;
import org.homeunix.thecave.buddi.i18n.keys.BudgetIncomeDefaultKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.TypeCreditDefaultKeys;
import org.homeunix.thecave.buddi.i18n.keys.TypeDebitDefaultKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.Split;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.BuddiCryptoFactory;
import org.homeunix.thecave.buddi.util.OperationCancelledException;
import org.homeunix.thecave.buddi.view.dialogs.BuddiPasswordDialog;

import ca.digitalcave.moss.application.document.exception.DocumentLoadException;
import ca.digitalcave.moss.common.DateUtil;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.crypto.CipherException;
import ca.digitalcave.moss.crypto.IncorrectDocumentFormatException;
import ca.digitalcave.moss.crypto.IncorrectPasswordException;

/**
 * The factory for all model objects.  It is highly recommended to use this
 * class instead of the constructors, for all model objects.
 * 
 * @author wyatt
 *
 */
public class ModelFactory {

	public static Map<String, BudgetCategoryType> budgetPeriodTypes;

	/**
	 * Returns the budget category type of the given type, or null if it 
	 * does not exist.
	 * @param name
	 * @return
	 */
	public static BudgetCategoryType getBudgetCategoryType(BudgetCategoryTypes type){
		return getBudgetCategoryType(type.toString());
	}

	/**
	 * Returns the budget category type of the given name, or null if it 
	 * does not exist.
	 * @param name
	 * @return
	 */
	public static BudgetCategoryType getBudgetCategoryType(String name){
		if (budgetPeriodTypes == null){
			budgetPeriodTypes = new HashMap<String, BudgetCategoryType>();
			budgetPeriodTypes.put(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH.toString(), new BudgetCategoryTypeMonthly());
			budgetPeriodTypes.put(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_WEEK.toString(), new BudgetCategoryTypeWeekly());
			budgetPeriodTypes.put(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_SEMI_MONTH.toString(), new BudgetCategoryTypeSemiMonthly());
			budgetPeriodTypes.put(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_QUARTER.toString(), new BudgetCategoryTypeQuarterly());
			budgetPeriodTypes.put(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_SEMI_YEAR.toString(), new BudgetCategoryTypeSemiYearly());
			budgetPeriodTypes.put(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_YEAR.toString(), new BudgetCategoryTypeYearly());
		}

		return budgetPeriodTypes.get(name);
	}

	/**
	 * Creates a new Account with the given values
	 * @param name
	 * @param type
	 * @return
	 * @throws InvalidValueException
	 */
	public static Account createAccount(String name, AccountType type) throws InvalidValueException {
		Account a = new AccountImpl();

		a.setName(name);
		a.setAccountType(type);

		return a;
	}
	
	private static Split split = new SplitImpl();
	public static Split createSplit() throws InvalidValueException {
		return split;
	}
	
	public static TransactionSplit createTransactionSplit(Source source, long amount) throws InvalidValueException {
		TransactionSplit t = new TransactionSplitImpl();
		
		t.setSource(source);
		t.setAmount(amount);
		
		return t;
	}

	/**
	 * Creates a new AccountType with the given values
	 * @param name
	 * @param credit
	 * @return
	 * @throws InvalidValueException
	 */
	public static AccountType createAccountType(String name, boolean credit) throws InvalidValueException {
		AccountType at = new AccountTypeImpl();

		at.setName(name);
		at.setCredit(credit);

		return at;
	}

	/**
	 * Creates a new BudgetCategory with the given values
	 * @param name
	 * @param type
	 * @param income
	 * @return
	 * @throws InvalidValueException
	 */
	public static BudgetCategory createBudgetCategory(String name, BudgetCategoryType type, boolean income) throws InvalidValueException {
		BudgetCategory bc = new BudgetCategoryImpl();

		bc.setName(name);
		bc.setPeriodType(type);
		bc.setIncome(income);

		return bc;
	}

	/**
	 * Creates a new data model, with some default types and categories.
	 */
	public static Document createDocument() throws ModelException {
		Document document;
		if (getAutoSaveLocation(null).exists() && getAutoSaveLocation(null).canRead()){
			Logger.getLogger(ModelFactory.class.getName()).info("Autosave file found; prompting user if we should use it or not");

			String[] options = new String[2];
			options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_YES);
			options[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_NO);

			if (JOptionPane.showOptionDialog(
					null, 
					TextFormatter.getTranslation(BuddiKeys.MESSAGE_AUTOSAVE_FILE_FOUND),
					TextFormatter.getTranslation(BuddiKeys.MESSAGE_AUTOSAVE_FILE_FOUND_TITLE),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]
			) == 0) {  //The index of the Yes button.
				try {
					document = createDocument(getAutoSaveLocation(null));
					document.setFile(null);
					document.setChanged();
					Logger.getLogger(ModelFactory.class.getName()).info("User decided to load AutoSave file");
					return document;
				}
				catch (DocumentLoadException dle){
					Logger.getLogger(ModelFactory.class.getName()).warning("Error opening auto save file.  Continuing on to create normal file.");
				}
				catch (OperationCancelledException oce){
					Logger.getLogger(ModelFactory.class.getName()).finest("User cancelled opening auto save file.  Continuing on to create normal file.");
				}
			}
			else {
				Logger.getLogger(ModelFactory.class.getName()).info("User decided not to load AutoSave file.  It will be removed the next time this file is saved.");
			}
		}

		document = new DocumentImpl();
		document.setFile(null); //A null dataFile will prompt for location on first save.

		for (BudgetExpenseDefaultKeys s : BudgetExpenseDefaultKeys.values()){
			BudgetCategory bc = ModelFactory.createBudgetCategory(s.toString(), new BudgetCategoryTypeMonthly(), false); 
			document.addBudgetCategory(bc);
		}
		for (BudgetIncomeDefaultKeys s : BudgetIncomeDefaultKeys.values()){
			document.addBudgetCategory(ModelFactory.createBudgetCategory(s.toString(), new BudgetCategoryTypeMonthly(), true));
		}

		for (TypeDebitDefaultKeys s : TypeDebitDefaultKeys.values()){
			document.addAccountType(ModelFactory.createAccountType(s.toString(), false));
		}

		for (TypeCreditDefaultKeys s : TypeCreditDefaultKeys.values()){
			document.addAccountType(ModelFactory.createAccountType(s.toString(), true));
		}	

		//Refresh the UID Map...
		document.refreshUidMap();
		
		//Do some sanity checks.  If this returns anything, display it.
		String errors = document.doSanityChecks();
		if (errors != null)
			JOptionPane.showMessageDialog(null, errors);

		//Update all balances...
		document.updateAllBalances();

		//Sort lists...
		Collections.sort(document.getAccounts());
		Collections.sort(document.getAccountTypes());
		Collections.sort(document.getBudgetCategories());
		Collections.sort(document.getTransactions());
		Collections.sort(document.getScheduledTransactions());

		//Allow changes to start firing
		document.finishBatchChange();

		//Fire a change event
		document.setChanged();

		//Return to calling code... the model is correctly loaded.
		return document;
	}

	/**
	 * Attempts to load a data model from file.  Works with Buddi 3 format.  To load a
	 * legacy format, use ModelConverter to get a Bean object, and call the constructor which
	 * takes a DataModelBean.
	 * @param file File to load
	 * @throws DocumentLoadException
	 */
	public static Document createDocument(File file) throws DocumentLoadException, OperationCancelledException {
		DocumentImpl document;

		//Which file to actually load from.  Initially set to the given file, but
		// this may be changed if we find an autosave document and the user wants 
		// to load it.
		File fileToLoad = file;

		if (file == null)
			throw new DocumentLoadException("Error loading model: specfied file is null.");

		if (!file.exists())
			throw new DocumentLoadException("File " + file + " does not exist.");

		if (!file.canRead())
			throw new DocumentLoadException("File " + file + " cannot be opened for reading.");

		if (getAutoSaveLocation(file).exists() && getAutoSaveLocation(file).canRead()){
			Logger.getLogger(ModelFactory.class.getName()).info("Autosave file found; prompting user if we should use it or not");

			String[] options = new String[2];
			options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_YES);
			options[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_NO);

			if (JOptionPane.showOptionDialog(
					null, 
					TextFormatter.getTranslation(BuddiKeys.MESSAGE_AUTOSAVE_FILE_FOUND),
					TextFormatter.getTranslation(BuddiKeys.MESSAGE_AUTOSAVE_FILE_FOUND_TITLE),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]
			) == 0) {  //The index of the Yes button.
				fileToLoad = getAutoSaveLocation(file);
				Logger.getLogger(ModelFactory.class.getName()).info("User decided to load AutoSave file");
			}
			else {
				Logger.getLogger(ModelFactory.class.getName()).info("User decided not to load AutoSave file.  It will be removed the next time this file is saved.");
			}
		}

		Logger.getLogger(ModelFactory.class.getName()).finest("Trying to load file " + fileToLoad);

		try {
			InputStream is;
			BuddiCryptoFactory factory = new BuddiCryptoFactory();
			char[] password = null;

			//Loop until the user gets the password correct, hits cancel, 
			// or some other error occurs.
			while (true) {
				try {
					is = factory.getDecryptedStream(new FileInputStream(fileToLoad), password);

					//Attempt to decode the XML within the (now hopefully un-encrypted) data file. 
					XMLDecoder decoder = new XMLDecoder(is);
					Object o = decoder.readObject();
					if (o instanceof DocumentImpl){
						document = (DocumentImpl) o;
					}
					else {
						throw new IncorrectDocumentFormatException("Could not find a DataModelBean object in the data file!");
					}
					is.close();

					//Refresh the UID Map...
					document.refreshUidMap();

					//This will let us know where to save the file to.  Even if we found
					// an autosave file, we want to save the file to the new location, 
					// so we don't use the fileToLoad variable.
					document.setFile(file);

					//Update all balances...
					document.updateAllBalances();

					//Store the password
					document.setPassword(password);

					//Check for scheduled transactions
					document.updateScheduledTransactions();




					//The old data format (before 2.9.11.0) used a string representation of a date
					// object as the key to the Budget Category amount map.  Unfortunately, this
					// meant that in different time zones, you would have different start dates
					// for budget periods!
					//We fix this in 2.9.11.0.  For backwards compatibility (to make sure that 
					// you can still load all the old format of date), we have to convert them
					// here at file load.
					//We should be able to remove this early in the 3.0 branch (or possible even 
					// at 3.0.0.0 itself, depending on how long it stays in Dev branch).
					for (BudgetCategory bc : document.getBudgetCategories()) {
						boolean changed = false;
						Map<String, Long> newAmountMap = new HashMap<String, Long>();

						//Iterate through all budget periods, and check the 
						// 
						for (String key : ((BudgetCategoryImpl) bc).getAmounts().keySet()) {
							String[] splitKey = key.split(":");
							if (splitKey.length == 2){
								changed = true;
								Date dateKey = new Date(Long.parseLong(splitKey[1]));
								Logger.getLogger(ModelFactory.class.getName()).finest("dateKey: " + dateKey);
								long amount = ((BudgetCategoryImpl) bc).getAmounts().get(key);
								String newDateKey = 
									bc.getBudgetPeriodType().getName() 
									+ ":" + DateUtil.getYear(dateKey)
									+ ":" + DateUtil.getMonth(dateKey)
									+ ":" + DateUtil.getDay(dateKey);
								newAmountMap.put(newDateKey, amount);
							}
							else if (splitKey.length == 4){
								newAmountMap.put(key, ((BudgetCategoryImpl) bc).getAmounts().get(key));
							}
						}

						if (changed){
							Logger.getLogger(ModelFactory.class.getName()).warning("Your data file has been updated to address bug #1811038.  Included inline are the details of the changes:\n\n"
									+ "New Map:\n" + newAmountMap + "\n\n"
									+ "Old Map:\n" + ((BudgetCategoryImpl) bc).getAmounts());

							((BudgetCategoryImpl) bc).setAmounts(newAmountMap);
						}
					}



					//As a precaution for the bug fix above, we are also changing our internal 
					// representation of Date objects for Transactions to Strings.  This is done
					// automatically when a date is read; to force this, we read every date here.
					for (Transaction t : document.getTransactions()) {
						t.getDate();
					}



					//Sort lists...
					Collections.sort(document.getAccounts());
					Collections.sort(document.getAccountTypes());
					Collections.sort(document.getBudgetCategories());
					Collections.sort(document.getTransactions());
					Collections.sort(document.getScheduledTransactions());

					//Allow changes to start firing
					document.finishBatchChange();

					//Fire a change event
					document.setChanged();
					document.resetChanged();

					//Return to calling code... the model is correctly loaded.
					return document;
				}
				catch (IncorrectPasswordException ipe){
					//The password was not correct.  Prompt for a new one.
					BuddiPasswordDialog passwordDialog = new BuddiPasswordDialog();
					password = passwordDialog.askForPassword(false, true);

					//User hit cancel.  Cancel loading, and pass control to calling code. 
					// Calling code will possibly prompt for a new file.
					if (password == null)
						throw new OperationCancelledException("User cancelled file load.");
				}
				catch (IncorrectDocumentFormatException ife){
					//The document we are trying to load does not have the proper header.
					// This is not a valid Buddi3 data file.
					throw new DocumentLoadException("Incorrect document format", ife);
				}
				catch (ModelException me){
					throw new DocumentLoadException("Model exception", me);
				}
			}
		}
		catch (CipherException ce){
			//If we get here, something is very wrong.  Perhaps the platform does not support
			// the encryption we have chosen, or something else is wrong.
			throw new DocumentLoadException(ce);
		}
		catch (IOException ioe){
			//If we get here, chances are good that the file is not valid.
			throw new DocumentLoadException(ioe);
		}
	}



	/**
	 * Creates a new ScheduledTransaction with the given values
	 * @return
	 * @throws InvalidValueException
	 */
	public static ScheduledTransaction createScheduledTransaction(String name, String message, Date startDate, Date endDate, String frequencyType, int scheduleDay, int scheduleWeek, int scheduleMonth, String description, long amount, Source from, Source to) throws InvalidValueException {

		ScheduledTransaction st = new ScheduledTransactionImpl();

		st.setScheduleName(name);
		st.setMessage(message);
		st.setStartDate(startDate);
		if (endDate != null)
			st.setEndDate(endDate);
		st.setFrequencyType(frequencyType);
		st.setScheduleDay(scheduleDay);
		st.setScheduleWeek(scheduleWeek);
		st.setScheduleMonth(scheduleMonth);
//		st.setDate(date);
		st.setDescription(description);
		st.setAmount(amount);
		st.setFrom(from);
		st.setTo(to);

		return st;
	}

	/**
	 * Creates a new Transaction with the given values
	 * @param date
	 * @param description
	 * @param amount
	 * @param from
	 * @param to
	 * @return
	 * @throws InvalidValueException
	 */
	public static Transaction createTransaction(Date date, String description, long amount, Source from, Source to) throws InvalidValueException {
		Transaction t = new TransactionImpl();

		t.setDate(date);
		t.setDescription(description);
		t.setAmount(amount);
		t.setFrom(from);
		t.setTo(to);

		return t;
	}


	/**
	 * Returns the auto save file locaton for the given base file.  If the base file
	 * is null, return the Preferences directory and a filename of "AutosaveData.buddi3autosave";
	 * if the base file is not null, return the location of the data file, with the extension
	 * changed from .buddi3 to .buddi3autosave.
	 * @param baseFile
	 * @return
	 */
	public static File getAutoSaveLocation(File baseFile){
		if (baseFile == null)
			return OperatingSystemUtil.getUserFile("Buddi", "AutosaveData" + Const.AUTOSAVE_FILE_EXTENSION);
		else {
			String autoSaveLocationString = baseFile.getAbsolutePath();
			autoSaveLocationString = 
				autoSaveLocationString.replaceAll(
						Const.DATA_FILE_EXTENSION + "$", "");
			autoSaveLocationString = 
				autoSaveLocationString 
				+ Const.AUTOSAVE_FILE_EXTENSION;
			return new File(autoSaveLocationString);
		}
	}
}
