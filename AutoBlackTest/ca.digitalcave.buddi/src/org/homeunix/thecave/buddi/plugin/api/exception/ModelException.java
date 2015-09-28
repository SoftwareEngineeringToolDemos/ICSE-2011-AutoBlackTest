/*
 * Created on Aug 3, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.exception;


/**
 * The operation which was attempted on the model did not succeed, and is to be cancelled.
 *  
 * @author wyatt
 *
 */
public class ModelException extends Exception {
	public static final long serialVersionUID = 0;
	
	public ModelException() {
		super();
	}
	public ModelException(String message) {
		super(message);
	}
	public ModelException(Throwable cause) {
		super(cause);
	}
	public ModelException(String message, Throwable cause) {
		super(message, cause);
	}
}
