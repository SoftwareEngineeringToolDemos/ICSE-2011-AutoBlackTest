/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.swing.MyBudgetTreeTableModel;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossMenuItem;

public class EditCopyBudgetCategoryValuesToNext extends MossMenuItem{
	public static final long serialVersionUID = 0;

	public EditCopyBudgetCategoryValuesToNext(MainFrame frame) {
		super(frame, TextFormatter.getTranslation(MenuKeys.MENU_EDIT_COPY_VALUES_TO_NEXT_BUDGET_PERIOD),
				KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Date currentlySelectedDate = ((MainFrame) getFrame()).getMyBudgetPanel().getTreeTableModel().getSelectedDate();
		for (BudgetCategory bc : ((MainFrame) getFrame()).getBudgetCategoriesInSelectedPeriod()) {
			long amount = bc.getAmount(currentlySelectedDate);
			try {
				if (bc.getAmount(bc.getBudgetPeriodType().getBudgetPeriodOffset(currentlySelectedDate, 1)) == 0)
					bc.setAmount(bc.getBudgetPeriodType().getBudgetPeriodOffset(currentlySelectedDate, 1), amount);
			}
			catch (InvalidValueException ive){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Invalid Value Exception", ive);
			}
		}

		((MainFrame) getFrame()).fireStructureChanged();
		((MainFrame) getFrame()).updateContent();
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();
		
		MyBudgetTreeTableModel treeTableModel = ((MainFrame) getFrame()).getMyBudgetPanel().getTreeTableModel();
		this.setText(TextFormatter.getTranslation(MenuKeys.MENU_EDIT_COPY_VALUES_TO_NEXT_BUDGET_PERIOD) 
				+ " (" + treeTableModel.getColumnName(2) + " "
				+ TextFormatter.getTranslation(BuddiKeys.TO)
				+ " " + treeTableModel.getColumnName(3) + ")");
		
		this.setEnabled(((MainFrame) getFrame()).getBudgetCategoriesInSelectedPeriod().size() > 0);
	}
}
