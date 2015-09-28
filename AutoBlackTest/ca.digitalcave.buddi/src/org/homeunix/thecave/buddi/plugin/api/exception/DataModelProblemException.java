/*
 * Created on Aug 3, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.exception;

import org.homeunix.thecave.buddi.model.Document;

/**
 * The primary exception thrown when there is a problem with the data model.
 * 
 * It is recommended to throw the instance of the problem data model if at
 * all possible, as this helps in debugging.
 * 
 * This extends RuntimeException, and thus should only be thrown if there is 
 * no chance of recovery. 
 * @author wyatt
 *
 */
public class DataModelProblemException extends RuntimeException {
	public static final long serialVersionUID = 0;
	
	private Document dataModel;
	
	public DataModelProblemException() {
		this(null, null, null);
	}
	public DataModelProblemException(String message) {
		this(message, null, null);
	}
	public DataModelProblemException(Throwable cause) {
		this(null, cause, null);
	}
	public DataModelProblemException(String message, Throwable cause) {
		this(message, cause, null);
	}
	/**
	 * It is recommended to throw the instance of the problem data model if at
	 * all possible, as this helps in debugging.
	 * @param message
	 * @param dataModel
	 */
	public DataModelProblemException(String message, Document dataModel){
		this(message, null, dataModel);
	}
	/**
	 * It is recommended to throw the instance of the problem data model if at
	 * all possible, as this helps in debugging.
	 * @param message
	 * @param cause
	 * @param dataModel
	 */
	public DataModelProblemException(String message, Throwable cause, Document dataModel){
		super(message, cause);
		
		this.dataModel = dataModel;
	}
	
	public Document getDataModel(){
		return dataModel;
	}
}
