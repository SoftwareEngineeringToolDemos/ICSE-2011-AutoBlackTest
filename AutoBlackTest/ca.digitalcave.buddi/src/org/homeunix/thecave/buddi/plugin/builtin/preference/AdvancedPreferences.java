/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.preference;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.BuddiTransactionCellRendererPlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.plugin.builtin.cellrenderer.DefaultTransactionCellRenderer;

import ca.digitalcave.moss.swing.model.BackedComboBoxModel;

public class AdvancedPreferences extends BuddiPreferencePlugin {
	public static final long serialVersionUID = 0;

	private final JComboBox numberOfBackups;
	private final JComboBox transactionCellRenderer;
	private final JComboBox autosavePeriod;
	private final JCheckBox showPromptForDataFile;
//	private final JCheckBox sendCrashReport;
	private final JCheckBox showUpdateNotifications;
	private final JCheckBox hideNegativeSign;
	
	
	@SuppressWarnings("unchecked")
	public AdvancedPreferences() {
		transactionCellRenderer = new JComboBox(new BackedComboBoxModel<BuddiTransactionCellRendererPlugin>((List<BuddiTransactionCellRendererPlugin>) BuddiPluginFactory.getPlugins(BuddiTransactionCellRendererPlugin.class)));
		numberOfBackups = new JComboBox(new Integer[]{3, 5, 10, 25, 50});
		autosavePeriod = new JComboBox(new Integer[]{15, 30, 60, 120, 300});
		showPromptForDataFile = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_PROMPT_FOR_DATA_FILE_AT_STARTUP));
//		sendCrashReport = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SEND_CRASH_REPORTS));
		showUpdateNotifications = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_ENABLE_UPDATE_NOTIFICATIONS));
		hideNegativeSign = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_HIDE_NEGATIVE_SIGNS));
	}

	@Override
	public JPanel getPreferencesPanel() {
		
		autosavePeriod.setRenderer(new DefaultListCellRenderer(){
			public static final long serialVersionUID = 0;
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (((Integer) value) < 60)
					this.setText(value + " " + TextFormatter.getTranslation(BuddiKeys.SECONDS));
				else if (((Integer) value) == 60)
					this.setText(((Integer) value) / 60 + " " + TextFormatter.getTranslation(BuddiKeys.MINUTE));
				else
					this.setText(((Integer) value) / 60 + " " + TextFormatter.getTranslation(BuddiKeys.MINUTES));
				return this;
			}
		});
		
		transactionCellRenderer.setRenderer(new DefaultListCellRenderer(){
			public static final long serialVersionUID = 0;
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				if (value instanceof BuddiTransactionCellRendererPlugin){
					this.setText(((BuddiTransactionCellRendererPlugin) value).getName());
				}
				else {
					this.setText(" ");
				}
				
				return this;
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel autosavePeriodPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel numberOfBackupsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel editLanguagesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel editTypesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel promptForDataFilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel checkForUpdatesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		JPanel sendCrashReportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel hideNegativePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel transactionCellRendererPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JLabel autosavePeriodLabel = new JLabel(TextFormatter.getTranslation(BuddiKeys.AUTOSAVE_PERIOD));
		JLabel numberOfBackupsLabel = new JLabel(TextFormatter.getTranslation(BuddiKeys.NUMBER_OF_BACKUPS));
		JLabel transactionCellRendererLabel = new JLabel(TextFormatter.getTranslation(BuddiKeys.TRANSACTION_CELL_RENDERER));

		transactionCellRendererPanel.add(transactionCellRendererLabel);
		transactionCellRendererPanel.add(transactionCellRenderer);
		
		autosavePeriodPanel.add(autosavePeriodLabel);
		autosavePeriodPanel.add(autosavePeriod);
		
		numberOfBackupsPanel.add(numberOfBackupsLabel);
		numberOfBackupsPanel.add(numberOfBackups);
		
		transactionCellRendererPanel.add(transactionCellRendererLabel);
		transactionCellRendererPanel.add(transactionCellRenderer);
		
		promptForDataFilePanel.add(showPromptForDataFile);
		checkForUpdatesPanel.add(showUpdateNotifications);
//		sendCrashReportPanel.add(sendCrashReport);
		
		hideNegativePanel.add(hideNegativeSign);
				
		panel.add(autosavePeriodPanel);
		panel.add(numberOfBackupsPanel);
		panel.add(transactionCellRendererPanel);
		panel.add(editLanguagesPanel);
		panel.add(editTypesPanel);
		panel.add(updatePanel);
		panel.add(promptForDataFilePanel);
		panel.add(hideNegativePanel);
		panel.add(checkForUpdatesPanel);
//		panel.add(sendCrashReportPanel);
		
		return panel;
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		BuddiTransactionCellRendererPlugin renderer = new DefaultTransactionCellRenderer();
		for (BuddiTransactionCellRendererPlugin r : (List<BuddiTransactionCellRendererPlugin>) BuddiPluginFactory.getPlugins(BuddiTransactionCellRendererPlugin.class)) {
			if (r.getClass().getCanonicalName().equals(PrefsModel.getInstance().getTransactionCellRenderer())){
				renderer = r;
				break;
			}
		}
		
		transactionCellRenderer.setSelectedItem(renderer);
		autosavePeriod.setSelectedItem(PrefsModel.getInstance().getAutosaveDelay());
		numberOfBackups.setSelectedItem(PrefsModel.getInstance().getNumberOfBackups());
		showPromptForDataFile.setSelected(PrefsModel.getInstance().isShowPromptAtStartup());
		showUpdateNotifications.setSelected(PrefsModel.getInstance().isShowUpdateNotifications());
//		sendCrashReport.setSelected(PrefsModel.getInstance().isSendCrashReports());
		hideNegativeSign.setSelected(PrefsModel.getInstance().isDontShowNegativeSign());
	}

	public boolean save() {
		PrefsModel.getInstance().setTransactionCellRenderer(transactionCellRenderer.getSelectedItem().getClass().getCanonicalName());
		PrefsModel.getInstance().setAutosaveDelay((Integer) autosavePeriod.getSelectedItem());
		PrefsModel.getInstance().setNumberOfBackups((Integer) numberOfBackups.getSelectedItem());
		PrefsModel.getInstance().setShowPromptAtStartup(showPromptForDataFile.isSelected());
		PrefsModel.getInstance().setShowUpdateNotifications(showUpdateNotifications.isSelected());
//		PrefsModel.getInstance().setSendCrashReports(sendCrashReport.isSelected());
		PrefsModel.getInstance().setShowNegativeSign(hideNegativeSign.isSelected());
		
		return false;
	}
	
	public String getName() {
		return BuddiKeys.ADVANCED.toString();
	}
}
