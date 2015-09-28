/*
 * Created on Aug 24, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.junit.Test;

import ca.digitalcave.moss.common.DateUtil;



public class AccountTest {
	
	@Test
	public void testGetBalanceDate(){
		try {
			Document d = ModelFactory.createDocument();
			AccountType at = d.getAccountType("Cash");
			Account a = ModelFactory.createAccount("Test", at);
			d.addAccount(a);
			
			d.addTransaction(ModelFactory.createTransaction(
					DateUtil.getDate(2007, Calendar.AUGUST, 1), "Test 1", 100, a, d.getBudgetCategory("Groceries")));
			d.addTransaction(ModelFactory.createTransaction(
					DateUtil.getDate(2007, Calendar.AUGUST, 5), "Test 2", 100, a, d.getBudgetCategory("Groceries")));
			d.addTransaction(ModelFactory.createTransaction(
					DateUtil.getDate(2007, Calendar.AUGUST, 10), "Test 3", -100, a, d.getBudgetCategory("Groceries")));
			d.addTransaction(ModelFactory.createTransaction(
					DateUtil.getDate(2007, Calendar.SEPTEMBER, 1), "Test 4", -100, a, d.getBudgetCategory("Groceries")));
			d.addTransaction(ModelFactory.createTransaction(
					DateUtil.getDate(2007, Calendar.SEPTEMBER, 2), "Test 5", -100, a, d.getBudgetCategory("Groceries")));
			
			d.updateAllBalances();
			
			Object[][] tests = {
					{DateUtil.getDate(2007, Calendar.JULY, 30), 0l},
					{DateUtil.getDate(2007, Calendar.AUGUST, 1), -100l},
					{DateUtil.getDate(2007, Calendar.AUGUST, 2), -100l},
					{DateUtil.getDate(2007, Calendar.AUGUST, 4), -100l},
					{DateUtil.getDate(2007, Calendar.AUGUST, 5), -200l},
					{DateUtil.getDate(2007, Calendar.AUGUST, 6), -200l},
					{DateUtil.getDate(2007, Calendar.AUGUST, 30), -100l},
					{DateUtil.getDate(2007, Calendar.SEPTEMBER, 3), 100l}
			};
			
			for (Object[] test : tests) {
				assertEquals((Long) test[1], (Long) a.getBalance((Date) test[0]));
			}
		}
		catch (Exception e){
			fail("Exception: " + e);
		}
	}
	
	@Test
	public void accountStartDate() throws Exception {
		Document d = ModelFactory.createDocument();
		AccountType at = d.getAccountType("Cash");
		Account a = ModelFactory.createAccount("Test", at);
		BudgetCategory bc = d.getBudgetCategory("Groceries");
		d.addAccount(a);

		//StartDate == null, No transactions; return today
		// We wrap each in getStartOfDay() to avoid millisecond differences, if the get()
		// method takes a few millis.
		assertEquals(DateUtil.getStartOfDay(a.getStartDate()), DateUtil.getStartOfDay(new Date()));
		
		//StartDate != null, No transactions; return startDate
		Date startDate = DateUtil.getDate(2007, Calendar.JANUARY, 3);
		a.setStartDate(startDate);
		assertEquals(startDate, a.getStartDate());
		
		//StartDate != null, Transactions exist, and are after startDate; return startDate
		Transaction t = ModelFactory.createTransaction(
				DateUtil.getDate(2007, Calendar.JANUARY, 5), 
				"Test", 
				1234, 
				a,
				bc);
		d.addTransaction(t);
		assertEquals(a.getStartDate(), startDate);
		
		//StartDate != null, Transactions exist, and are before startDate; return first transaction
		t = ModelFactory.createTransaction(
				DateUtil.getDate(2007, Calendar.JANUARY, 1), 
				"Test", 
				1234, 
				a,
				bc);
		d.addTransaction(t);
		assertEquals(a.getStartDate(), DateUtil.getDate(2007, Calendar.JANUARY, 1));
		
		//StartDate == null, Transactions exist; return first transaction
		a.setStartDate(null);
		assertEquals(a.getStartDate(), DateUtil.getDate(2007, Calendar.JANUARY, 1));
	}
	
	@Test
	public void testAccountSerialization() throws Exception {
		Account a = ModelFactory.createAccount("Foo", ModelFactory.createAccountType("Cash", false));
		a.setInterestRate(1234);
		a.setNotes("Foo bar notes");
		a.setOverdraftCreditLimit(123);
		a.setStartingBalance(10000);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream(); 
		XMLEncoder encoder = new XMLEncoder(os);
		encoder.writeObject(a);
		encoder.flush();
		encoder.close();
		
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		XMLDecoder decoder = new XMLDecoder(is);
		Object o = decoder.readObject();
		decoder.close();
		
		if (!(o instanceof Account)){
			throw new Exception("Deserialized object is not an account!");
		}
		
		Account newAccount = (Account) o;
		assertEquals(a.getName(), newAccount.getName());
		assertEquals(a.getAccountType(), newAccount.getAccountType());
		assertEquals(a.getInterestRate(), newAccount.getInterestRate());
		assertEquals(a.getNotes(), newAccount.getNotes());
		assertEquals(a.getOverdraftCreditLimit(), newAccount.getOverdraftCreditLimit());
		assertEquals(a.getStartingBalance(), newAccount.getStartingBalance());
	}
}
