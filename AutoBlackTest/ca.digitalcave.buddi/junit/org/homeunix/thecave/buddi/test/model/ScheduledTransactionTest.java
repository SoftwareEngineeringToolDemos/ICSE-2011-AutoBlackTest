/*
 * Created on Aug 24, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.test.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequency;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ca.digitalcave.moss.common.DateUtil;


@RunWith(Parameterized.class)
public class ScheduledTransactionTest {

	@Parameters
	public static List<Object[]> getData(){
		return Arrays.asList(new Object[][] {
				{ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_DAY.toString(), //Frequency
					DateUtil.getDate(2007, Calendar.JANUARY, 1), //Start Date
					DateUtil.getDate(2007, Calendar.JANUARY, 31), //End Date
					DateUtil.getDate(2007, Calendar.FEBRUARY, 1), //Test Date
					-3100l},	//Expected amount
					{ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_DAY.toString(), //Frequency
						DateUtil.getDate(2007, Calendar.JANUARY, 1), //Start Date
						DateUtil.getDate(2007, Calendar.JANUARY, 31), //End Date
						DateUtil.getDate(2007, Calendar.MARCH, 1), //Test Date
						-3100l},	//Expected amount
						{ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_WEEKDAY.toString(), //Frequency
							DateUtil.getDate(2007, Calendar.JANUARY, 1), //Start Date
							DateUtil.getDate(2007, Calendar.JANUARY, 31), //End Date
							DateUtil.getDate(2007, Calendar.FEBRUARY, 1), //Test Date
							-2300l},	//Expected amount
							{ScheduleFrequency.SCHEDULE_FREQUENCY_BIWEEKLY.toString(), //Frequency
								DateUtil.getDate(2007, Calendar.JANUARY, 1), //Start Date
								DateUtil.getDate(2007, Calendar.JANUARY, 31), //End Date
								DateUtil.getDate(2007, Calendar.FEBRUARY, 1), //Test Date
								-200l},	//Expected amount
								{ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_WEEKDAY.toString(), //Frequency
									DateUtil.getDate(2007, Calendar.JANUARY, 1), //Start Date
									DateUtil.getDate(2007, Calendar.JANUARY, 31), //End Date
									DateUtil.getDate(2007, Calendar.FEBRUARY, 1), //Test Date
									-2300l},	//Expected amount
		});
	}

	private final String freq;
	private final Date start;
	private final Date end;
	private final Date test;
	private final Long expected;

	public ScheduledTransactionTest(Object freq, Object start, Object end, Object test, Object expected) {
		this.freq = (String) freq;
		this.start = (Date) start;
		this.end = (Date) end;
		this.test = (Date) test;
		this.expected = (Long) expected;
	}

	@Test
	public void testScheduledTransactions() throws Exception {
		
		//There seems to be some sort of race condition in here.  What it is, I have 
		// no idea, as I don't think that I am using multiple threads...
		Document d = ModelFactory.createDocument();
		AccountType at = d.getAccountType("Cash");
		Account a1 = ModelFactory.createAccount("Test1", at);
		d.addAccount(a1);

		d.addScheduledTransaction(
				ModelFactory.createScheduledTransaction(
						"Test", 
						null, 
						start, 
						end, 
						freq, 
						0, 
						0, 
						0, 
						"Description", 
						100, 
						a1, 
						d.getBudgetCategory("Groceries")));


		d.updateScheduledTransactions();

		d.updateAllBalances();

		assertEquals((long) expected, a1.getBalance((Date) test));
	}

}
