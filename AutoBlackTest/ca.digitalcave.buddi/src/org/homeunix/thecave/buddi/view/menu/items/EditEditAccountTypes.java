/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.AccountTypeListFrame;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class EditEditAccountTypes extends MossMenuItem{
	public static final long serialVersionUID = 0;
	
	//This has to be a AccountFrame, as we need to get selected accounts.
	public EditEditAccountTypes(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_EDIT_ACCOUNT_TYPES));
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		((MainFrame) getFrame()).getDocument().startBatchChange();

		try {
			new AccountTypeListFrame((MainFrame) getFrame()).openWindow();
		}
		catch (WindowOpenException woe){}
		
		((MainFrame) getFrame()).getDocument().finishBatchChange();
	}
}
