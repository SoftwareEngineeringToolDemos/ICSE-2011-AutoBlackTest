/*
 * Created on Aug 5, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;

public class MyBudgetTableAmountCellRenderer extends DefaultTableCellRenderer {
	public static final long serialVersionUID = 0;

	private final StringBuilder sb = new StringBuilder(); 
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value instanceof Object[]){
			Object[] values = (Object[]) value;

			if (sb.length() > 0)
				sb.delete(0, sb.length());
			

			if ((Long) values[1] == 0 && (Long) values[2] == 0){
				//To make the table easier to read, we don't include $0.00 in it; we use --- instead.
				sb.append("---");
			}
			else {
				//Display the amount for this budget category
				TextFormatter.appendFormattedCurrency(sb, (Long) values[1], 
						InternalFormatter.isRed((BudgetCategory) values[0], (Long) values[1]), false);
				
				//If there is anything in sub categories, add it in brackets.
				if (!((Long) values[2]).equals((Long) values[1]) && (Long) values[2] != 0){
					sb.append(" (");
					TextFormatter.appendFormattedCurrency(sb,
							(Long) values[2], 
							InternalFormatter.isRed((BudgetCategory) values[0], (Long) values[2]),
							false);
					sb.append(")");
				}
			}

			sb.append(" / ");
			final Long actual = (Long) values[4];
			if(actual != 0) {
				TextFormatter.appendFormattedCurrency(sb, actual, actual < 0, actual < 0);
			} else {
				sb.append("---");
			}

			final Long actualIncludingSubs = (Long) values[5];
			if(actualIncludingSubs != (long)actual) {
				sb.append(" (");
				TextFormatter.appendFormattedCurrency(sb, actualIncludingSubs, actualIncludingSubs < 0,
						actualIncludingSubs < 0);
				sb.append(")");
			}
			
			for (int i = 0; i < ((Integer) values[3]); i++){
				sb.insert(0, "&nbsp&nbsp&nbsp "); 
			}
			
			sb.insert(0, "<html>");
			sb.append("</html>");
			this.setText(sb.toString());
		}

		if (hasFocus && isSelected) {
			table.editCellAt(row,column);
			table.getCellEditor(row, column).getTableCellEditorComponent(table, value, isSelected, row, column).requestFocus();
		}

		return this;
	}
}
