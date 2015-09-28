/*
 * Created on Aug 10, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import java.util.HashMap;
import java.util.Map;

import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.Transaction;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;

public class AutoCompleteEntryModel {
	private final Document model;
	private final DocumentChangeListener listener;
	private final Map<String, AutoCompleteEntry> autoCompleteEntries;
	
	public AutoCompleteEntryModel(Document model) {
		this.model = model;
		this.autoCompleteEntries = new HashMap<String, AutoCompleteEntry>();
		updateAutoCompleteList();
		listener = new DocumentChangeListener(){
			public void documentChange(DocumentChangeEvent event) {
				updateAutoCompleteList();
			}
		};
		model.addDocumentChangeListener(listener);
	}

	private void updateAutoCompleteList(){
		for (Transaction t : model.getTransactions()) {
			autoCompleteEntries.put(t.getDescription(), new AutoCompleteEntry(t.getAmount(), t.getFrom(), t.getTo()));
		}
	}
	
	public AutoCompleteEntry getEntry(String description){
		return autoCompleteEntries.get(description);
	}
	
	public static class AutoCompleteEntry {
		private long amount;
		private Source from;
		private Source to;

		public AutoCompleteEntry(long amount, Source from, Source to) {
			this.amount = amount;
			this.from = from;
			this.to = to;
		}
		
		public long getAmount() {
			return amount;
		}
		public void setAmount(long amount) {
			this.amount = amount;
		}
		public Source getFrom() {
			return from;
		}
		public void setFrom(Source from) {
			this.from = from;
		}
		public Source getTo() {
			return to;
		}
		public void setTo(Source to) {
			this.to = to;
		}	
	}
}
