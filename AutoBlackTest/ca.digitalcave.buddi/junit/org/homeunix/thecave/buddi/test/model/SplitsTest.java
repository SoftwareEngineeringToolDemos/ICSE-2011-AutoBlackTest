/*
 * Created on Aug 24, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.test.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.model.impl.AccountImpl;
import org.homeunix.thecave.buddi.model.impl.AccountTypeImpl;
import org.homeunix.thecave.buddi.model.impl.BudgetCategoryImpl;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.impl.SplitImpl;
import org.homeunix.thecave.buddi.model.impl.TransactionImpl;
import org.homeunix.thecave.buddi.model.impl.TransactionSplitImpl;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.junit.Before;
import org.junit.Test;

import ca.digitalcave.moss.collections.SortedArrayList;



public class SplitsTest {

	Document d;
	Account a1;
	Account a2;
	BudgetCategory bc1;
	BudgetCategory bc2;
	BudgetCategory bc3;
	BudgetCategory bc4;

	@Before
	public void setup() throws Exception {
		d = ModelFactory.createDocument();

		bc1 = new BudgetCategoryImpl();
		bc2 = new BudgetCategoryImpl();
		bc3 = new BudgetCategoryImpl();
		bc4 = new BudgetCategoryImpl();			
		bc1.setName("Foo Budget Category 1");
		bc2.setName("Foo Budget Category 2");
		bc3.setName("Foo Budget Category 3");
		bc4.setName("Foo Budget Category 4");
		bc3.setIncome(true);
		bc4.setIncome(true);

		a1 = new AccountImpl();
		a2 = new AccountImpl();
		a1.setName("Foo Account");
		a2.setName("Bar Account");
		a1.setAccountType(new AccountTypeImpl());
		a2.setAccountType(new AccountTypeImpl());
		a1.getAccountType().setName("Chequing");
		a1.getAccountType().setName("Cash");
		a1.getAccountType().setCredit(false);
		a2.getAccountType().setCredit(false);


		d.addAccount(a1);
		d.addAccount(a2);
		d.addBudgetCategory(bc1);
		d.addBudgetCategory(bc2);
		d.addBudgetCategory(bc3);
		d.addBudgetCategory(bc4);
	}
	
	@Test
	/**
	 * Tests a number of transaction comparisons by adding and removing transactions.
	 */
	public void testCompare() throws Exception {
		List<Transaction> transactions = new SortedArrayList<Transaction>();

		//Splitting To - all attributes are the same
		for(int i = 0; i < 1000; i++){
			Transaction t = new TransactionImpl();
			t.setAmount(1234l);
			t.setDate(new Date());
			t.setDescription("Foo");
			t.setFrom(a1);
			t.setTo(new SplitImpl());
			List<TransactionSplit> splits = new ArrayList<TransactionSplit>();
			TransactionSplit split = new TransactionSplitImpl();
			split.setAmount(1200);
			split.setSource(bc1);
			splits.add(split);
			split = new TransactionSplitImpl();
			split.setAmount(34);
			split.setSource(bc2);
			splits.add(split);
			t.setToSplits(splits);

			transactions.add(t);
		}

		//Splitting From - All attributes are the same
		for(int i = 0; i < 1000; i++){
			Transaction t = new TransactionImpl();
			t.setAmount(1234l);
			t.setDate(new Date());
			t.setDescription("Foo");
			t.setTo(a1);
			t.setFrom(new SplitImpl());
			List<TransactionSplit> splits = new ArrayList<TransactionSplit>();
			TransactionSplit split = new TransactionSplitImpl();
			split.setAmount(1200);
			split.setSource(bc1);
			splits.add(split);
			split = new TransactionSplitImpl();
			split.setAmount(34);
			split.setSource(bc2);
			splits.add(split);
			t.setFromSplits(splits);

			transactions.add(t);
		}


		while(transactions.size() > 0){
			int index = (int) (Math.random() * transactions.size());
			Transaction t = transactions.get(index);

			transactions.remove(t);

			if (transactions.contains(t))
				throw new Exception("We already removed the transaction!");
		}
	}

	@Test
	public void testWithoutSplits() throws Exception {
		Transaction t = new TransactionImpl();
		t.setAmount(1234l);
		t.setDate(new Date());
		t.setDescription("Foo");
		t.setTo(a1);
		t.setFrom(bc3);
		d.addTransaction(t);
		assertTrue(d.getTransactions().size() == 1);
	}

	@Test
	public void testWithFromSplits() throws Exception {
		Transaction t = new TransactionImpl();
		t.setAmount(1234l);
		t.setDate(new Date());
		t.setDescription("Foo");
		t.setTo(a1);
		t.setFrom(new SplitImpl());
		List<TransactionSplit> fromSplits = new ArrayList<TransactionSplit>();
		TransactionSplit fromSplit1 = new TransactionSplitImpl();
		fromSplit1.setAmount(1200);
		fromSplit1.setSource(bc3);
		fromSplits.add(fromSplit1);
		TransactionSplit fromSplit2 = new TransactionSplitImpl();
		fromSplit2.setAmount(34);
		fromSplit2.setSource(bc4);
		fromSplits.add(fromSplit2);
		t.setFromSplits(fromSplits);
		d.addTransaction(t);
		assertTrue(d.getTransactions().size() == 1);
	}

	@Test
	public void testWithToSplits() throws Exception {
		Transaction t = new TransactionImpl();
		t.setAmount(1234l);
		t.setDate(new Date());
		t.setDescription("Foo");
		t.setFrom(a1);
		t.setTo(new SplitImpl());
		List<TransactionSplit> toSplits = new ArrayList<TransactionSplit>();
		TransactionSplit toSplit1 = new TransactionSplitImpl();
		toSplit1.setAmount(1200);
		toSplit1.setSource(bc1);
		toSplits.add(toSplit1);
		TransactionSplit toSplit2 = new TransactionSplitImpl();
		toSplit2.setAmount(34);
		toSplit2.setSource(bc2);
		toSplits.add(toSplit2);
		t.setToSplits(toSplits);
		d.addTransaction(t);
		assertTrue(d.getTransactions().size() == 1);
	}

	@Test
	public void testWithBothSplits() throws Exception {
		Transaction t = new TransactionImpl();
		t.setAmount(1234l);
		t.setDate(new Date());
		t.setDescription("Foo");
		t.setTo(new SplitImpl());
		t.setFrom(new SplitImpl());
		List<TransactionSplit> fromSplits = new ArrayList<TransactionSplit>();
		TransactionSplit fromSplit1 = new TransactionSplitImpl();
		fromSplit1.setAmount(1200);
		fromSplit1.setSource(a1);
		fromSplits.add(fromSplit1);
		TransactionSplit fromSplit2 = new TransactionSplitImpl();
		fromSplit2.setAmount(34);
		fromSplit2.setSource(a2);
		fromSplits.add(fromSplit2);
		t.setFromSplits(fromSplits);
		List<TransactionSplit> toSplits = new ArrayList<TransactionSplit>();
		TransactionSplit toSplit1 = new TransactionSplitImpl();
		toSplit1.setAmount(1200);
		toSplit1.setSource(bc1);
		toSplits.add(toSplit1);
		TransactionSplit toSplit2 = new TransactionSplitImpl();
		toSplit2.setAmount(34);
		toSplit2.setSource(bc2);
		toSplits.add(toSplit2);
		t.setToSplits(toSplits);			
		d.addTransaction(t);
		assertTrue(d.getTransactions().size() == 1);
	}

	@Test
	public void testUnbalancedSplits() throws Exception {
		Transaction t = new TransactionImpl();
		t.setAmount(1234l);
		t.setDate(new Date());
		t.setDescription("Foo");
		t.setFrom(a1);
		t.setTo(new SplitImpl());
		List<TransactionSplit> failingToSplits = new ArrayList<TransactionSplit>();
		TransactionSplit toSplit1 = new TransactionSplitImpl();
		toSplit1.setAmount(1200);
		toSplit1.setSource(bc1);
		failingToSplits.add(toSplit1);
		TransactionSplit toSplit2 = new TransactionSplitImpl();
		toSplit2.setAmount(3400);
		toSplit2.setSource(bc2);
		failingToSplits.add(toSplit2);
		t.setToSplits(failingToSplits);
		try {
			d.addTransaction(t);
			fail("Incorrectly succeeded in adding transaction with unbalanced splits");
		}
		catch (ModelException me){
			//Expected to catch exception
			assertTrue(d.getTransactions().size() == 0);
		}
	}

	@Test
	public void testWithIncorrectIncomeBCSplits() throws Exception {
		Transaction t = new TransactionImpl();
		t.setAmount(1234l);
		t.setDate(new Date());
		t.setDescription("Foo");
		t.setFrom(a1);
		t.setTo(new SplitImpl());
		List<TransactionSplit> failingToSplits = new ArrayList<TransactionSplit>();
		TransactionSplit toSplit1 = new TransactionSplitImpl();
		toSplit1.setAmount(1200);
		toSplit1.setSource(bc3);
		failingToSplits.add(toSplit1);
		TransactionSplit toSplit2 = new TransactionSplitImpl();
		toSplit2.setAmount(34);
		toSplit2.setSource(bc4);
		failingToSplits.add(toSplit2);
		t.setToSplits(failingToSplits);
		try {
			d.addTransaction(t);
			fail("Incorrectly succeeded in adding transaction with income BCs in the To location");
		}
		catch (ModelException me){
			//Expected to catch exception
			assertTrue(d.getTransactions().size() == 0);
		}
	}
}
