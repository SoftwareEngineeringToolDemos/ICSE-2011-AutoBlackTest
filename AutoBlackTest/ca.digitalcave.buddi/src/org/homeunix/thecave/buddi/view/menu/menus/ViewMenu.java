/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.menus;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.menu.items.ViewDataFileStats;
import org.homeunix.thecave.buddi.view.menu.items.ViewNextBudgetPeriod;
import org.homeunix.thecave.buddi.view.menu.items.ViewPreviousBudgetPeriod;
import org.homeunix.thecave.buddi.view.menu.items.ViewRollAllAccounts;
import org.homeunix.thecave.buddi.view.menu.items.ViewRollAllBudgetCategories;
import org.homeunix.thecave.buddi.view.menu.items.ViewUnrollAllAccounts;
import org.homeunix.thecave.buddi.view.menu.items.ViewUnrollAllBudgetCategories;

import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenu;

public class ViewMenu extends MossMenu {
	public static final long serialVersionUID = 0;
	
	public ViewMenu(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_VIEW));		
	}
	
	@Override
	public void updateMenus() {
		this.removeAll();
		
		if (getFrame() instanceof MainFrame){
			MainFrame frame = (MainFrame) getFrame();
			this.setEnabled(true);
			
			if (frame.isMyAccountsTabSelected()){
				this.add(new ViewUnrollAllAccounts(frame));
				this.add(new ViewRollAllAccounts(frame));
				this.addSeparator();
			}
			else if (frame.isMyBudgetTabSelected()){
				this.add(new ViewUnrollAllBudgetCategories(frame));
				this.add(new ViewRollAllBudgetCategories(frame));
				this.addSeparator();
				this.add(new ViewNextBudgetPeriod(frame));
				this.add(new ViewPreviousBudgetPeriod(frame));				
				this.addSeparator();
			}
			else if (frame.isMyReportsTabSelected()){
				this.setEnabled(false);
			}

			this.add(new ViewDataFileStats(frame));
			this.addSeparator();
			this.add(new ViewPanelsMenu(frame));
		}
		else {
			this.setVisible(false);
		}
		
		super.updateMenus();
	}
}
