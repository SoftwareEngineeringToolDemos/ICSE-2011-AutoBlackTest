/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.dialogs.BackupManagerDialog;

import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class FileRevertToBackup extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public FileRevertToBackup(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE_REVERT_TO_BACKUP));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			BackupManagerDialog bmd = new BackupManagerDialog((MossDocumentFrame) getFrame());
			bmd.openWindow();
		}
		catch (WindowOpenException woe){}
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();

		this.setEnabled(getFrame() instanceof MossDocumentFrame 
				&& ((MossDocumentFrame) getFrame()).getDocument().getFile() != null);
	}
}
