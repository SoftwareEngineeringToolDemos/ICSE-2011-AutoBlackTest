/*
 * Created on Aug 24, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.test.model;

import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.impl.AccountImpl;
import org.homeunix.thecave.buddi.model.impl.AccountTypeImpl;
import org.homeunix.thecave.buddi.model.impl.BudgetCategoryImpl;
import org.homeunix.thecave.buddi.model.impl.TransactionImpl;
import org.junit.Test;

import ca.digitalcave.moss.collections.SortedArrayList;
import ca.digitalcave.moss.common.DateUtil;



public class TransactionTest {
	
	@Test
	public void testCompare() throws Exception {
		List<Transaction> transactions = new SortedArrayList<Transaction>();
		
		Date date = new Date();
		Account a = new AccountImpl();
		BudgetCategory bc = new BudgetCategoryImpl();
		
		a.setName("Foo Account");
		a.setAccountType(new AccountTypeImpl());
		a.getAccountType().setName("Chequing");
		a.getAccountType().setCredit(false);
		
		bc.setName("Foo Budget Category");
		
		//All attributes are the same
		for(int i = 0; i < 1000; i++){
			Transaction t = new TransactionImpl();
			t.setAmount(1234l);
			t.setDate(date);
			t.setDescription("Foo");
			t.setFrom(a);
			t.setTo(bc);
			
			transactions.add(t);
		}
		
		//All attributes are random
		for(int i = 0; i < 1000; i++){
			Transaction t = new TransactionImpl();
			t.setAmount((long) (Math.random() * 100000));
			t.setDate(DateUtil.getDate((int) (Math.random() * 10) + 2000, (int) (Math.random() * 12), (int) Math.random() * 28));
			t.setDescription("Foo #" + Math.random());
			t.setFrom(a);
			t.setTo(bc);
			
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
}
