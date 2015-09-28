/*
 * Created on Aug 4, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import java.util.List;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.FilteredLists;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class MyAccountTreeTableModel extends AbstractTreeTableModel {

	private final Document model;
	private final Object root;

	public MyAccountTreeTableModel(Document model) {
		super(new Object());
		this.model = model;
		this.root = getRoot();		
	}

	public int getColumnCount() {
		int columns = 2;
		if (PrefsModel.getInstance().isShowOverdraft() || PrefsModel.getInstance().isShowCreditRemaining())
			columns++;
		if (PrefsModel.getInstance().isShowInterestRates())
			columns++;
		return columns;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return PrefsModel.getInstance().getTranslator().get(BuddiKeys.ACCOUNT);
		if (column == 1)
			return PrefsModel.getInstance().getTranslator().get(BuddiKeys.AMOUNT);
		
		//Depending on which of overdraft/credit and interest options are enabled, we
		// may have 3 or 4 columns.  If overdraft/credit is enabled, column index 2 is
		// used for it; otherwise, it is used for interest rate.
		if (column == 2){
			if (PrefsModel.getInstance().isShowOverdraft() || PrefsModel.getInstance().isShowCreditRemaining())
				return PrefsModel.getInstance().getTranslator().get(BuddiKeys.AVAILABLE_FUNDS);
			else
				return PrefsModel.getInstance().getTranslator().get(BuddiKeys.INTEREST_RATE);	
		}
		//If there is a column index 3, it will always be for interest rate.
		if (column == 3)
			return PrefsModel.getInstance().getTranslator().get(BuddiKeys.INTEREST_RATE);
		return "";
	}

	public Object getValueAt(Object node, int column) {
		return node;
	}

	public Object getChild(Object parent, int childIndex) {
		if (parent.equals(root)){
			if (PrefsModel.getInstance().isShowFlatAccounts()){
				return new FilteredLists.AccountListFilteredByDeleted(model, model.getAccounts()).get(childIndex);
			}
			else{
				List<AccountType> types = new FilteredLists.TypeListFilteredByAccounts(model);
				if (childIndex < types.size())
					return types.get(childIndex);
			}
		}
		if (parent instanceof AccountType){
			List<Account> accounts = new FilteredLists.AccountListFilteredByDeleted(model, new FilteredLists.AccountListFilteredByType(model, model.getAccounts(), (AccountType) parent));
			if (childIndex < accounts.size())
				return accounts.get(childIndex);
		}
		return null;
	}

	public int getChildCount(Object parent) {
		if (parent.equals(root)){
			if (PrefsModel.getInstance().isShowFlatAccounts()){
				return new FilteredLists.AccountListFilteredByDeleted(model, model.getAccounts()).size();
			}
			else{
				List<AccountType> types = new FilteredLists.TypeListFilteredByAccounts(model);
				return types.size();
			}
		}
		if (parent instanceof AccountType){
			List<Account> accounts = new FilteredLists.AccountListFilteredByDeleted(model, new FilteredLists.AccountListFilteredByType(model, model.getAccounts(), (AccountType) parent));
			return accounts.size();
		}

		return 0;
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (parent == null || child == null)
			return -1;

//		if (parent.equals(root) && child instanceof AccountType){
		if (parent.equals(root)){
			if (PrefsModel.getInstance().isShowFlatAccounts()){
				return new FilteredLists.AccountListFilteredByDeleted(model, model.getAccounts()).indexOf(child);
			}
			else{
				List<AccountType> types = new FilteredLists.TypeListFilteredByAccounts(model);
				return types.indexOf(child);
			}
		}

		if (parent instanceof AccountType && child instanceof Account){
			List<Account> accounts = new FilteredLists.AccountListFilteredByDeleted(model, new FilteredLists.AccountListFilteredByType(model, model.getAccounts(), (AccountType) parent));
			return accounts.indexOf(child);
		}
		return -1;
	}

	public void fireStructureChanged(){
		modelSupport.fireNewRoot();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < getChildCount(root); i++) {
			Object o1 = getChild(root, i);
			sb.append(o1).append(" [");
			for (int j = 0; j < getChildCount(o1); j++){
				Object o2 = getChild(o1, j);
				sb.append(o2);
				if (j < (getChildCount(o1) - 1))
					sb.append(", ");
			}
			sb.append("]  ");
		}

		return sb.toString();
	}
}
