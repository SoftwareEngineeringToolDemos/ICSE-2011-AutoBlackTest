/*
 * Created on Aug 3, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.exception;

import javax.swing.JOptionPane;


/**
 * The exception thrown when you need to notify the user of something.  This is generally
 * an error, but can be good as well ("Plugin Completed Successfully").
 * 
 * I suppose that to be completely correct, I should make two versions of this: one 
 * solely an error (implemented via an Exception class), and the other an object
 * which can be returned.  However, throwing this should still work fine, so we may as well
 * just do it this way, as it is simpler from the plugin implementor's side.  
 * 
 * @author wyatt
 *
 */
public class PluginMessage extends Exception {
	public static final long serialVersionUID = 0;
	
	private final String longMessage;
	private final String title;
	private final int type;
	
	public PluginMessage(String message) {
		this(message, null, "", JOptionPane.PLAIN_MESSAGE);
	}
	
	public PluginMessage(String message, String longMessage, String title, int type) {
		super(message);
		
		this.longMessage = longMessage;
		this.title = title;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public int getType() {
		return type;
	}
	
	public String getLongMessage(){
		return longMessage;
	}
}
