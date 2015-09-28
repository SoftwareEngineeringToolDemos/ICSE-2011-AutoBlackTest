/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.impl.ConcurrentSaveException;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.application.document.StandardDocument;
import ca.digitalcave.moss.application.document.exception.DocumentSaveException;
import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;

public class FileSave extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public FileSave(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE_SAVE),
				KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		StandardDocument model = ((MossDocumentFrame) getFrame()).getDocument(); 
		File f = model.getFile();
		try {
			if (f == null) {
				new FileSaveAs((MossDocumentFrame) getFrame()).doClick();
			}
			else {
				model.save();
				getFrame().updateContent();
			}
		}
		catch (ConcurrentSaveException cse){
			JOptionPane.showMessageDialog(
					null, 
					TextFormatter.getTranslation(BuddiKeys.MESSAGE_CONCURRENT_SAVE_EXCEPTION_TEXT),
					TextFormatter.getTranslation(BuddiKeys.WARNING),
					JOptionPane.WARNING_MESSAGE);
		}		
		catch (DocumentSaveException dse){
			//TODO Do something less drastic here.  For debugging, we will
			// throw a runtime exception to ensure that we catch all error
			// conditions.
			throw new RuntimeException("Error saving file: " + dse, dse);
		}
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();

		this.setEnabled(getFrame() instanceof MossDocumentFrame);
	}
}
