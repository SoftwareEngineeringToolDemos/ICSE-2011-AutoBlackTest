/*
 * Created on Nov 3, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.Date;

import ca.digitalcave.moss.common.DateUtil;

public class Day extends Date{
	public static final long serialVersionUID = 0;
	
	public Day(int year, int month, int day) {
		DateUtil.setDate(this, year, month, day);
	}
	
	public Day(Date date) {
		this.setTime(date.getTime());
	}
	
	public Day() {
		this.setTime(0);
	}
}
