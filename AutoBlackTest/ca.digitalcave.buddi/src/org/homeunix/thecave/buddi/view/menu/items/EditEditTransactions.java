/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.TransactionFrame;

import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class EditEditTransactions extends MossMenuItem{
	public static final long serialVersionUID = 0;
	
	//This has to be a AccountFrame, as we need to get selected accounts.
	public EditEditTransactions(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_EDIT_TRANSACTIONS),
				KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();
		
		if (((MainFrame) getFrame()).isMyAccountsTabSelected())
			this.setEnabled(((MainFrame) getFrame()).getSelectedAccounts().size() > 0);
		else if (((MainFrame) getFrame()).isMyBudgetTabSelected())
			this.setEnabled(((MainFrame) getFrame()).getSelectedBudgetCategories().size() > 0);
		else {
			this.setEnabled(false);
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		((MainFrame) getFrame()).getDocument().startBatchChange();
		List<? extends Source> selectedSources;
		
		if (((MainFrame) getFrame()).isMyAccountsTabSelected())
			selectedSources = ((MainFrame) getFrame()).getSelectedAccounts();
		else if (((MainFrame) getFrame()).isMyBudgetTabSelected())
			selectedSources = ((MainFrame) getFrame()).getSelectedBudgetCategories();
		else {
			Logger.getLogger(this.getClass().getName()).warning("EditEditTransactions called from a menu other than MyAccounts or MyBudget!");
			return;
		}
		
		for (Source a : selectedSources) {
			try {
				TransactionFrame transactionsFrame = new TransactionFrame((MainFrame) getFrame(), a);
				transactionsFrame.openWindow(
						PrefsModel.getInstance().getWindowSize(((MainFrame) getFrame()).getDocument().getFile() + a.getFullName()), 
						PrefsModel.getInstance().getWindowLocation(((MainFrame) getFrame()).getDocument().getFile() + a.getFullName()));
			}
			catch (WindowOpenException woe){}
		}
		((MainFrame) getFrame()).getDocument().finishBatchChange();
	}
}
