/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossMenuItem;

public class EditUndeleteBudgetCategory extends MossMenuItem{
	public static final long serialVersionUID = 0;
	
	public EditUndeleteBudgetCategory(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_UNDELETE_BUDGET_CATEGORIES));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!(getFrame() instanceof MainFrame))
			throw new RuntimeException("Calling frame not instance of BudgetFrame");
			
		for (BudgetCategory bc : ((MainFrame) getFrame()).getSelectedBudgetCategories()) {
			try {
				bc.setDeleted(false);
			}
			catch (InvalidValueException ive){
				Logger.getLogger(this.getClass().getName()).warning("Error setting deleted flag to false on budget category");
			}
		}
		
		((MainFrame) getFrame()).updateContent();
		((MainFrame) getFrame()).fireStructureChanged();
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();
		
		this.setEnabled(((MainFrame) getFrame()).getSelectedBudgetCategories().size() > 0);
		
		this.setVisible(PrefsModel.getInstance().isShowDeleted());
	}
}
