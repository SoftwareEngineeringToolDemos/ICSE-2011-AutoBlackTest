/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.ApplicationModel;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;

public class FileQuit extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public FileQuit(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE_QUIT),
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	public void actionPerformed(ActionEvent e) {
		
		List<MossFrame> frames = ApplicationModel.getInstance().getOpenFrames();
		List<File> openFiles = new LinkedList<File>();
		for (MossFrame frame : frames) {
			if (frame instanceof MainFrame){
				openFiles.add(((MainFrame) frame).getDocument().getFile());
			}
		}
		if (openFiles.size() > 0)
			PrefsModel.getInstance().setLastDataFiles(openFiles);
		
		PrefsModel.getInstance().save();
		
		for (MossFrame frame : ApplicationModel.getInstance().getOpenFrames()) {
			if (!frame.canClose()){
				Logger.getLogger(this.getClass().getName()).finest("Frame " + frame.getTitle() + " refused to quit; cancelling quit request.");
				return;
			}
		}
		
		//We have already checked the windows; if none of them cancelled the request, we can just close.
		while (ApplicationModel.getInstance().getOpenFrames().size() > 0) {
			ApplicationModel.getInstance().getOpenFrames().get(0).closeWindowWithoutPrompting();
		}
		
		System.exit(0);
	}
}
