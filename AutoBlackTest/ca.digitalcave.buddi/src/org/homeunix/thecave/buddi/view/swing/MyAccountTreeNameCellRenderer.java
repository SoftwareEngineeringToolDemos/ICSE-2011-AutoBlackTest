/*
 * Created on Aug 5, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

public class MyAccountTreeNameCellRenderer extends DefaultTreeCellRenderer {
	public static final long serialVersionUID = 0;


	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		this.setIcon(null);
		
		if (value instanceof Account){
			Account a = (Account) value;
			this.setText("<html>" 
					+ TextFormatter.getDeletedWrapper(TextFormatter.getFormattedNameForAccount(a), a)
					+ "</html>");
		}
		else if (value instanceof AccountType){
			AccountType t = (AccountType) value;
			this.setText("<html>" + TextFormatter.getFormattedNameForType(t) + "</html>");
		}		
		else
			this.setText("");

		return this;
	}
}
