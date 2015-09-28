/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.Formatter;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossMenuItem;

public class ViewDataFileStats extends MossMenuItem{
	public static final long serialVersionUID = 0;
	
	public ViewDataFileStats(MainFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_VIEW_DATA_FILE_STATS));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Document model = (Document) ((MainFrame) getFrame()).getDocument();
		JOptionPane.showMessageDialog(
				getFrame(), 
				"<html><h3>" + 
				TextFormatter.getTranslation(BuddiKeys.DATA_FILE_STATS) +
				"</h3><p>" +
				TextFormatter.getTranslation(BuddiKeys.DATA_FILE_STATS_ACCOUNTS) + " " +
				Formatter.getDecimalFormat(0).format(model.getAccounts().size()) +
				"</p><p>" +
				TextFormatter.getTranslation(BuddiKeys.DATA_FILE_STATS_BUDGET_CATEGORIES) + " " +
				Formatter.getDecimalFormat(0).format(model.getBudgetCategories().size()) +
				"</p><p>" +
				TextFormatter.getTranslation(BuddiKeys.DATA_FILE_STATS_TRANSACTIONS) + " " +
				Formatter.getDecimalFormat(0).format(model.getTransactions().size()) +
				"</p><p>" +
				TextFormatter.getTranslation(BuddiKeys.DATA_FILE_STATS_SCHEDULED_TRANSACTIONS) + " " +
				Formatter.getDecimalFormat(0).format(model.getScheduledTransactions().size()) +
				"</p></html>", 
				TextFormatter.getTranslation(BuddiKeys.DATA_FILE_STATS_TITLE), 
				JOptionPane.INFORMATION_MESSAGE);
	}
}
