/*
 * Created on Aug 3, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.exception;


/**
 * The value being set to the model object is not valid.
 * 
 * @author wyatt
 *
 */
public class InvalidValueException extends ModelException {
	public static final long serialVersionUID = 0;
	
	public InvalidValueException() {
		super();
	}
	public InvalidValueException(String message) {
		super(message);
	}
	public InvalidValueException(Throwable cause) {
		super(cause);
	}
	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
	}
}
