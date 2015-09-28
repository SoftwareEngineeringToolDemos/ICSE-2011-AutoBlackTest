/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.dialogs.BudgetCategoryEditorDialog;

import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class EditNewBudgetCategory extends MossMenuItem{
	public static final long serialVersionUID = 0;

	public EditNewBudgetCategory(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_NEW_BUDGET_CATEGORY),
				KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BudgetCategory bc = null;
		
		if (((MainFrame) getFrame()).getSelectedBudgetCategories().size() > 0)
			bc = ((MainFrame) getFrame()).getSelectedBudgetCategories().get(0);
		
		BudgetCategoryEditorDialog editor = new BudgetCategoryEditorDialog((MainFrame) getFrame(), (Document) ((MainFrame) getFrame()).getDocument(), null, bc);
		try {
			editor.openWindow();
		}
		catch (WindowOpenException woe){}
		
		((MainFrame) getFrame()).fireStructureChanged();
		((MainFrame) getFrame()).updateContent();
	}
}
