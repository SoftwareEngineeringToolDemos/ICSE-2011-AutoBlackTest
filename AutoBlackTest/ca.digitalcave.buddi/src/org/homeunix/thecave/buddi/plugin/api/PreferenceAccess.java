/*
 * Created on Sep 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api;

import java.util.List;

import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.util.BuddiCryptoFactory;

/**
 * An abstract class which provides plugins the functionality of storing and
 * reading preferences in the main Buddi preferences file. 
 * @author wyatt
 *
 */
public abstract class PreferenceAccess {
	/**
	 * Loads the value which is associated with the given key.
	 * @param key The key to read.
	 * @return The value associated with the given key, or null if there was no 
	 * such value set.
	 */
	public String getPreference(String key){
		return PrefsModel.getInstance().getPluginPreference(key);
	}
	
	/**
	 * Stores the value associated with a given key.
	 * @param key The key to save.  In order to ensure that you do not
	 * overwrite the preferences for another plugin, you must use
	 * the key format as follows:
	 * 
	 * "package.Plugin.property"
	 * 
	 * For instance, use the key "com.example.buddi.ImportFoo.LAST_EXECUTE_DAY" 
	 * instead of the key "LAST_EXECUTE_DAY".
	 * @param value The value to store.  Can be any valid String.
	 */
	public void putPreference(String key, String value){
		PrefsModel.getInstance().putPluginPreference(key, value);
	}
	
	/**
	 * Sets the given list of strings in the preferences map, using the given key.
	 * 
	 * @param key The key to save.  In order to ensure that you do not
	 * overwrite the preferences for another plugin, you must use
	 * the key format as follows:
	 * 
	 * "package.Plugin.property"
	 * 
	 * For instance, use the key "com.example.buddi.ImportFoo.LAST_EXECUTE_DAY" 
	 * instead of the key "LAST_EXECUTE_DAY".
	 * @param values The list of values to store
	 */
	public void putListPreference(String key, List<String> values){
		PrefsModel.getInstance().putPluginListPreference(key, values);
	}
	
	/**
	 * Returns the list associated with the given key.
	 * @param key The key to save.  In order to ensure that you do not
	 * overwrite the preferences for another plugin, you must use
	 * the key format as follows:
	 * 
	 * "package.Plugin.property"
	 * 
	 * For instance, use the key "com.example.buddi.ImportFoo.LAST_EXECUTE_DAY" 
	 * instead of the key "LAST_EXECUTE_DAY".

	 * @return
	 */
	public List<String> getListPreference(String key){
		return PrefsModel.getInstance().getPluginListPreference(key);
	}
	
	/**
	 * Loads the value which is associated with the given key, after decrypting 
	 * the value using the given password.
	 * @param key
	 * @param password
	 * @return
	 */
	public String getSecurePreference(String key, char[] password) throws PluginException {
		try {
			BuddiCryptoFactory crypto = new BuddiCryptoFactory();
			String ciphertext = PrefsModel.getInstance().getPluginPreference(key);
			if (ciphertext == null)
				return null;
			return crypto.getDecryptedString(ciphertext, password);
			
		}
		catch (Exception e){
			throw new PluginException("Error getting encrypted preference from key " + key, e);
		}
	}
	
	/**
	 * Stores the value, encrypted using the given password, into the preferences
	 * file using the given key.
	 * @param key
	 * @param value
	 * @param password
	 */
	public void putSecurePreference(String key, String value, char[] password) throws PluginException {
		try {
			BuddiCryptoFactory crypto = new BuddiCryptoFactory();
			String encrypted = crypto.getEncryptedString(value, password);
			PrefsModel.getInstance().putPluginPreference(key, encrypted);
		}
		catch (Exception e){
			throw new PluginException("Error putting encrypted preference to key " + key, e);
		}
	}
}
