/*
 * Created on Aug 23, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import org.homeunix.thecave.buddi.model.ModelObject;

public abstract class MutableModelObjectImpl extends ImmutableModelObjectImpl {

	public MutableModelObjectImpl(ModelObject raw) {
		super(raw);
	}
}
