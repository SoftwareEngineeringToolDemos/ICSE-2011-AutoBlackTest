/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.preference;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.homeunix.thecave.buddi.Buddi;
import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.FileFunctions;

import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.common.ClassLoaderFunctions;
import ca.digitalcave.moss.swing.model.DefaultGenericListModel;

public class PluginPreferences extends BuddiPreferencePlugin implements ActionListener {
	public static final long serialVersionUID = 0;

	private final JButton addButton;
	private final JButton removeButton;
	private final JList pluginList;
	private final DefaultGenericListModel<File> pluginListModel;

	private final List<File> filesToAdd = new LinkedList<File>();
	private final List<File> filesToRemove = new LinkedList<File>();

	public PluginPreferences() {
		addButton = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_ADD));
		removeButton = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_REMOVE));
		pluginListModel = new DefaultGenericListModel<File>();
		pluginList = new JList(pluginListModel);
	}

	public JPanel getPreferencesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		Dimension buttonSize = new Dimension(Math.max(100, removeButton.getPreferredSize().width), removeButton.getPreferredSize().height);
		addButton.setPreferredSize(buttonSize);
		removeButton.setPreferredSize(buttonSize);
		removeButton.setEnabled(false);
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);

		addButton.addActionListener(this);
		removeButton.addActionListener(this);

		JScrollPane pluginListScroller = new JScrollPane(pluginList);

		pluginList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()){
					removeButton.setEnabled(pluginList.getSelectedIndex() != -1);
				}
			}
		});

		pluginList.setCellRenderer(new DefaultListCellRenderer(){
			public static final long serialVersionUID = 0;
			private final Map<File, String> fileToVersionMap = new HashMap<File, String>();
			private final Map<File, String> fileToNameMap = new HashMap<File, String>();

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value instanceof File && ((File) value).getName().endsWith(Const.PLUGIN_EXTENSION)){
					File file = (File) value;
					if (fileToVersionMap.get(file) == null){
						String version = ""; 
						Properties p = new Properties();
						InputStream is = ClassLoaderFunctions.getResourceAsStreamFromJar(file, Const.PLUGIN_PROPERTIES);
						if (is != null) {
							try {
								p.load(is);
								if (p.getProperty(Const.PLUGIN_PROPERTIES_VERSION) != null)
									version = "  (" + p.getProperty(Const.PLUGIN_PROPERTIES_VERSION) + ")";
								fileToVersionMap.put(file, version);
							}
							catch (IOException ioe){}
						}
					}
					if (fileToNameMap.get(file) == null){
						fileToNameMap.put(file, file.getName().replaceAll(Const.PLUGIN_EXTENSION, ""));
					}
					if (fileToVersionMap.get(file) == null){
						fileToVersionMap.put(file, "");
					}
					
					this.setText(fileToNameMap.get(file) + fileToVersionMap.get(file));
				}
				else 
					this.setText("Not of correct file type");

				return this;
			}
		});

		panel.add(pluginListScroller, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}

	public void load() {
		pluginListModel.clear();
		for (File file : BuddiPluginFactory.getPluginFiles()) {
			pluginListModel.addElement(file);
		}		
		pluginList.setSelectedIndices(new int[0]);
	}

	public boolean save() {
		//At save time, we actually copy the files over to the new location, and remove ones which
		// are to be removed.		
		for (File f : filesToRemove) {
			if (!f.delete()){
				f.deleteOnExit();
				String[] options = new String[1];
				options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK);
				JOptionPane.showOptionDialog(
						null, 
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_DELETING_PLUGIN_POSTPONED), 
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_DELETING_PLUGIN_TITLE), 
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[0]
				);
			}
		}

		if (!Buddi.getPluginsFolder().exists()){
			if (!Buddi.getPluginsFolder().mkdirs()){
				String[] options = new String[1];
				options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(
						null, 
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_DELETING_PLUGIN), 
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_DELETING_PLUGIN_TITLE), 
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[0]
				);
			}
		}


		for (File f : filesToAdd) {
			try {
				FileFunctions.copyFile(f, new File(Buddi.getPluginsFolder().getAbsolutePath() + File.separator + f.getName()));
			}
			catch (IOException ioe){
				String[] options = new String[1];
				options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(
						null, 
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_COPYING_PLUGIN), 
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_ERROR_COPYING_PLUGIN_TITLE), 
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[0]
				);
			}
		}

		BuddiPluginFactory.forcePluginRefresh();
		
		return filesToAdd.size() > 0 || filesToRemove.size() > 0;
	}

	public String getName() {
		return BuddiKeys.PLUGINS.toString();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addButton)){
			final JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setFileFilter(new FileFilter(){
				public boolean accept(File arg0) {
					if (arg0.getAbsolutePath().endsWith(Const.PLUGIN_EXTENSION) ||
							arg0.isDirectory())
						return true;
					else
						return false;
				}

				@Override
				public String getDescription() {
					return PrefsModel.getInstance().getTranslator().get(BuddiKeys.FILE_DESCRIPTION_BUDDI_PLUGINS);
				}
			});
			jfc.setDialogTitle(PrefsModel.getInstance().getTranslator().get(BuddiKeys.CHOOSE_PLUGIN_JAR));
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				Properties props = new Properties();
				try {
					InputStream is = ClassLoaderFunctions.getResourceAsStreamFromJar(jfc.getSelectedFile(), Const.PLUGIN_PROPERTIES);
					if (is != null)
						props.load(is);
				}
				catch (IOException ioe){}				
				List<MossPlugin> plugins = BuddiPluginFactory.getMossPluginsFromJar(jfc.getSelectedFile(), Buddi.getVersion(), props.getProperty(Const.PLUGIN_PROPERTIES_ROOT));
				if (plugins.size() == 0){
					String[] options = new String[1];
					options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK);

					JOptionPane.showOptionDialog(
							null, 
							PrefsModel.getInstance().getTranslator().get(BuddiKeys.NO_PLUGINS_IN_JAR), 
							PrefsModel.getInstance().getTranslator().get(BuddiKeys.NO_PLUGINS_IN_JAR_TITLE), 
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							options[0]
					);
				}
				else {
					filesToAdd.add(jfc.getSelectedFile());
					pluginListModel.addElement(jfc.getSelectedFile());
				}
			}
		}
		else if (e.getSource().equals(removeButton)){
			if (pluginList.getSelectedValues().length > 0) {
				for (Object o : pluginList.getSelectedValues()) {
					filesToAdd.remove(o);
					filesToRemove.add((File) o);
				}
			}
			
			for (File f : filesToRemove) {
				pluginListModel.removeElement(f);
			}
		}
	}

	@Override
	public boolean isUseWrapper() {
		return false;
	}
}
