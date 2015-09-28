/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.TransactionFrame;

import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;

public class EditDeleteTransaction extends MossMenuItem {
	public static final long serialVersionUID = 0;

	public EditDeleteTransaction(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_DELETE_TRANSACTION));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		((TransactionFrame) getFrame()).doClickDelete();
	}
}
