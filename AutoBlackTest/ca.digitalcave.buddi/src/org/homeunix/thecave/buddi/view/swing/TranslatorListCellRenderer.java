/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.homeunix.thecave.buddi.model.prefs.PrefsModel;

public class TranslatorListCellRenderer extends DefaultListCellRenderer {
	public static final long serialVersionUID = 0;
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if (value != null)
			this.setText(PrefsModel.getInstance().getTranslator().get(value.toString()));
		else
			this.setText("");
		
		return this;
	}
}
