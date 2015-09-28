/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.bars;

import org.homeunix.thecave.buddi.view.menu.menus.EditMenu;
import org.homeunix.thecave.buddi.view.menu.menus.FileMenu;
import org.homeunix.thecave.buddi.view.menu.menus.HelpMenu;
import org.homeunix.thecave.buddi.view.menu.menus.ViewMenu;
import org.homeunix.thecave.buddi.view.menu.menus.WindowMenu;

import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuBar;

public class BuddiMenuBar extends MossMenuBar {
	public static final long serialVersionUID = 0;

	public BuddiMenuBar(MossFrame frame) {
		super(frame);

		this.add(new FileMenu(frame));
		this.add(new EditMenu(frame));
		this.add(new ViewMenu(frame));
		this.add(new WindowMenu(frame));
		this.add(new HelpMenu(frame));
	}
}
