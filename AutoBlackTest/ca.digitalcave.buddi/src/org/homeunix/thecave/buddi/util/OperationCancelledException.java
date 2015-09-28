/*
 * Created on Aug 3, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.util;

public class OperationCancelledException extends Exception {
	public static final long serialVersionUID = 0;
	
	public OperationCancelledException() {
		super();
	}
	public OperationCancelledException(String message) {
		super(message);
	}
	public OperationCancelledException(Throwable cause) {
		super(cause);
	}
	public OperationCancelledException(String message, Throwable cause) {
		super(message, cause);
	}
}
