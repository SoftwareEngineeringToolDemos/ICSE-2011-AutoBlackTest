/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.TransactionFrame;

import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class EditEditAllTransactions extends MossMenuItem{
	public static final long serialVersionUID = 0;
	
	//This has to be a AccountFrame, as we need to get selected accounts.
	public EditEditAllTransactions(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_EDIT_ALL_TRANSACTIONS));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		((MainFrame) getFrame()).getDocument().startBatchChange();
		try {
			TransactionFrame transactionsFrame = new TransactionFrame((MainFrame) getFrame(), null);
			transactionsFrame.openWindow(
					PrefsModel.getInstance().getWindowSize(((MainFrame) getFrame()).getDocument().getFile() + BuddiKeys.ALL_TRANSACTIONS.toString()), 
					PrefsModel.getInstance().getWindowLocation(((MainFrame) getFrame()).getDocument().getFile() + BuddiKeys.ALL_TRANSACTIONS.toString()));
		}
		catch (WindowOpenException woe){}
		((MainFrame) getFrame()).getDocument().finishBatchChange();
	}
}
