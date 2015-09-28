/*
 * Created on Aug 21, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.util;

import ca.digitalcave.moss.crypto.CipherException;
import ca.digitalcave.moss.crypto.CryptoFactory;


/**
 * A class which extends CipherStreamFactory and provides the header value
 * for Buddi to use when saving / loading data files.
 * 
 * @author wyatt
 *
 */
public class BuddiCryptoFactory  extends CryptoFactory {
	private static final byte[] HEADER = "buddi_data_03.00".getBytes();

	public BuddiCryptoFactory() throws CipherException {}
	
	@Override
	public byte[] getHeader() {
		return HEADER;
	}
	
	@Override
	public boolean isSaveDate() {
		return true;
	}
	
	@Override
	public boolean isCompressData() {
		return true;
	}
}
