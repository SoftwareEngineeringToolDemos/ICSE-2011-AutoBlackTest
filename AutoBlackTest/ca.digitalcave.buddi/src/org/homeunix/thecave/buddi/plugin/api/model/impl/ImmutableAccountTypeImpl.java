/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.api.model.impl;

import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccountType;

public class ImmutableAccountTypeImpl extends ImmutableModelObjectImpl implements ImmutableAccountType {
	
	public ImmutableAccountTypeImpl(AccountType type) {
		super(type);
	}
	
	public AccountType getType(){
		return (AccountType) getRaw();
	}
	
	public String getName(){
		return getType().getName();
	}
	
	public boolean isCredit(){
		return getType().isCredit();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
