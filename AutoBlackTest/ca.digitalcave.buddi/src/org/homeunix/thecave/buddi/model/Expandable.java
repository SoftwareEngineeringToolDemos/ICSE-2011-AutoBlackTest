/*
 * Created on Aug 31, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model;

/**
 * An interface which specifes the state of tree elements.  Objects which 
 * can be expanded on a tree (such as AccountType and BudgetCategory)
 * should implement this interface.
 * 
 * @author wyatt
 *
 */
public interface Expandable {
	/**
	 * Is the node currently expanded
	 * @return
	 */
	public boolean isExpanded();
	/**
	 * Should the node be expanded
	 * @param isExpanded
	 */
	public void setExpanded(boolean isExpanded);	
}
