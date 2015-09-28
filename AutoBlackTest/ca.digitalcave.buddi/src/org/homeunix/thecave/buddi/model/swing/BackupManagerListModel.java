/*
 * Created on Sep 2, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.util.BuddiCryptoFactory;

import ca.digitalcave.moss.crypto.CipherException;
import ca.digitalcave.moss.crypto.IncorrectDocumentFormatException;

public class BackupManagerListModel extends AbstractListModel {
	public static final long serialVersionUID = 0;
	
	private final List<File> backupDocuments;
	private final Map<File, Date> backupFileMap;
	
	public BackupManagerListModel(Document document) {
		backupFileMap = getAssociatedBackups(document.getFile());
		backupDocuments = new LinkedList<File>(backupFileMap.keySet());
		Collections.sort(backupDocuments, new Comparator<File>(){
			public int compare(File o1, File o2) {
				Date d1 = backupFileMap.get(o1);
				Date d2 = backupFileMap.get(o2);
				
				if (d1 == null || d2 == null || d1.equals(d2))
					return o1.compareTo(o2);
				return d1.compareTo(d2) * -1; //Inverse date sorting
			}
		});
	}
	
	public File getElementAt(int index) {
		return backupDocuments.get(index);
	}
	
	public int getSize() {
		return backupDocuments.size();
	}
	
	public Date getDate(File f){
		return backupFileMap.get(f);
	}
	
	private Map<File, Date> getAssociatedBackups(final File dataFile){
		Map<File, Date> backups = new HashMap<File, Date>();

		if (dataFile != null){
			File folder = dataFile.getParentFile();
			try {
				BuddiCryptoFactory bcf = new BuddiCryptoFactory();
				for (File f : folder.listFiles(new FileFilter(){
					public boolean accept(File pathname) {
						if (pathname.getName().matches(
								dataFile.getName().replaceAll(Const.DATA_FILE_EXTENSION + "$", "") 
								+ "_\\d+\\" 
								+ Const.BACKUP_FILE_EXTENSION))
							return true;
						return false;
					}
				})) {
					try {
						File newFile = new File(f.getAbsolutePath());
						backups.put(f, bcf.getTimestamp(new FileInputStream(newFile)));
					}
					catch (IOException ioe){
						Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "IOException for file " + f, ioe);
					}
					catch (IncorrectDocumentFormatException idfe){
						Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Incorrect document format for file " + f, idfe);
					}
				}
			}
			catch (CipherException ce){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cipher Exception", ce);
			}
		}

		return backups;
	}
}
