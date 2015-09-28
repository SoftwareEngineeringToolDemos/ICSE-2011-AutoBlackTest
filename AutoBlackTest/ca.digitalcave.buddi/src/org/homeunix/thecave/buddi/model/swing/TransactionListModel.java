/*
 * Created on Aug 9, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import org.homeunix.thecave.buddi.i18n.keys.TransactionClearedFilterKeys;
import org.homeunix.thecave.buddi.i18n.keys.TransactionDateFilterKeys;
import org.homeunix.thecave.buddi.i18n.keys.TransactionReconciledFilterKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.impl.FilteredLists;
import org.homeunix.thecave.buddi.model.impl.FilteredLists.TransactionListFilteredBySearch;

import ca.digitalcave.moss.swing.model.BackedListModel;

/**
 * The transaction list model.  Implements List interface to ease navigation,
 * but only the read-only methods are implemented.  
 * 
 * @author wyatt
 *
 */
public class TransactionListModel extends BackedListModel<Transaction> {
	public static final long serialVersionUID = 0;
	
	private final FilteredLists.TransactionListFilteredBySearch transactions;
	
	public TransactionListModel(Document model, Source selectedSource) {
		super(selectedSource == null ? 
				FilteredLists.getTransactionsBySearch(model, selectedSource, model.getTransactions()) :
					FilteredLists.getTransactionsBySearch(model, selectedSource, model.getTransactions(selectedSource)));
		
		this.transactions = (TransactionListFilteredBySearch) listModel;
	}
	
	public void setSearchText(String text){
		transactions.setSearchText(text);
		transactions.updateFilteredList();
		update();
	}
	
	public void setDateFilter(TransactionDateFilterKeys key){
		transactions.setDateFilter(key);
		transactions.updateFilteredList();
		update();
	}
	
	public void setClearedFilter(TransactionClearedFilterKeys key){
		transactions.setClearedFilter(key);
		transactions.updateFilteredList();
		update();
	}
	
	public void setReconciledFilter(TransactionReconciledFilterKeys key){
		transactions.setReconciledFilter(key);
		transactions.updateFilteredList();
		update();
	}
	
	public boolean isListFiltered(){
		return (transactions.isFiltered());
	}
	
	public void update(){
		transactions.updateFilteredList();
		fireContentsChanged(this, -1, -1);
	}
}
