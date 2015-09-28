/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossMenuItem;

public class ViewRollAllBudgetCategories extends MossMenuItem{
	public static final long serialVersionUID = 0;
	
	public ViewRollAllBudgetCategories(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_EDIT_ROLL_ALL_BUDGET_CATEGORIES));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!(getFrame() instanceof MainFrame))
			throw new RuntimeException("Calling frame not instance of MainFrame");
			
		for (BudgetCategory bc : ((Document) ((MainFrame) getFrame()).getDocument()).getBudgetCategories()) {
			bc.setExpanded(false);
		}
		
		((MainFrame) getFrame()).updateContent();
	}
}
