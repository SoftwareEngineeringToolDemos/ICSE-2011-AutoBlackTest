/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.menus;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.menu.items.FileCloseWindow;
import org.homeunix.thecave.buddi.view.menu.items.FileNew;
import org.homeunix.thecave.buddi.view.menu.items.FileOpen;
import org.homeunix.thecave.buddi.view.menu.items.FileQuit;
import org.homeunix.thecave.buddi.view.menu.items.FileRevertToBackup;
import org.homeunix.thecave.buddi.view.menu.items.FileRevertToSaved;
import org.homeunix.thecave.buddi.view.menu.items.FileSave;
import org.homeunix.thecave.buddi.view.menu.items.FileSaveAll;
import org.homeunix.thecave.buddi.view.menu.items.FileSaveAs;

import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenu;

public class FileMenu extends MossMenu {
	public static final long serialVersionUID = 0;
	
	public FileMenu(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE));
		
		this.add(new FileNew(frame));
		this.add(new FileOpen(frame));
		this.addSeparator();
		this.add(new FileSave(frame));
		this.add(new FileSaveAs(frame));
		this.add(new FileSaveAll(frame));
		this.add(new FileRevertToSaved(frame));
		this.addSeparator();
		this.add(new FileRevertToBackup(frame));
		this.addSeparator();
		this.add(new FileImportMenu(frame));
		this.add(new FileExportMenu(frame));
		this.add(new FileSynchronizeMenu(frame));
		this.addSeparator();
		this.add(new FileCloseWindow(frame));
		if (!OperatingSystemUtil.isMac()){
			this.addSeparator();
			this.add(new FileQuit(frame));
		}
	}
}
