/*
 * Created on Aug 24, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequency;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.impl.AccountImpl;
import org.homeunix.thecave.buddi.model.impl.BudgetCategoryImpl;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import ca.digitalcave.moss.common.DateUtil;



public class DocumentTest {

	private Document d;

	@Before
	public void setup() throws Exception{
		d = ModelFactory.createDocument();
	}

	@Test
	public void testDocument(){
		try {
			assertTrue(d.getBudgetCategories().size() > 1);

		}
		catch (Exception e){
			fail("Exception: " + e);
		}
	}

	@Test
	public void testClone() throws Exception {
		Document model = ModelFactory.createDocument();

		for (int i = 0; i < 5; i++){
			Account a = ModelFactory.createAccount("ACME " + i, model.getAccountTypes().get((int) (Math.random() * model.getAccountTypes().size())));
			model.addAccount(a);				
		}

		for (int i = 0; i < 15; i++){
			BudgetCategory bc = ModelFactory.createBudgetCategory("ACME" + i, ModelFactory.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH), (Math.random() > 0.5));
			model.addBudgetCategory(bc);
		}

		List<BudgetCategory> budgetCategories = model.getBudgetCategories();
		List<Account> accounts = model.getAccounts();

		int MAX = 2000;
		for (int i = 0; i < MAX; i++){
			if (i % 100 == 0)
				Logger.getLogger(this.getClass().getName()).info("Creating transaction " + i + " of " + MAX);
			Account a = accounts.get((int) (Math.random() * accounts.size()));
			BudgetCategory bc = budgetCategories.get((int) (Math.random() * budgetCategories.size()));
			bc.setIncome(false);
			Transaction t = ModelFactory.createTransaction(DateUtil.getDate(2006, (int) (Math.random() * 12), (int) (Math.random() * 28)), "Test Transaction " + i, (long) (Math.random() * 1000000), a, bc);
			t.setMemo(Math.random() + "");
			model.addTransaction(t);
		}
		
		MAX = 10;
		for (int i = 0; i < MAX; i++){
			model.addScheduledTransaction(
					ModelFactory.createScheduledTransaction(
							"Scheduled Test #" + i, 
							null, 
							DateUtil.getDate(2010, 1, 1), 
							DateUtil.getDate(2010, 2, 1), 
							ScheduleFrequency.SCHEDULE_FREQUENCY_MONTHLY_BY_DATE.toString(), 
							0, 
							0, 
							0, 
							"Description", 
							100, 
							model.getAccounts().get(0), 
							model.getBudgetCategory("Groceries")));		
			
		}
		
//		long start = System.currentTimeMillis();
		Document clonedModel = model.clone();
//		long end = System.currentTimeMillis();
//		System.out.println("Time to clone: " + (end - start) + " millis.");
		
//		File f = new File("temp.test");
//		start = System.currentTimeMillis();
//		model.saveAs(f);
//		end = System.currentTimeMillis();
//		System.out.println("Time to save: " + (end - start) + " millis.");
//		f.delete();
		
		if (clonedModel == model)
			throw new Exception("Cloned object must not be identical to original!");
		
		for (Account a1 : model.getAccounts()) {
			for (Account a2 : clonedModel.getAccounts()) {
				if (a1 == a2)
					throw new Exception("Cloned object must not be identical to original!");
				if (a1.equals(a2))
					throw new Exception("Cloned object must not be equal to original!");
			}
		}
		
		for (BudgetCategory bc1 : model.getBudgetCategories()) {
			for (BudgetCategory bc2 : clonedModel.getBudgetCategories()) {
				if (bc1 == bc2)
					throw new Exception("Cloned object must not be identical to original!");
				if (bc1.equals(bc2))
					throw new Exception("Cloned object must not be equal to original!");
			}
		}
		
		for (Transaction t1 : model.getTransactions()) {
			for (Transaction t2 : clonedModel.getTransactions()) {
				if (t1 == t2)
					throw new Exception("Cloned object must not be identical to original!");
				if (t1.equals(t2))
					throw new Exception("Cloned object must not be equal to original!");				
			}
		}
		
		for (ScheduledTransaction st1 : model.getScheduledTransactions()) {
			for (ScheduledTransaction st2 : clonedModel.getScheduledTransactions()) {
				if (st1 == st2)
					throw new Exception("Cloned object must not be identical to original!");
				if (st1.equals(st2))
					throw new Exception("Cloned object must not be equal to original!");				
			}
		}
		
		checkAssertions(model, clonedModel);
		
		//Verify that cloning retains all essential data. 
		byte[] modelBytes = saveToBytes(model);
		byte[] clonedModelBytes = saveToBytes(clonedModel);
		
		checkAssertions(model, clonedModel);
		
		//Verify that serializing to XML and re-reading still retains all essential data.
		model = (Document) new XMLDecoder(new ByteArrayInputStream(modelBytes)).readObject();
		clonedModel = (Document) new XMLDecoder(new ByteArrayInputStream(clonedModelBytes)).readObject();
		
		checkAssertions(model, clonedModel);
	}

	private byte[] saveToBytes(Document d) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document d2 = d.clone();
		d2.saveToStream(baos);
		baos.flush();

		return baos.toByteArray();
	}


	private void checkAssertions(Document model, Document clonedModel){
		assertEquals(model.getAccounts().size(), clonedModel.getAccounts().size());
		assertEquals(model.getAccountTypes().size(), clonedModel.getAccountTypes().size());
		assertEquals(model.getBudgetCategories().size(), clonedModel.getBudgetCategories().size());
		assertEquals(model.getTransactions().size(), clonedModel.getTransactions().size());
		assertEquals(model.getScheduledTransactions().size(), clonedModel.getScheduledTransactions().size());
		
		for (int i = 0; i < model.getAccounts().size(); i++){
			assertEquals(model.getAccounts().get(i).getName(), clonedModel.getAccounts().get(i).getName());
			assertEquals(model.getAccounts().get(i).isDeleted(), clonedModel.getAccounts().get(i).isDeleted());
			assertEquals(model.getAccounts().get(i).getBalance(), clonedModel.getAccounts().get(i).getBalance());
		}
		
		for (int i = 0; i < model.getTransactions().size(); i++){
			assertEquals(model.getTransactions().get(i).getDescription(), clonedModel.getTransactions().get(i).getDescription());
			assertEquals(model.getTransactions().get(i).getAmount(), clonedModel.getTransactions().get(i).getAmount());
			assertEquals(model.getTransactions().get(i).getMemo(), clonedModel.getTransactions().get(i).getMemo());
			assertEquals(model.getTransactions().get(i).getTo().getName(), clonedModel.getTransactions().get(i).getTo().getName());
			assertEquals(model.getTransactions().get(i).getFrom().getName(), clonedModel.getTransactions().get(i).getFrom().getName());
		}
		
		for (int i = 0; i < model.getScheduledTransactions().size(); i++){
			assertEquals(model.getScheduledTransactions().get(i).getDescription(), clonedModel.getScheduledTransactions().get(i).getDescription());
			assertEquals(model.getScheduledTransactions().get(i).getAmount(), clonedModel.getScheduledTransactions().get(i).getAmount());
			assertEquals(model.getScheduledTransactions().get(i).getMemo(), clonedModel.getScheduledTransactions().get(i).getMemo());
			assertEquals(model.getScheduledTransactions().get(i).getTo().getName(), clonedModel.getScheduledTransactions().get(i).getTo().getName());
			assertEquals(model.getScheduledTransactions().get(i).getFrom().getName(), clonedModel.getScheduledTransactions().get(i).getFrom().getName());
		}
		
		assertEquals(model.getScheduledTransactions().get(0).getFrom().getClass(), AccountImpl.class);
		assertEquals(model.getScheduledTransactions().get(0).getTo().getClass(), BudgetCategoryImpl.class);
		assertEquals(clonedModel.getScheduledTransactions().get(0).getFrom().getClass(), AccountImpl.class);
		assertEquals(clonedModel.getScheduledTransactions().get(0).getTo().getClass(), BudgetCategoryImpl.class);
		
	}
}
