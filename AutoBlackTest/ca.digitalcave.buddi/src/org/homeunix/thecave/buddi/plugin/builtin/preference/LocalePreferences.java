/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.preference;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.Buddi;
import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.BuddiLanguageEditor;
import org.homeunix.thecave.buddi.i18n.BuddiLanguageEditor.BuddiLanguageEditorException;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;

import ca.digitalcave.moss.swing.MossScrollingComboBox;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

public class LocalePreferences extends BuddiPreferencePlugin implements ActionListener {
	public static final long serialVersionUID = 0; 

	private final MossScrollingComboBox language;
	private final MossScrollingComboBox dateFormat;
	private final MossScrollingComboBox currencyFormat;
	private final JCheckBox currencySymbolAfterAmount;
	private final JButton otherCurrencyButton;
	private final JButton otherDateFormatButton;
	private final JButton editLanguagesButton;

	private final DefaultComboBoxModel languageModel;
	private final DefaultComboBoxModel currencyModel;
	private final DefaultComboBoxModel dateFormatModel;

	public LocalePreferences() {
		languageModel = new DefaultComboBoxModel();
		language = new MossScrollingComboBox(languageModel);
		currencyModel = new DefaultComboBoxModel();
		currencyFormat = new MossScrollingComboBox(currencyModel);
		currencySymbolAfterAmount = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_CURRENCY_SYMBOL_AFTER_AMOUNT));
		dateFormatModel = new DefaultComboBoxModel();
		dateFormat = new MossScrollingComboBox(dateFormatModel);
		otherCurrencyButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_OTHER));
		otherDateFormatButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_OTHER));
		editLanguagesButton = new JButton(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_EDIT_LANGUAGES));
	}

	@Override
	public JPanel getPreferencesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel dateFormatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel currencyFormatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JLabel dateFormatLabel = new JLabel(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_DATE_FORMAT));
		JLabel currencyFormatLabel = new JLabel(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_CURRENCY));
		JLabel languageLabel = new JLabel(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_LANGUAGE));

		//Set up the language pulldown
		language.setRenderer(new DefaultListCellRenderer(){
			public static final long serialVersionUID = 0;
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof String){
					String str = (String) value;
					this.setText(str.replaceAll("_", " "));
				}
				return this;
			}
		});

		//Set up the date format pulldown
		dateFormat.setRenderer(new DefaultListCellRenderer(){
			public static final long serialVersionUID = 0;
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value != null){
					this.setText(new SimpleDateFormat(value.toString()).format(new Date()));
				}

				return this;
			}
		});

		otherCurrencyButton.setPreferredSize(InternalFormatter.getButtonSize(otherCurrencyButton));
		otherDateFormatButton.setPreferredSize(InternalFormatter.getButtonSize(otherDateFormatButton));
		editLanguagesButton.setPreferredSize(InternalFormatter.getButtonSize(editLanguagesButton));

		otherCurrencyButton.addActionListener(this);
		otherDateFormatButton.addActionListener(this);
		editLanguagesButton.addActionListener(this);

		languagePanel.add(languageLabel);
		languagePanel.add(language);
		languagePanel.add(editLanguagesButton);

		dateFormatPanel.add(dateFormatLabel);
		dateFormatPanel.add(dateFormat);
		dateFormatPanel.add(otherDateFormatButton);

		currencyFormatPanel.add(currencyFormatLabel);
		currencyFormatPanel.add(currencyFormat);
		currencyFormatPanel.add(otherCurrencyButton);

		panel.add(languagePanel);		
		panel.add(dateFormatPanel);
		panel.add(currencyFormatPanel);
		panel.add(currencySymbolAfterAmount);
		panel.add(Box.createVerticalGlue());

		return panel;
	}

	public void load() {

		//Set up currency model
		boolean customCurrency = true; //Assume custom until proved otherwise, below
		String currency = PrefsModel.getInstance().getCurrencySign();
		for (String s : Const.CURRENCY_FORMATS) {
			currencyModel.addElement(s);
			if (s.equals(currency))
				customCurrency = false;
		}
		if (customCurrency){
			currencyModel.addElement(currency);
		}

		//Set up Date Format model
		boolean customDateFormat = true; //Assume custom until proved otherwise, below
		for (String s : Const.DATE_FORMATS) {
			dateFormatModel.addElement(s);
			if (s.equals(PrefsModel.getInstance().getDateFormat()))
				customDateFormat = false;
		}
		if (customDateFormat){
			dateFormatModel.addElement(PrefsModel.getInstance().getDateFormat());
		}


		//Set up language model.  Look for all built in ones, and any extras.
		languageModel.removeAllElements();
		Set<String> languages = new HashSet<String>();
		//Load all built in languages
		for (String language : Const.BUNDLED_LANGUAGES) {
			languages.add(language);			
		}
		//Load all languages in the Languages folder
		File languageLocation = Buddi.getLanguagesFolder();
		if (languageLocation.exists() && languageLocation.isDirectory()){
			for (File f: languageLocation.listFiles())
				if (f.getName().endsWith(Const.LANGUAGE_EXTENSION))
					languages.add(f.getName().replaceAll(Const.LANGUAGE_EXTENSION, ""));
		}
		//Load all found languages into language model
		Vector<String> languagesVector = new Vector<String>(languages);
		Collections.sort(languagesVector);
		for (String string : languagesVector) {
			languageModel.addElement(string);
		}


		//Select from Preferencs
		dateFormat.setSelectedItem(PrefsModel.getInstance().getDateFormat());
		currencyFormat.setSelectedItem(PrefsModel.getInstance().getCurrencySign());
		currencySymbolAfterAmount.setSelected(PrefsModel.getInstance().isShowCurrencyAfterAmount());
		language.setSelectedItem(PrefsModel.getInstance().getLanguage());
	}

	public boolean save() {
		boolean restart = false;
		if (!PrefsModel.getInstance().getDateFormat().equals(dateFormat.getSelectedItem().toString()))
			restart = true;
		if (!PrefsModel.getInstance().getLanguage().equals(language.getSelectedItem().toString()))
			restart = true;
		if (!PrefsModel.getInstance().getCurrencySign().equals(currencyFormat.getSelectedItem().toString()))
			restart = true;
		if (PrefsModel.getInstance().isShowCurrencyAfterAmount() != currencySymbolAfterAmount.isSelected())
			restart = true;
		
		PrefsModel.getInstance().setDateFormat(dateFormat.getSelectedItem().toString());
		PrefsModel.getInstance().setCurrencySign(currencyFormat.getSelectedItem().toString());
		PrefsModel.getInstance().setShowCurrencyAfterAmount(currencySymbolAfterAmount.isSelected());
		PrefsModel.getInstance().setLanguage(language.getSelectedItem().toString());

		PrefsModel.getInstance().getTranslator().reloadLanguages();
		
		return restart;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(otherCurrencyButton)){
			String newCurrency = JOptionPane.showInputDialog(
					null, 
					TextFormatter.getTranslation(BuddiKeys.PREFERENCE_ENTER_CURRENCY_SYMBOL), 
					TextFormatter.getTranslation(BuddiKeys.PREFERENCE_ENTER_CURRENCY_SYMBOL_TITLE), 
					JOptionPane.PLAIN_MESSAGE);

			if (newCurrency != null && newCurrency.length() > 0){
				currencyModel.removeAllElements();

				//Set up currency lists
				boolean customCurrency = true; //Assume custom until proved otherwise, below
				for (String s : Const.CURRENCY_FORMATS) {
					currencyModel.addElement(s);
					if (s.equals(newCurrency)){
						customCurrency = false;
						Logger.getLogger(this.getClass().getName()).finest("Currency " + newCurrency + " already in list...");
					}
				}
				if (customCurrency){
					currencyModel.addElement(newCurrency);
				}

				currencyFormat.setSelectedItem(newCurrency);
			}
			else {
				Logger.getLogger(this.getClass().getName()).finest("Invalid currency: '" + newCurrency + "'");
			}	
		}
		else if (e.getSource().equals(otherDateFormatButton)){
			String newDateFormat = JOptionPane.showInputDialog(
					null, 
					TextFormatter.getTranslation(BuddiKeys.PREFERENCE_ENTER_DATE_FORMAT), 
					TextFormatter.getTranslation(BuddiKeys.PREFERENCE_ENTER_DATE_FORMAT_TITLE), 
					JOptionPane.PLAIN_MESSAGE);

			if (newDateFormat != null 
					&& newDateFormat.length() > 0){

				//Test out the new format, to see if it complies with
				// the format rules.
				try {
					new SimpleDateFormat(newDateFormat);
				}
				catch (IllegalArgumentException iae){
					String[] options = new String[1];
					options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

					JOptionPane.showOptionDialog(
							null, 
							TextFormatter.getTranslation(BuddiKeys.PREFERENCE_ERROR_INCORRECT_FORMAT), 
							TextFormatter.getTranslation(BuddiKeys.ERROR),
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE,
							null,
							options,
							options[0]);
					return;
				}


				dateFormatModel.removeAllElements();

				//Set up currency lists
				boolean customDateFormat = true; //Assume custom until proved otherwise, below
				for (String s : Const.DATE_FORMATS) {
					dateFormatModel.addElement(s);
					if (s.equals(newDateFormat)){
						customDateFormat = false;
						Logger.getLogger(this.getClass().getName()).finest("Date Format " + newDateFormat + " already in list...");
					}
				}
				if (customDateFormat){
					dateFormatModel.addElement(newDateFormat);
				}

				dateFormatModel.setSelectedItem(newDateFormat);
			}
			else {
				Logger.getLogger(this.getClass().getName()).finest("Invalid Date Format: '" + newDateFormat + "'");
			}
		}
		else if (e.getSource().equals(editLanguagesButton)){
			try {
				BuddiLanguageEditor ble = BuddiLanguageEditor.getInstance(language.getSelectedItem().toString());
				ble.openWindow();
			}
			catch (WindowOpenException woe){}
			catch (BuddiLanguageEditorException blee){}
		}

	}

	public String getName() {
		return BuddiKeys.LOCALE.toString();
	}
}
