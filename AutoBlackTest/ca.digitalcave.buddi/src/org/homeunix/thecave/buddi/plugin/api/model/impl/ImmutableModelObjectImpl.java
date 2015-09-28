/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import java.util.Date;

import org.homeunix.thecave.buddi.model.ModelObject;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableModelObject;

public abstract class ImmutableModelObjectImpl implements ImmutableModelObject {
	private ModelObject raw;
	
	public ImmutableModelObjectImpl(ModelObject raw) {
		this.raw = raw;
	}
	
	public ModelObject getRaw(){
		return raw;
	}
	
	public int compareTo(ImmutableModelObject o) {
		return this.getRaw().compareTo(o.getRaw());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImmutableModelObject)
			return getRaw().equals(((ImmutableModelObject) obj).getRaw());
		return false;
	}
	
	@Override
	public int hashCode() {
		return getRaw().hashCode();
	}
	
	/**
	 * Returns the UID string for this object.
	 * @return
	 */
	public String getUid(){
		return getRaw().getUid();
	}
	
	public Date getModified() {
		return getRaw().getModified();
	}
	
	@Override
	public String toString() {
		if (getRaw() != null)
			return getRaw().toString();
		return "null";
	}
}
