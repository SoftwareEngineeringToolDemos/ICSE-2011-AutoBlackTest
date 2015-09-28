/*
 * Created on Jul 29, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.Date;
import java.util.Map;

import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ModelObject;

import ca.digitalcave.moss.application.document.AbstractDocument;

/**
 * The class from which most other model objects descend from.  You should not 
 * instantiate this class directly.
 * 
 * @author wyatt
 *
 */
public abstract class ModelObjectImpl implements ModelObject {
	
	protected Time modifiedTime;
	protected String uid;
	protected Document document;
		
	public void setChanged(){
		setModified(new Time());
		if (document != null)
			document.setChanged();
	}
	public Time getModified() {
		return modifiedTime;
	}
	public void setModified(Time modifiedTime){
		this.modifiedTime = modifiedTime;
	}
	public void setModified(Date modifiedDate) {
		this.modifiedTime = new Time(modifiedDate);
	}
	public String getUid() {
		if (uid == null || uid.length() == 0){
			setUid(AbstractDocument.getGeneratedUid(this));
		}
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
		setChanged();
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
		setChanged();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModelObjectImpl)
			return this.getUid().equals(((ModelObjectImpl) obj).getUid());
		return false;
	}
	
	public int compareTo(ModelObject o) {
		return (getUid().compareTo(o.getUid()));
	}
	
	
	/**
	 * Clones the object.  A cloned object will contain the same values as the original, but will 
	 * not have identical (i.e., same reference) to these values.  The originalToCloneMap allows
	 * references to other original objects to carry over to the new cloned object.  For instance, 
	 * when cloning a transaction, you must also have references to the new cloned sources.
	 * 
	 * When you clone this given object, you must also put a reference to the new object
	 * into the map, to allow future objects to refer to the newly cloned object.
	 * 
	 * If you try to clone an object twice using the same map, it will return the same
	 * object which it originally did.  This allows you to indiscriminantly use the clone
	 * method, even if converting trees or graphs of objects.
	 * 
	 * @param originalToCloneMap
	 * @return
	 * @throws CloneNotSupportedException
	 */
	abstract ModelObject clone(Map<ModelObject, ModelObject> originalToCloneMap) throws CloneNotSupportedException;
}
