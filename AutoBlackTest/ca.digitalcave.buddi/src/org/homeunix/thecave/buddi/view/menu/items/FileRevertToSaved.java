/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.OperationCancelledException;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.application.document.exception.DocumentLoadException;
import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class FileRevertToSaved extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public FileRevertToSaved(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE_REVERT_TO_SAVED));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String[] options = new String[2];
		options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_YES);
		options[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_NO);

		if (JOptionPane.showOptionDialog(
				null, 
				TextFormatter.getTranslation(BuddiKeys.MESSAGE_REVERT_TO_SAVED),
				TextFormatter.getTranslation(BuddiKeys.MESSAGE_REVERT_TO_SAVED_TITLE),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null,
				options,
				options[0]
		) == 0) {  //The index of the Yes button.
			try {
				ModelFactory.getAutoSaveLocation(((MossDocumentFrame) getFrame()).getDocument().getFile()).delete();
				Document newDoc = ModelFactory.createDocument(((MossDocumentFrame) getFrame()).getDocument().getFile());
				
				MainFrame mainWndow = new MainFrame(newDoc);
				mainWndow.openWindow(
						PrefsModel.getInstance().getWindowSize(((MainFrame) getFrame()).getDocument().getFile() + ""), 
						PrefsModel.getInstance().getWindowLocation(((MainFrame) getFrame()).getDocument().getFile() + ""), 
						true);
			}
			catch (OperationCancelledException oce){}
			catch (WindowOpenException woe){}
			catch (DocumentLoadException dle){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "There was an error loading the file " + ((MossDocumentFrame) getFrame()).getDocument().getFile(), dle);
			}
		}
		else {
			Logger.getLogger(this.getClass().getName()).info("User cancelled file restore.");
		}
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();

		this.setEnabled(getFrame() instanceof MossDocumentFrame 
				&& ((MossDocumentFrame) getFrame()).getDocument().getFile() != null && ((MossDocumentFrame) getFrame()).getDocument().isChanged());
	}
}
