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

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.ConcurrentSaveException;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.application.document.exception.DocumentSaveException;
import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.MossSmartFileChooser;

public class FileSaveAs extends MossMenuItem {
	public static final long serialVersionUID = 0;
	
	public FileSaveAs(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE_SAVE_AS),
				KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_MASK));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean removeAutosave = false;
		if (((MossDocumentFrame) getFrame()).getDocument().getFile() == null)
			removeAutosave = true;
		
		File f = MossSmartFileChooser.showSaveFileDialog(
				getFrame(), 
				Const.FILE_FILTER_DATA, 
				Const.DATA_FILE_EXTENSION, 
				PrefsModel.getInstance().getTranslator().get(BuddiKeys.FILECHOOSER_SAVE_DATA_FILE_TITLE), 
				PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK), 
				PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CANCEL), 
				PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_REPLACE), 
				PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_ERROR_CANNOT_WRITE_DATA_FILE),
				PrefsModel.getInstance().getTranslator().get(BuddiKeys.ERROR),
				PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_PROMPT_OVERWRITE_FILE), 
				PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_PROMPT_OVERWRITE_FILE_TITLE)
		);
		
		//User hit cancel
		if (f == null)
			return;
		
		try {
			//Ask if we should encrypt this file
			String[] options = new String[2];
			options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_YES);
			options[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_NO);
			
			//Set whether we should change the password or not...
			((Document) ((MossDocumentFrame) getFrame()).getDocument()).setFlag(Document.RESET_PASSWORD, true);
			((Document) ((MossDocumentFrame) getFrame()).getDocument()).setFlag(Document.CHANGE_PASSWORD, 
					(JOptionPane.showOptionDialog(
							getFrame(), 
							TextFormatter.getTranslation(BuddiKeys.MESSAGE_ASK_FOR_DATA_FILE_ENCRYPTION),
							TextFormatter.getTranslation(BuddiKeys.MESSAGE_ASK_FOR_DATA_FILE_ENCRYPTION_TITLE),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE,
							null,
							options,
							options[0]
					) == JOptionPane.YES_OPTION));
			
			((MossDocumentFrame) getFrame()).getDocument().saveAs(f);
			getFrame().updateContent();
		}
		catch (ConcurrentSaveException cse){
			JOptionPane.showMessageDialog(
					null, 
					TextFormatter.getTranslation(BuddiKeys.MESSAGE_CONCURRENT_SAVE_EXCEPTION_TEXT),
					TextFormatter.getTranslation(BuddiKeys.WARNING),
					JOptionPane.WARNING_MESSAGE);
		}
		catch (DocumentSaveException dse){
			throw new RuntimeException("Error saving file: " + dse);
		}
		
		//If we started out with a null file (i.e., this is a save as for a new
		// file), we will remove any auto save we find in the default auto save
		// location.  If we have multiple unsaved files open, this may remove
		// a different file's auto save information, but it will be recreated
		// within the set auto save period (and if there were two open, they would
		// be overwriting each other anyway, so there is no problem).
		if (removeAutosave)
			ModelFactory.getAutoSaveLocation(null).delete();
	}
	
	@Override
	public void updateMenus() {
		super.updateMenus();

		this.setEnabled(getFrame() instanceof MossDocumentFrame);
	}
}
