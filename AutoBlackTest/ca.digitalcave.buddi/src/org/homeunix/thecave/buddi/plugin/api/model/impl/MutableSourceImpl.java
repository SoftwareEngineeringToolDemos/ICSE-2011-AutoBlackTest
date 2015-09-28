/*
 * Created on Aug 23, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.model.MutableSource;

public abstract class MutableSourceImpl extends MutableModelObjectImpl implements MutableSource {

	public MutableSourceImpl(Source source) {
		super(source);
	}

	public void setDeleted(boolean deleted) throws InvalidValueException{
		getSource().setDeleted(deleted);
		
	}

	public void setName(String name) throws InvalidValueException{
		getSource().setName(name);
	}

	public void setNotes(String notes) throws InvalidValueException{
		getSource().setNotes(notes);
	}

	public Source getSource(){
		return (Source) getRaw(); 
	}
	public boolean isDeleted() {
		return getSource().isDeleted();
	}
	public String getName() {
		return getSource().getName();
	}
	public String getFullName() {
		return getSource().getFullName();
	}
	public String getNotes() {
		return getSource().getNotes();
	}
	@Override
	public String toString() {
		return getFullName();
	}
}
