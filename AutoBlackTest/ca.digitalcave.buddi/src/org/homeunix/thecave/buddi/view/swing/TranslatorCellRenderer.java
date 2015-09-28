/*
 * Created on Aug 5, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.homeunix.thecave.buddi.model.prefs.PrefsModel;

public class TranslatorCellRenderer extends DefaultTableCellRenderer {
	public static final long serialVersionUID = 0;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		this.setText(PrefsModel.getInstance().getTranslator().get(value.toString()));
		
		return this;
	}
}
