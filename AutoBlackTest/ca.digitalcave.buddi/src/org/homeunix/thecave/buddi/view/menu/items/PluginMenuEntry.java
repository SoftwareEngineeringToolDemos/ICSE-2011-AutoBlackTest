/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import net.java.dev.SwingWorker;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.BuddiExportPlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiImportPlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiSynchronizePlugin;
import org.homeunix.thecave.buddi.plugin.api.MenuPlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginMessage;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableDocumentImpl;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.TransactionFrame;

import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossMenuItem;
import ca.digitalcave.moss.swing.MossSmartFileChooser;
import ca.digitalcave.moss.swing.MossStatusDialog;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class PluginMenuEntry extends MossMenuItem {
	public static final long serialVersionUID = 0;

	private final MenuPlugin plugin;

	public PluginMenuEntry(MossDocumentFrame parentFrame, MenuPlugin plugin) {
		super(parentFrame, TextFormatter.getTranslation(plugin.getName()));

		this.plugin = plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Document d;
		if (plugin instanceof BuddiImportPlugin && ((BuddiImportPlugin) plugin).isCreateNewFile()){
			try {
				d = ModelFactory.createDocument();
				List<BudgetCategory> bcs = new LinkedList<BudgetCategory>(d.getBudgetCategories());
				for (BudgetCategory bc : bcs) {
					d.removeBudgetCategory(bc);
				}
			}
			catch (ModelException me){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Could not create a new document!", me);
				return;	
			}
		}
		else
			d = (Document) ((MossDocumentFrame) getFrame()).getDocument();

		File f = null;

		if (plugin.isPromptForFile()){
			FileFilter filter = new FileFilter(){
				@Override
				public boolean accept(File f) {
					if (f.isDirectory())
						return true;
					if (plugin.getFileExtensions() != null){
						for (String s : plugin.getFileExtensions()) {
							if (f.getName().endsWith(s))
								return true;
						}
					}
					else 
						return true;
					return false;
				}

				@Override
				public String getDescription() {
					return TextFormatter.getTranslation(plugin.getDescription());
				}
			};

			String title = "Unknown Plugin Type";
			if (plugin instanceof BuddiExportPlugin)
				title = PrefsModel.getInstance().getTranslator().get(BuddiKeys.FILECHOOSER_EXPORT_FILE_TITLE);
			else if (plugin instanceof BuddiImportPlugin)
				title = PrefsModel.getInstance().getTranslator().get(BuddiKeys.FILECHOOSER_IMPORT_FILE_TITLE);
			else if (plugin instanceof BuddiSynchronizePlugin)
				title = PrefsModel.getInstance().getTranslator().get(BuddiKeys.FILECHOOSER_SYNCHRONIZE_FILE_TITLE);

			if (plugin.isFileChooserSave())
				f = MossSmartFileChooser.showSaveFileDialog(
						getFrame(),  
						filter,
						plugin.getFileExtensions() == null || plugin.getFileExtensions().length == 0 ? 
								null : 
									plugin.getFileExtensions()[0],
									title, 
									PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK),
									PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CANCEL),
									PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_REPLACE),
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_ERROR_CANNOT_WRITE_DATA_FILE),  
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.ERROR),
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_PROMPT_OVERWRITE_FILE),
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_PROMPT_OVERWRITE_FILE_TITLE));

			else 
				f = MossSmartFileChooser.showOpenDialog(
						getFrame(),  
						filter, 
						title, 
						PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK), 
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_ERROR_CANNOT_WRITE_DATA_FILE), 
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_ERROR_CANNOT_READ_DATA_FILE), 
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.ERROR));

			if (f == null)
				return;
		}

		d.startBatchChange();

		try {
			final File fFinal = f;
			final MossStatusDialog status = new MossStatusDialog(
					getFrame(),
					TextFormatter.getTranslation(plugin.getProcessingMessage()));

			if (plugin.getProcessingMessage() != null)
				status.openWindow();

			SwingWorker worker = new SwingWorker(){
				@Override
				public Object construct() {
					try {
						plugin.processData(new MutableDocumentImpl(d), ((MossDocumentFrame) getFrame()), fFinal);
					}
					catch (PluginMessage pm){
						return pm;
					}
					catch (PluginException pe){
						Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error processing data in plugin: ", pe);
						return null;
					}
					catch (RuntimeException re){
						Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Runtime exception processing data in plugin: ", re);
						return null;
					}
					catch (Error e){
						Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error processing data in plugin: ", e);
						return null;						
					}

					return new Object();
				}

				@Override
				public void finished() {
					status.closeWindow();
					
					d.updateAllBalances();
					d.finishBatchChange();

					if (plugin instanceof BuddiImportPlugin && ((BuddiImportPlugin) plugin).isCreateNewFile()){
						try {
							MainFrame mainWndow = new MainFrame(d);
							mainWndow.openWindow(
									PrefsModel.getInstance().getWindowSize(d.getFile() + ""), 
									PrefsModel.getInstance().getWindowLocation(d.getFile() + ""));
						}
						catch (WindowOpenException woe){}
					}
					else {
						if (getFrame() instanceof MainFrame)
							((MainFrame) getFrame()).fireStructureChanged();
						if (getFrame() instanceof TransactionFrame)
							((TransactionFrame) getFrame()).fireStructureChanged();
					}
					
					MainFrame.updateAllContent();

					if (get() == null){
						String[] options = new String[1];
						options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK);

						JOptionPane.showOptionDialog(
								getFrame(), 
								TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_CHECK_LOGS_FOR_DETAILS), 
								TextFormatter.getTranslation(BuddiKeys.ERROR), 
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE,
								null,
								options,
								options[0]
						);
					}
					else if (get() instanceof PluginMessage){
						String[] options = new String[1];
						options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK);

						PluginMessage message = (PluginMessage) get();
						JOptionPane optionPane = new JOptionPane(message.getMessage(), message.getType(), JOptionPane.OK_OPTION, null, options, options[0]);
						JDialog dial = optionPane.createDialog(null, message.getTitle());
						dial.setModal(true);
						JTextArea text = new JTextArea();
						JScrollPane scroller = new JScrollPane(text);
						if (message.getLongMessage() != null){
							text.setEditable(false);
							text.setWrapStyleWord(true);
							text.setLineWrap(true);
							text.setText(message.getLongMessage());
							scroller.setPreferredSize(new Dimension(350, 200));
							scroller.setBorder(null);
							dial.getContentPane().add(scroller, BorderLayout.SOUTH);
						}

						dial.pack();

						scroller.getVerticalScrollBar().setValue(0);
						scroller.getHorizontalScrollBar().setValue(0);

						dial.setVisible(true);
						optionPane.getValue();
					}

					super.finished();
				}
			};

			worker.start();
		}
		catch (Exception ex){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error encountered in plugin: " + ex);
		}
	}
}
