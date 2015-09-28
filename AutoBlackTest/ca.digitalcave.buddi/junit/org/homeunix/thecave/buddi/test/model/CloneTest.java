/*
 * Created on Aug 24, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.test.model;

import static org.junit.Assert.assertEquals;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequency;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.AccountImpl;
import org.homeunix.thecave.buddi.model.impl.BudgetCategoryImpl;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import ca.digitalcave.moss.common.DateUtil;



public class CloneTest {

	private Document d;

	@Before
	public void setup() throws Exception{
		d = ModelFactory.createDocument();
	}

	@Test
	public void testClone() throws Exception {
		Document model = ModelFactory.createDocument();
		AccountType at = model.getAccountType("Cash");
		Account a1 = ModelFactory.createAccount("Test1", at);
		model.addAccount(a1);

		model.addScheduledTransaction(
				ModelFactory.createScheduledTransaction(
						"Test", 
						null, 
						DateUtil.getDate(2010, 1, 3), 
						DateUtil.getDate(2010, 1, 3), 
						ScheduleFrequency.SCHEDULE_FREQUENCY_MONTHLY_BY_DATE.toString(), 
						0, 
						0, 
						0, 
						"Description", 
						100, 
						a1, 
						model.getBudgetCategory("Groceries")));
	
		//Verify that cloning retains all essential data. 
		byte[] modelBytes = saveToBytes(model);
		
		System.out.println(new String(modelBytes, "UTF-8"));
		
		//Verify that serializing to XML and re-reading still retains all essential data.
		model = (Document) new XMLDecoder(new ByteArrayInputStream(modelBytes)).readObject();
		
//		modelBytes = saveToBytes(model);
//		System.out.println(new String(modelBytes, "UTF-8"));
		
		assertEquals(model.getScheduledTransactions().get(0).getFrom().getClass(), AccountImpl.class);
		assertEquals(model.getScheduledTransactions().get(0).getTo().getClass(), BudgetCategoryImpl.class);
	}

	private byte[] saveToBytes(Document d) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document d2 = d.clone();
		d2.saveToStream(baos);
		baos.flush();

		return baos.toByteArray();
	}
}
