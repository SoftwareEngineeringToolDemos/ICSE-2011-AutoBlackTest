/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

import org.homeunix.thecave.buddi.util.Formatter;

public class MaxLengthListCellRenderer extends DefaultListCellRenderer {
	public static final long serialVersionUID = 0;

	private final JComponent component;

	private int computedLength = -1;

	public MaxLengthListCellRenderer(JComponent component) {
		this.component = component;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value != null){
			String s = value.toString();
			if (index == -1 && s.length() > computedLength && component != null && component.getGraphics() != null)
				//The width - 55 is obtained by trial and error on the Mac.  In general, the Mac LnF
				// has more padding than most others, so this should be more than enough for others.
				// This offset prevents the combo box button from interfering with the display
				// of oversized values, by making the value wrap at the word level, and remove the
				// "..." at the end of long values.
				this.setText(Formatter.getStringToLength(s, component.getWidth() - 55, component.getGraphics().getFontMetrics()));
			else
				this.setText(s);
		}
		else {
			this.setText(" ");
		}

		return this;
	}
}
