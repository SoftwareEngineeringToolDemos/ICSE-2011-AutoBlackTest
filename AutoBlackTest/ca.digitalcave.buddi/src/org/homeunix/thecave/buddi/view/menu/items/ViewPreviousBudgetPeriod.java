/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossMenuItem;

public class ViewPreviousBudgetPeriod extends MossMenuItem{
	public static final long serialVersionUID = 0;

	public ViewPreviousBudgetPeriod(MainFrame frame) {
		super(frame, TextFormatter.getTranslation(MenuKeys.MENU_VIEW_PREVIOUS_BUDGET_PERIOD),
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		((MainFrame) getFrame()).getMyBudgetPanel().setPreviousPeriod();
	}
}
