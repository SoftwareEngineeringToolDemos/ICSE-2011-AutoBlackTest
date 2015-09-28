/*
 * Created on Aug 9, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.impl.FilteredLists;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;

public class SourceComboBoxModel implements ComboBoxModel {
	private static final long serialVersionUID = 0; 

	private final Document model;
	private final boolean includeIncome;
	private final boolean includeSplit; //True for calls from transaction editor; false for calls from split editor.
	
	private final Source ignoredSource; //An array of sources which will not be added to the list. 
	
	private final List<Account> accounts = new ArrayList<Account>();
	private final List<BudgetCategory> budgetCategories = new ArrayList<BudgetCategory>();
	
	private final Map<Object, Integer> levelMappings = new HashMap<Object, Integer>();
	
	private final DefaultComboBoxModel comboBoxModel;
	
	private final DocumentChangeListener listener;

	public SourceComboBoxModel(Document model, boolean includeIncome, boolean includeSplit, Source ignoredSource) {
		this.model = model;
		this.includeIncome = includeIncome;
		this.includeSplit = includeSplit;
		this.comboBoxModel = new DefaultComboBoxModel();
		this.ignoredSource = ignoredSource;
		updateComboBoxModel(null);
		listener = new DocumentChangeListener(){
			public void documentChange(DocumentChangeEvent event) {
				updateComboBoxModel(null);	
			}
		};
		model.addDocumentChangeListener(listener);
	}

	public Object getSelectedItem() {
		return getComboBoxModel().getSelectedItem();
	}

	public void setSelectedItem(Object anItem) {
		getComboBoxModel().setSelectedItem(anItem);
	}

	public void addListDataListener(ListDataListener l) {
		getComboBoxModel().addListDataListener(l);
	}

	public Object getElementAt(int index) {
		return getComboBoxModel().getElementAt(index);
	}

	public int getSize() {
		return getComboBoxModel().getSize();
	}

	public void removeListDataListener(ListDataListener l) {
		getComboBoxModel().removeListDataListener(l);
	}
	
	private DefaultComboBoxModel getComboBoxModel(){
		return comboBoxModel;
	}
	
	/**
	 * Returns true if the given source is included in this 
	 * combo box model, and false otherwise.
	 * @param source
	 * @return
	 */
	public boolean contains(Source source){
		return (accounts.contains(source) || budgetCategories.contains(source));
	}
	
	public int getLevel(Object item){
		if (levelMappings.containsKey(item))
			return levelMappings.get(item);
		return 0;
	}
	
	/**
	 * Updates the model, and optionally (if source is not null) forces it to include
	 * the given source.  This is used to ensure that deleted accounts still show up 
	 * for transactions which link to them, but not for others.
	 * @param source
	 */
	public void updateComboBoxModel(Source source){
		Object selected = getSelectedItem(); 
		
		accounts.clear();
		
		for (Account a : model.getAccounts()) {
			if (!a.isDeleted() || PrefsModel.getInstance().isShowDeleted() || a.equals(source)){
				if (!a.equals(ignoredSource))
					accounts.add(a);
			}
		}
		budgetCategories.clear();
		for (BudgetCategory bc : model.getBudgetCategories()) {
			if (!bc.isDeleted() || PrefsModel.getInstance().isShowDeleted() || bc.equals(source)){
				if (!bc.equals(ignoredSource))
					budgetCategories.add(bc);
			}
		}
		
		Collections.sort(accounts);
		Collections.sort(budgetCategories);		
		
		getComboBoxModel().removeAllElements();
		getComboBoxModel().addElement(TextFormatter.getTranslation(BuddiKeys.ACCOUNTS_COMBOBOX_HEADER));
		for (Account a : accounts) {
			getComboBoxModel().addElement(a);
		}
		getComboBoxModel().addElement("&nbsp;");
		getComboBoxModel().addElement(TextFormatter.getTranslation(BuddiKeys.BUDGET_CATEGORIES_COMBOBOX_HEADER));

		addBudgetCategories(null, 0);
		
		if (includeSplit){
			getComboBoxModel().addElement("&nbsp;");
			getComboBoxModel().addElement(BuddiKeys.SPLITS.toString());
		}
		
		setSelectedItem(selected);
	}
	
	private void addBudgetCategories(BudgetCategory parent, int level){
		final List<BudgetCategory> siblings;
		if (PrefsModel.getInstance().isShowFlatBudgetInSourceCombobox()) {
			siblings = budgetCategories;
		}
		else {
			siblings = new FilteredLists.BudgetCategoryListFilteredByParent(model, budgetCategories, parent);
		}
		for (BudgetCategory bc : siblings) {
			if (bc.isIncome() == includeIncome){
				getComboBoxModel().addElement(bc);
				levelMappings.put(bc, level);
				if (!PrefsModel.getInstance().isShowFlatBudgetInSourceCombobox()){
					addBudgetCategories(bc, level + 1);
				}
			}
		}		
	}
}