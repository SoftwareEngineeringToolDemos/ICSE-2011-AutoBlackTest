/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;
import edu.stanford.ejalbert.BrowserLauncher;

public class HelpTutorial extends MossMenuItem {
	public static final long serialVersionUID = 0;

	public HelpTutorial(MossFrame frame) {
		super(frame, TextFormatter.getTranslation(MenuKeys.MENU_HELP_TUTORIAL));
	}

	public void actionPerformed(ActionEvent e) {
		try{
			BrowserLauncher bl = new BrowserLauncher(null);
			bl.openURLinBrowser(
					Const.PROJECT_URL 
					+ PrefsModel.getInstance().getLanguage().replaceAll("-.*$", "") 
					+ "/quickstart/index.jsp");
		}
		catch (Exception ex){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Exception", ex);
		}
	}
}
