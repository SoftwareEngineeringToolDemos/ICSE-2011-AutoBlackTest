/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSource;

public abstract class ImmutableSourceImpl extends ImmutableModelObjectImpl implements ImmutableSource {
	
	public ImmutableSourceImpl(Source source) {
		super(source);
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
