/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model;

import java.util.Date;

import org.homeunix.thecave.buddi.model.ModelObject;

public interface ImmutableModelObject extends Comparable<ImmutableModelObject> {
	
	/**
	 * Returns the raw (wrapped) model object.  When you use this
	 * object, you lose the safety of using the API.  It is not
	 * recommended to use this method unless you know what you
	 * are doing.
	 * @return
	 */
	public ModelObject getRaw();
	
	/**
	 * Returns the UID string for this object.
	 * @return
	 */
	public String getUid();
	
	/**
	 * Returnst he modified date.
	 * @return
	 */
	public Date getModified();
}
