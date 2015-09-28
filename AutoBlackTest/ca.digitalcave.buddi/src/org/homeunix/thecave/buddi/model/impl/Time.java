/*
 * Created on Oct 10, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.Date;

/**
 * A wrapper around Time.  We use a persistence delegate for Dates in the XML
 * file to avoid storing more information than we need (and to avoid time zone
 * bugs), but for certain things, such as Modified Date, we need more accuracy 
 * than just 'day'.
 * @author wyatt
 *
 */
public class Time extends Date {
	public static final long serialVersionUID = 0;
	
	public Time() {
		super();
	}
	
	public Time(long time){
		super(time);
	}
	
	public Time(Date date){ 
		super(date != null ? date.getTime() : new Date().getTime());
	}
}
