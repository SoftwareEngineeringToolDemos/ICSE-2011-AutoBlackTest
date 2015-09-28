/*
 * Created on Aug 10, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.swing;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Transaction;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;

public class DescriptionList extends AbstractList<String> {
	private static final long serialVersionUID = 0; 

	private Document model;
	private final DocumentChangeListener listener;
	private final List<String> descriptionList;
	private final Set<String> descriptions = new HashSet<String>();

	public DescriptionList(Document model) {
		this.model = model;
		this.descriptionList = new ArrayList<String>();
		updateAutoCompleteList();

		listener = new DocumentChangeListener(){
			public void documentChange(DocumentChangeEvent event) {
				updateAutoCompleteList();				
			}
		};
		model.addDocumentChangeListener(listener);
	}

	@Override
	public String get(int index) {
		return descriptionList.get(index);
	}

	@Override
	public int size() {
		return descriptionList.size();
	}

	private void updateAutoCompleteList(){
		descriptions.clear();
		for (Transaction t : model.getTransactions()) {
			descriptions.add(t.getDescription());
		}

		descriptionList.clear();
		descriptionList.addAll(descriptions);
		Collections.sort(descriptionList);
	}
}
