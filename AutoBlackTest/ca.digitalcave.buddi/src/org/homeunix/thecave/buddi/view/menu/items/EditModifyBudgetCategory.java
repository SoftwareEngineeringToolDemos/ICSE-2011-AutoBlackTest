/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.dialogs.BudgetCategoryEditorDialog;

import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class EditModifyBudgetCategory extends MossMenuItem{
	public static final long serialVersionUID = 0;

	public EditModifyBudgetCategory(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_MODIFY_BUDGET_CATEGORIES),
				KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (BudgetCategory bc : ((MainFrame) getFrame()).getSelectedBudgetCategories()) {
			BudgetCategoryEditorDialog editor = new BudgetCategoryEditorDialog((MainFrame) getFrame(), (Document) ((MainFrame) getFrame()).getDocument(), bc, null);
			try {
				editor.openWindow();
			}
			catch (WindowOpenException woe){}
		}

		((MainFrame) getFrame()).fireStructureChanged();
		((MainFrame) getFrame()).updateContent();
		
		//We add this to the end of the AWT event thread to allow the 
		// structure change to register first.  Otherwise, the tree
		// will not unroll again.
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				((MainFrame) getFrame()).updateContent();
			}
		});
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();
		
		this.setEnabled(((MainFrame) getFrame()).getSelectedBudgetCategories().size() > 0);
	}
}
