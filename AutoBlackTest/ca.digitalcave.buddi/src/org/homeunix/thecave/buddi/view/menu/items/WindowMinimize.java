/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;

import ca.digitalcave.moss.osx.HiddenMossFrame;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;

public class WindowMinimize extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public WindowMinimize(MossFrame frame) {
		super(frame, 
				PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_WINDOW_MINIMIZE),
				KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	public void actionPerformed(ActionEvent e) {
		getFrame().setState(MossFrame.ICONIFIED);
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();

		this.setEnabled(!(getFrame() instanceof HiddenMossFrame));
	}
}
