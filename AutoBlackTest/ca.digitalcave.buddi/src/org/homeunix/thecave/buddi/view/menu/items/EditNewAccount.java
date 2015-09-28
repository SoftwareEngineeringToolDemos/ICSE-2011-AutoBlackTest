/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.dialogs.AccountEditorDialog;

import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class EditNewAccount extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public EditNewAccount(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_NEW_ACCOUNT),
				KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AccountEditorDialog editor = new AccountEditorDialog((MainFrame) getFrame(), (Document) ((MainFrame) getFrame()).getDocument(), null);
		try {
			editor.openWindow();
		}
		catch (WindowOpenException woe){}
		
		((MainFrame) getFrame()).fireStructureChanged();
		((MainFrame) getFrame()).updateContent();
	}
}
