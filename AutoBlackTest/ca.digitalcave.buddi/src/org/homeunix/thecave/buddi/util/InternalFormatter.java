/*
 * Created on Apr 11, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.util;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.Transaction;

public class InternalFormatter {
//	public static DateFormat getDateFormat(){
//		return Formatter.getDateFormat(PrefsModel.getInstance().getDateFormat());
//	}
//
//	/**
//	 * Converts a long value (in cents: 10000 == $100.00) to a string
//	 * with proper decimal values, with the user's desired currency
//	 * sign in the user's specified position (whether behind or in front
//	 * of the amount).  It is highly recommended that you use this method
//	 * to output monetary values, as it presents the user with a constant
//	 * look for currency.
//	 * 
//	 * Note that this method uses HTML font tags to set the color.  This
//	 * means that you must wrap all code which uses this with HTML start 
//	 * and end tags - you can use the getHtmlWrapper() method to do this.
//	 * 
//	 * @param value The currency amount, in cents (as per Buddi's internal 
//	 * representation of currency).  For instance, to represent the value
//	 * $123.45, you would pass in 12345.
//	 * @param red Wrap the return string in HTML font tags to make it red.
//	 * @param negate Multiply the value by *1 before rendering.  
//	 * @return A string with proper decimal places, plus the user's defined 
//	 * currency symbol in the correct position (whether before or after the
//	 * amount).  Optionally it will be wrapped in red font tags.
//	 */
//	public static String getFormattedCurrency(long value, boolean negate, boolean red){
//		if (negate)
//			value *= -1;
//
//		boolean symbolAfterAmount = PrefsModel.getInstance().isShowCurrencyAfterAmount();
//		String symbol = PrefsModel.getInstance().getCurrencySign();
//
//		
//		String formatted = 
//			(red ? "<font color='red'>" : "")
//			+ (symbolAfterAmount ? "" : symbol)
//			+ Formatter.getDecimalFormat().format((double) value / 100.0)  
//			+ (symbolAfterAmount ? " " + symbol : "")
//			+ (red ? "</font>" : "");
//
//		return formatted;
//	}
//	
//	public static String getFormattedCurrency(long value, boolean negate){
//		return getFormattedCurrency(value, negate, isRed(value));
//	}
//	
//	public static String getFormattedCurrency(long value){
//		return getFormattedCurrency(value, false, false);
//	}
//
//	/**
//	 * Returns the name, formatted as a String.  This method complies
//	 * with the Buddi Formatting Standard, and should be used whenever
//	 * possible.
//	 * @param a
//	 * @return
//	 */
//	public static String getFormattedNameForAccount(Account a){
//		return getFormattedNameGeneric(a.getName(), a.getType().isCredit());
//	}
//
//	/**
//	 * Returns the name, formatted as a String.  This method complies
//	 * with the Buddi Formatting Standard, and should be used whenever
//	 * possible.
//	 * @param c
//	 * @return
//	 */
//	public static String getFormattedNameForCategory(BudgetCategory c){
//		return getFormattedNameGeneric(c.getName(), !c.isIncome());
//	}
//
//	/**
//	 * Returns the name, formatted as a String.  This method complies
//	 * with the Buddi Formatting Standard, and should be used whenever
//	 * possible.
//	 * @param t
//	 * @return
//	 */
//	public static String getFormattedNameForType(Type t){
//		return getFormattedNameGeneric(t.getName(), t.isCredit());
//	}
//
//	/**
//	 * Returns the given string, optionally wrapped in red font tags.
//	 * @param name
//	 * @param isRed
//	 * @return
//	 */
//	public static String getFormattedNameGeneric(String name, boolean isRed){
//		String formatted = 
//			(isRed ? "<font color='red'>" : "")
//			+ PrefsModel.getInstance().getTranslator().get(name)  
//			+ (isRed ? "</font>" : "");
//
//		return formatted;
//	}
//	
//	public static boolean isRed(Source s){
//		if (s instanceof Account){
//			return ((Account) s).getType().isCredit();
//		}
//		else if (s instanceof BudgetCategory){
//			return !((BudgetCategory) s).isIncome();
//		}
//		else
//			return false;
//	}
//
//	public static boolean isRed(Account a, long value){
//		if (a.getType().isCredit())
//			return value <= 0;
//		else
//			return value < 0;
//	}
//	
//	public static boolean isRed(BudgetCategory c, long value){
//		if (c.isIncome())
//			return value < 0;
//		else
//			return value >= 0;
//	}
//
//	public static boolean isRed(Type t){
//		return t.isCredit();
//	}
//
//	public static boolean isRed(Type t, long value){
//		if (t.isCredit()){
//			return value <= 0;	
//		}
//		else {
//			return value < 0;
//		}
//	}
//	
//	public static boolean isRed(Transaction t){
//		return !t.isInflow();
//	}
//	
//	public static boolean isRed(Transaction t, boolean toSelectedAccount){
//		if (!toSelectedAccount && t.getAmount() >= 0
//				|| toSelectedAccount && t.getAmount() < 0)
//			return true;
//		else
//			return false;
//	}
//
//	public static boolean isRed(long value){
//		if (value < 0)
//			return true;
//		else
//			return false;
//	}

	public static Dimension getComponentSize(JComponent component, int minWidth){
		return new Dimension(Math.max(minWidth, component.getPreferredSize().width), Math.max(component.getPreferredSize().height, component.getSize().height));
	}

	public static Dimension getComboBoxSize(JComboBox comboBox){
		return getComponentSize(comboBox, 150);
	}

	public static Dimension getButtonSize(JButton button){
		return getComponentSize(button, 100);
	}
	
	public static boolean isRed(Source s){
		if (s instanceof Account){
			return ((Account) s).getAccountType().isCredit();
		}
		else if (s instanceof BudgetCategory){
			return !((BudgetCategory) s).isIncome();
		}
		else
			return false;
	}

	public static boolean isRed(Account a, long value){
		return value < 0;
	}
	
	public static boolean isRed(BudgetCategory c, long value){
		if (c.isIncome())
			return value < 0;
		else
			return value >= 0;
	}

	public static boolean isRed(AccountType t){
		return t.isCredit();
	}

	public static boolean isRed(AccountType t, long value){
		return value < 0;
	}
	
	public static boolean isRed(Transaction t){
		return !t.isInflow();
	}
	
	public static boolean isRed(Transaction t, boolean toSelectedAccount){
		if (!toSelectedAccount && t.getAmount() >= 0
				|| toSelectedAccount && t.getAmount() < 0)
			return true;
		else
			return false;
	}

	public static boolean isRed(long value){
		if (value < 0)
			return true;
		else
			return false;
	}
//	WMO - Moved to Moss
//	public static String formatStringToMaxSize(String value, int maxPixelWidth, FontMetrics fm){
//		int width;
//		for(width = 5; width <= value.length() && fm.stringWidth(value.substring(0, width)) < maxPixelWidth; width++);
//		return Formatter.getStringLengthFormat(width).format(value);
//	}
//	
//	public static String getHtmlWrapper(String htmlToWrap){
//		return "<html>" + htmlToWrap + "</html>";
//	}
}
