/*
 * Created on Oct 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api;

public interface FileAccess {
	/**
	 * Should Buddi prompt for a file to import?  Defaults to true.  If you know what
	 * the file name is (or you are importing from a different source, such as the 
	 * network), you should override this method and return false. 
	 * @return
	 */
	public boolean isPromptForFile();
	
	/**
	 * Override to specify that Buddi should only include certain file types in 
	 * the file chooser.  This only has an effect if isPromptForFile() is true.  If
	 * you want to do this, return a String array of the file extensions which you wish
	 * to match.  The plugin loader will create a FileFilter for this.
	 * @return
	 */
	public String[] getFileExtensions();
	
	/**
	 * Returns the message which should show when processing the data.  Defaults
	 * to "Processing File..."; override if needed.  This value is filtered through
	 * the translator before being displayed.
	 * @return
	 */
	public String getProcessingMessage();
	
	/**
	 * Return the description which shows up in the file chooser.  By default, this 
	 * is set to "Buddi [Import|Export|Synchronize] Files".  This value is filtered 
	 * through the translator before being displayed.
	 */
	public String getDescription();
}
