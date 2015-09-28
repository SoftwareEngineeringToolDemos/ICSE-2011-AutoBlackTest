/*
 * Created on Aug 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.test;

import org.homeunix.thecave.buddi.test.model.AccountTest;
import org.homeunix.thecave.buddi.test.model.BudgetCategoryTest;
import org.homeunix.thecave.buddi.test.model.CloneTest;
import org.homeunix.thecave.buddi.test.model.DocumentTest;
import org.homeunix.thecave.buddi.test.model.ScheduledTransactionTest;
import org.homeunix.thecave.buddi.test.model.SplitsTest;
import org.homeunix.thecave.buddi.test.model.TransactionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CloneTest.class,
	DocumentTest.class,
	AccountTest.class,
	BudgetCategoryTest.class,
	ScheduledTransactionTest.class,
	SplitsTest.class,
	TransactionTest.class,
})
public class BuddiTests {}
