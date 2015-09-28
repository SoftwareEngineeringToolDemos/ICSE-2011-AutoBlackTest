/*
 * Created on Aug 4, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;

public class ScheduledTransactionTableModel extends AbstractTableModel {
	public static final long serialVersionUID = 0;

	private final Document model;
	private int selectedIndex = -1;

	//We modify this list in memory.  When we hit Done, we save the changes.  Cancel will discard.
//	private final List<ScheduledTransaction> unsavedScheduledTransactions;
//	private final CompositeList<ScheduledTransaction> allSchedules;
	
	public ScheduledTransactionTableModel(Document model) {
		this.model = model;
		
//		unsavedScheduledTransactions = new LinkedList<ScheduledTransaction>();
//		allSchedules = new CompositeList<ScheduledTransaction>(
//				true, 
//				model.getScheduledTransactions(), 
//				unsavedScheduledTransactions);
	}

	public int getColumnCount() {
		return 1;
	}
	
	@Override
	public String getColumnName(int column) {
		return null;
	}
	
	public int getRowCount() {
		return getScheduledTransactios().size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == -1)
			return getScheduledTransactios().get(rowIndex);
		return getScheduledTransactios().get(rowIndex).getScheduleName();
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		ScheduledTransaction s = getScheduledTransactios().get(rowIndex);
		try {
			s.setScheduleName(aValue.toString());
		}
		catch (InvalidValueException ive){}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	public void add(ScheduledTransaction s){
		try {
			model.addScheduledTransaction(s);
		}
		catch (ModelException me){}
		
		fireTableChanged();
	}
	
	public void remove(ScheduledTransaction s){
//		if (unsavedScheduledTransactions.contains(s))
//			unsavedScheduledTransactions.remove(s);
//		else 
		try {
			model.removeScheduledTransaction(s);
		}
		catch (ModelException me){}

	}
	
//	public void save(){
//		for (ScheduledTransaction s : unsavedScheduledTransactions) {
//			model.addScheduledTransaction(s);
//		}
//	}
	
	public List<ScheduledTransaction> getScheduledTransactios(){
		return model.getScheduledTransactions();
	}
	
	public void fireTableChanged(){
		fireTableStructureChanged();
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedScheduedTransaction(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
}
