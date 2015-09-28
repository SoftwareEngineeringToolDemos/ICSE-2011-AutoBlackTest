/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.bars;

import ca.digitalcave.moss.osx.Application;



public class FramelessMenuBar extends BuddiMenuBar {
	public static final long serialVersionUID = 0;

	public FramelessMenuBar() {
		super(Application.getApplication().getHiddenFrame());
	}
}
