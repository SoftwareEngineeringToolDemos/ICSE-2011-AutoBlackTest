/*
 * Created on Aug 19, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.i18n;

import javax.swing.JOptionPane;

import org.homeunix.thecave.buddi.Buddi;
import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.i18n.keys.BudgetExpenseDefaultKeys;
import org.homeunix.thecave.buddi.i18n.keys.BudgetIncomeDefaultKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.i18n.keys.MonthKeys;
import org.homeunix.thecave.buddi.i18n.keys.PluginRangeFilters;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequency;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyDayOfMonth;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyDayOfWeek;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyFirstWeekOfMonth;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyMonth;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequencyWeek;
import org.homeunix.thecave.buddi.i18n.keys.TransactionClearedFilterKeys;
import org.homeunix.thecave.buddi.i18n.keys.TransactionDateFilterKeys;
import org.homeunix.thecave.buddi.i18n.keys.TransactionReconciledFilterKeys;
import org.homeunix.thecave.buddi.i18n.keys.TypeCreditDefaultKeys;
import org.homeunix.thecave.buddi.i18n.keys.TypeDebitDefaultKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.application.document.exception.DocumentSaveException;
import ca.digitalcave.moss.i18n.LanguageEditor;

public class BuddiLanguageEditor extends LanguageEditor {
	public static final long serialVersionUID = 0;
	
	public static BuddiLanguageEditor getInstance(String language) throws BuddiLanguageEditorException {
		String tempLanguage = JOptionPane.showInputDialog( 
				TextFormatter.getTranslation(BuddiKeys.LANGUAGE_EDITOR_NAME),
				language.replaceAll("_\\(.*\\)", "")
		);

		if (tempLanguage == null || tempLanguage.length() == 0){
			String[] options = new String[1];
			options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

			JOptionPane.showOptionDialog(
					null, 
					TextFormatter.getTranslation(BuddiKeys.LANGUAGE_EDITOR_BLANK_VALUE),
					TextFormatter.getTranslation(BuddiKeys.ERROR),
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE,
					null,
					options,
					options[0]
			);

			throw new BuddiLanguageEditorException("Blank Language");
		}
		
		//Check that we removed something from the passed in language.
		String defaultLocale = language.replaceAll("^.*_\\(", "").replaceAll("\\)$", "");
		if (defaultLocale.equals(language))
			defaultLocale = "";

		String tempLocaleName = JOptionPane.showInputDialog(
				null, 
				TextFormatter.getTranslation(BuddiKeys.LANGUAGE_EDITOR_LOCALE),
				defaultLocale
		);

		String localeName;
		if (tempLocaleName == null || tempLocaleName.equals(""))
			localeName = "";
		else
			localeName = "_(" + tempLocaleName + ")";
		
		return new BuddiLanguageEditor(tempLanguage + localeName);
	}
	
	private BuddiLanguageEditor(String selectedLanguage) {
		super(Const.LANGUAGE_EXTENSION, selectedLanguage);

		//TODO Make sure all translation keys are loaded here.  It's annoying that this is
		// not automated, but the convenience of having multiple smaler key files
		// makes up for it.
		this.loadKeys((Enum[]) BuddiKeys.values());
		this.loadKeys((Enum[]) BudgetCategoryTypes.values());
		this.loadKeys((Enum[]) BudgetExpenseDefaultKeys.values());
		this.loadKeys((Enum[]) BudgetIncomeDefaultKeys.values());
		this.loadKeys((Enum[]) ButtonKeys.values());
		this.loadKeys((Enum[]) MenuKeys.values());
		this.loadKeys((Enum[]) MonthKeys.values());		
		this.loadKeys((Enum[]) PluginRangeFilters.values());
		this.loadKeys((Enum[]) ScheduleFrequency.values());
		this.loadKeys((Enum[]) ScheduleFrequencyDayOfMonth.values());
		this.loadKeys((Enum[]) ScheduleFrequencyDayOfWeek.values());
		this.loadKeys((Enum[]) ScheduleFrequencyFirstWeekOfMonth.values());
		this.loadKeys((Enum[]) ScheduleFrequencyMonth.values());
		this.loadKeys((Enum[]) ScheduleFrequencyWeek.values());
		this.loadKeys((Enum[]) TransactionClearedFilterKeys.values());
		this.loadKeys((Enum[]) TransactionDateFilterKeys.values());
		this.loadKeys((Enum[]) TransactionReconciledFilterKeys.values());
		this.loadKeys((Enum[]) TypeCreditDefaultKeys.values());
		this.loadKeys((Enum[]) TypeDebitDefaultKeys.values());
		
		this.loadLanguages(Const.LANGUAGE_RESOURCE_PATH, PrefsModel.getInstance().getTranslator().getLanguageList(selectedLanguage).toArray(new String[1]));
		this.loadLanguages(Buddi.getLanguagesFolder(), PrefsModel.getInstance().getTranslator().getLanguageList(selectedLanguage).toArray(new String[1]));
	}
	
	@Override
	public void save() {
		if (!Buddi.getLanguagesFolder().exists())
			Buddi.getLanguagesFolder().mkdirs();
		try {
			super.saveAs(Buddi.getLanguagesFolder());
		}
		catch (DocumentSaveException dse){}
	}
	
	public static class BuddiLanguageEditorException extends Exception {
		public static final long serialVersionUID = 0;
		
		public BuddiLanguageEditorException() {
			super();
		}
		
		public BuddiLanguageEditorException(String message) {
			super(message);
		}		
	}
}
