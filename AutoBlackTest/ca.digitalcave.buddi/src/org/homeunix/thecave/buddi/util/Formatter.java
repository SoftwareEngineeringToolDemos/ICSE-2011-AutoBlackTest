/*
 * Created on May 9, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.util;

import java.awt.FontMetrics;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;


/**
 * A combination of many Formatting functions.
 * 
 * @author wyatt
 *
 */
public class Formatter {
	
	private static StringLengthFormat lengthFormat = new StringLengthFormat();
	
	public static NumberFormat getDecimalFormat(){
		return getDecimalFormat(2);
	}
	
	public static NumberFormat getDecimalFormat(int decimalPlaces){
		NumberFormat f = DecimalFormat.getInstance();
		f.setMaximumFractionDigits(decimalPlaces);
		f.setMinimumFractionDigits(decimalPlaces);
		
		return f;
	}
	
	public static DateFormat getDateFormat(){
		return SimpleDateFormat.getInstance();
	}
	
	public static DateFormat getDateFormat(String format){
		return new SimpleDateFormat(format);
	}
	
	public static StringLengthFormat getStringLengthFormat(int length){
		lengthFormat.setLength(length);
		return lengthFormat;
	}
	
	/**
	 * Returns a string which will be at most maxPixelWidth pixels long as displayed by
	 * the given FontMetrics object.  If the string is any longer than that, it will
	 * be cut off, and show '...' at the end.
	 * @param value
	 * @param maxPixelWidth
	 * @param fm
	 * @return
	 */
	public static String getStringToLength(String value, int maxPixelWidth, FontMetrics fm){
		if (fm == null)
			return value;
		int width;
		for(width = 5; width <= value.length() && fm.stringWidth(value.substring(0, width)) < maxPixelWidth; width++);
		return getStringLengthFormat(width).format(value);
	}
	
//	public static String getDecimalFormattedToLength(long value, int decimalPlaces, int maxPixelWidth, FontMetrics fm){
//		int width;
//		String temp = getDecimalFormat(decimalPlaces).format(value);
//		for(width = 1; width <= temp.length() && fm.stringWidth(temp.substring(0, width)) < maxPixelWidth; width++);
//		temp = temp.substring(0, width).replaceAll("\\D", "");
//		int sigDigs = temp.length();
//		int totalDigs = (value + "").length();
//		long newValue = value;
//		String unit = "";
//		if ((totalDigs - 3) <= sigDigs){
//			newValue = Math.round(value / (Math.pow(10, 3)));
//			unit = "k";
//		}
//		else if ((totalDigs - 6) <= sigDigs){
//			newValue = Math.round(value / (Math.pow(10, 6)));
//			unit = "m";
//		}
//		else if ((totalDigs - 9) <= sigDigs){
//			newValue = Math.round(value / (Math.pow(10, 9)));
//			unit = "b";
//		}
//		else if ((totalDigs - 12) <= sigDigs){
//			newValue = Math.round(value / (Math.pow(10, 12)));
//			unit = "t";
//		}
//		
//		return getDecimalFormat(decimalPlaces).format(newValue) + unit;
//	}
	
//	/**
//	 * Returns a Singleton instance of Formatter
//	 * @return
//	 */
//	public static Formatter getInstance() {
//		return SingletonHolder.instance;
//	}
//
//	/**
//	 * You should call this the first time to correctly set the
//	 * date format string.  After, you can just call getInstance().
//	 * @param dateFormatString
//	 * @return
//	 */
//	public static Formatter getInstance(String dateFormatString) {
//		SingletonHolder.dateFormatString = dateFormatString;
//		SingletonHolder.instance.setDateFormat(dateFormatString);
//		
//		return SingletonHolder.instance;
//	}
//	
//	private static class SingletonHolder {
//		private static String dateFormatString = "yyyy-MM-dd";
//		private static Formatter instance = new Formatter(dateFormatString);		
//	}
//	
//	private final NumberFormat decimalFormat;
//	private SimpleDateFormat dateFormat;
//	private final SimpleDateFormat shortDateFormat;
//	private final LengthFormat lengthFormat;
//	
//	private Formatter(String dateFormatString){
//		decimalFormat = DecimalFormat.getInstance();
//		decimalFormat.setMaximumFractionDigits(2);
//		decimalFormat.setMinimumFractionDigits(2);
//				
//		shortDateFormat = new SimpleDateFormat("MM/dd");
//		
//		setDateFormat(dateFormatString);
//		
//		lengthFormat = new LengthFormat();
//	}
//	
//	/**
//	 * Forces a reload of the DateFormat object
//	 * @param dateFormatString
//	 */
//	public void setDateFormat(String dateFormatString){
//		dateFormat = new SimpleDateFormat(dateFormatString);
//	}
//	
//	/**
//	 * Returns the DecimalFormat object
//	 * @return
//	 */
//	public NumberFormat getDecimalFormat(){
//		return decimalFormat;
//	}
//		
//	/**
//	 * Returns the DateFormat object
//	 * @return
//	 */
//	public SimpleDateFormat getDateFormat(){
//		return dateFormat;
//	}
//	
//	/**
//	 * Returns the LengthFormat object, using specified length
//	 * @param length
//	 * @return
//	 */
//	public LengthFormat getLengthFormat(int length){
//		lengthFormat.setLength(length);
//		return lengthFormat;
//	}
//	
//	/**
//	 * Returns the ShortDateFormat object (for formatting dates when
//	 * space is at a premium)
//	 * @return
//	 */
//	public SimpleDateFormat getShortDateFormat(){
//		return shortDateFormat;
//	}
//
	/**
	 * A formatter-style class which cuts strings off after a given length.
	 * @author wyatt
	 *
	 */
	public static class StringLengthFormat{
		private int length;
				
		public String format(Object o){
			StringBuffer sb = new StringBuffer();
			sb.append(o.toString());
			
			if (length < sb.toString().length()){
				sb.delete(length, sb.length());
				sb.append("...");
			}
			
			return sb.toString();
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}
	}
}
