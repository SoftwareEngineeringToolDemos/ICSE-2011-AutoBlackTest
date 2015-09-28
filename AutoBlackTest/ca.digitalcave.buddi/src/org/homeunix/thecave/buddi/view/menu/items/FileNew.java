/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class FileNew extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public FileNew(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE_NEW),
				KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			MainFrame mainFrame = new MainFrame(ModelFactory.createDocument());
			mainFrame.openWindow(
					PrefsModel.getInstance().getWindowSize(mainFrame.getDocument().getFile() + ""), 
					PrefsModel.getInstance().getWindowLocation(mainFrame.getDocument().getFile() + ""));
		}
		catch (ModelException me){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Model Exception", me);
		}
		catch (WindowOpenException foe){}
	}
}
