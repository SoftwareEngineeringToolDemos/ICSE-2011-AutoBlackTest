/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.AboutFrame;

import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class HelpAbout extends MossMenuItem {
	public static final long serialVersionUID = 0;

	public HelpAbout(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_HELP_ABOUT));
	}

	public void actionPerformed(ActionEvent e) {
		try {
			new AboutFrame().openWindow();
		}
		catch (WindowOpenException foe){}
	}
}
