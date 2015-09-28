/*
 * Created on Aug 5, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.FilteredLists;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.Formatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;

public class MyAccountTableAmountCellRenderer extends DefaultTableCellRenderer {
	public static final long serialVersionUID = 0;
	
	private final Document document;
	
	public MyAccountTableAmountCellRenderer(Document document) {
		this.document = document;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value instanceof Account){
			Account a = (Account) value;
			if (column == 1){
				this.setText(TextFormatter.getHtmlWrapper(
						TextFormatter.getDeletedWrapper(
								TextFormatter.getFormattedCurrency(
										a.getBalance(), 
										InternalFormatter.isRed(a, a.getBalance()), 
										a.getAccountType().isCredit()
								), a)));
			}
			else if (column == 2 || column == 3){
				this.setText("");

				if (column == 2 && a.getAccountType().isCredit() && PrefsModel.getInstance().isShowCreditRemaining()){
					long availableFunds = (a.getBalance() + a.getOverdraftCreditLimit()) * -1;
					this.setText(TextFormatter.getHtmlWrapper(
							TextFormatter.getDeletedWrapper(
									TextFormatter.getFormattedCurrency(
											availableFunds, 
											InternalFormatter.isRed(a, availableFunds), 
											a.getAccountType().isCredit()
									), a)));
				}
				else if (column == 2 && !a.getAccountType().isCredit() && PrefsModel.getInstance().isShowOverdraft()){
					long availableFunds = a.getBalance() + a.getOverdraftCreditLimit();
					this.setText(TextFormatter.getHtmlWrapper(
							TextFormatter.getDeletedWrapper(
									TextFormatter.getFormattedCurrency(
											availableFunds, 
											InternalFormatter.isRed(a, availableFunds), 
											a.getAccountType().isCredit()
									), a)));
				}
				else if (PrefsModel.getInstance().isShowInterestRates()){
					long interestRate = a.getInterestRate();
					if (interestRate != 0){
						this.setText(TextFormatter.getHtmlWrapper(
								TextFormatter.getDeletedWrapper(
										Formatter.getDecimalFormat(3).format((double) interestRate / 1000.0)
										+ "%", 
										a)));
					}
				}
			}
		}
		if (value instanceof AccountType){
			if (column == 1){
				AccountType t = (AccountType) value;
				long amount = 0;
				for (Account a : new FilteredLists.AccountListFilteredByType(document, document.getAccounts(), t)) {
					if (!a.isDeleted())
						amount += a.getBalance();
				}
				this.setText(TextFormatter.getHtmlWrapper(
						TextFormatter.getFormattedCurrency(
								amount, 
								InternalFormatter.isRed(t, amount), 
								t.isCredit())));
			}
			else {
				this.setText("");
			}
		}


		if (hasFocus && isSelected) {
			table.editCellAt(row,column);
			table.getCellEditor(row, column).getTableCellEditorComponent(table, value, isSelected, row, column).requestFocus();
		}

		return this;
	}
}
