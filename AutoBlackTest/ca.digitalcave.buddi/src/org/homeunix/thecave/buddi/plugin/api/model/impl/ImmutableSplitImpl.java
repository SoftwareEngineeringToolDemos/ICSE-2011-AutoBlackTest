/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import org.homeunix.thecave.buddi.model.Split;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSplit;

public class ImmutableSplitImpl extends ImmutableSourceImpl implements ImmutableSplit {
	
	public ImmutableSplitImpl(Split split) {
		super(split);
	}
}
