/*
 * Created on Jul 29, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.Map;

import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ModelObject;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

/**
 * Default implementation of an AccountType.  You should not create this object directly; 
 * instead, please use the ModelFactory to create it, as this will ensure that all
 * required fields are correctly set.
 * @author wyatt
 *
 */
public class AccountTypeImpl extends ModelObjectImpl implements AccountType {
	private String name;
	private boolean credit;
	private boolean isExpanded;

	public boolean isCredit() {
		return credit;
	}
	public void setCredit(boolean credit) {
		this.credit = credit;
		setChanged();
	}
	public String getName() {
		return TextFormatter.getTranslation(name);
	}
	public void setName(String name) throws InvalidValueException {
		if (getDocument() != null){
			for (AccountType at : ((Document) getDocument()).getAccountTypes()) {
				if (at.getName().equals(name) && !at.equals(this))
					throw new InvalidValueException("The budget category name must be unique");
			}
		}
		this.name = name;
		setChanged();
	}
	public boolean isExpanded() {
		return isExpanded;
	}
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	@Override
	public int compareTo(ModelObject o) {
		if (o instanceof AccountTypeImpl){
			AccountTypeImpl t = (AccountTypeImpl) o;
			if (this.isCredit() != t.isCredit()){
				if (t.isCredit())
					return -1;
				return 1;
			}
			
			return this.getName().compareTo(t.getName());
		}
		return super.compareTo(o);
	}
	
	AccountType clone(Map<ModelObject, ModelObject> originalToCloneMap)
			throws CloneNotSupportedException {

		if (originalToCloneMap.get(this) != null)
			return (AccountType) originalToCloneMap.get(this);
		
		AccountTypeImpl a = new AccountTypeImpl();
		a.document = (Document) originalToCloneMap.get(document);
		a.name = name;
		a.credit = credit;
		a.isExpanded = isExpanded;
		
		originalToCloneMap.put(this, a);
		
		return a;
	}
}
