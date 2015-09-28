/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.menus;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.TransactionFrame;
import org.homeunix.thecave.buddi.view.menu.items.EditClearAndAdvanceTransaction;
import org.homeunix.thecave.buddi.view.menu.items.EditClearTransaction;
import org.homeunix.thecave.buddi.view.menu.items.EditCopyBudgetCategoryValuesToNext;
import org.homeunix.thecave.buddi.view.menu.items.EditCopyBudgetCategoryValuesToPrevious;
import org.homeunix.thecave.buddi.view.menu.items.EditDeleteAccount;
import org.homeunix.thecave.buddi.view.menu.items.EditDeleteBudgetCategory;
import org.homeunix.thecave.buddi.view.menu.items.EditDeleteTransaction;
import org.homeunix.thecave.buddi.view.menu.items.EditEditAccountTypes;
import org.homeunix.thecave.buddi.view.menu.items.EditEditAllTransactions;
import org.homeunix.thecave.buddi.view.menu.items.EditEditScheduledTransactions;
import org.homeunix.thecave.buddi.view.menu.items.EditEditTransactions;
import org.homeunix.thecave.buddi.view.menu.items.EditModifyAccount;
import org.homeunix.thecave.buddi.view.menu.items.EditModifyBudgetCategory;
import org.homeunix.thecave.buddi.view.menu.items.EditNewAccount;
import org.homeunix.thecave.buddi.view.menu.items.EditNewBudgetCategory;
import org.homeunix.thecave.buddi.view.menu.items.EditPreferences;
import org.homeunix.thecave.buddi.view.menu.items.EditReconcileAndAdvanceTransaction;
import org.homeunix.thecave.buddi.view.menu.items.EditRecordTransaction;
import org.homeunix.thecave.buddi.view.menu.items.EditUndeleteAccount;
import org.homeunix.thecave.buddi.view.menu.items.EditUndeleteBudgetCategory;

import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenu;

public class EditMenu extends MossMenu {
	public static final long serialVersionUID = 0;
	
	public EditMenu(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT));
		
	}
	
	@Override
	public void updateMenus() {
		this.removeAll();
		
		if (getFrame() instanceof MainFrame){
			this.setEnabled(true);
			MainFrame frame = (MainFrame) getFrame();
			
			if (frame.isMyAccountsTabSelected()){
				this.add(new EditNewAccount(frame));
				this.add(new EditModifyAccount(frame));
				this.add(new EditDeleteAccount(frame));
				this.add(new EditUndeleteAccount(frame));
				this.addSeparator();
				this.add(new EditEditAccountTypes(frame));
				this.addSeparator();
				this.add(new EditEditTransactions(frame));
				this.add(new EditEditAllTransactions(frame));
			}
			else if (frame.isMyBudgetTabSelected()){
				this.add(new EditNewBudgetCategory(frame));
				this.add(new EditModifyBudgetCategory(frame));
				this.add(new EditDeleteBudgetCategory(frame));
				this.add(new EditUndeleteBudgetCategory(frame));
				this.addSeparator();
				this.add(new EditCopyBudgetCategoryValuesToPrevious(frame));
				this.add(new EditCopyBudgetCategoryValuesToNext(frame));
				this.addSeparator();
				this.add(new EditEditTransactions(frame));
				this.add(new EditEditAllTransactions(frame));
			}

			this.add(new EditEditScheduledTransactions(frame));
			
			if (!OperatingSystemUtil.isMac()){
				this.addSeparator();
				this.add(new EditPreferences(frame));
			}
		}
		else if (getFrame() instanceof TransactionFrame){
			this.setEnabled(true);
			TransactionFrame frame = (TransactionFrame) getFrame();
			this.add(new EditRecordTransaction(frame));
			this.add(new EditClearTransaction(frame));
			this.add(new EditDeleteTransaction(frame));
			if (PrefsModel.getInstance().isShowReconciled() || PrefsModel.getInstance().isShowCleared())
				this.addSeparator();
			if (PrefsModel.getInstance().isShowReconciled())
				this.add(new EditReconcileAndAdvanceTransaction(frame));
			if (PrefsModel.getInstance().isShowCleared())
				this.add(new EditClearAndAdvanceTransaction(frame));			
			if (!OperatingSystemUtil.isMac()){
				this.addSeparator();
				this.add(new EditPreferences(frame));
			}
		}
		else {
			this.setVisible(false);
		}
		
		super.updateMenus();
	}
}
