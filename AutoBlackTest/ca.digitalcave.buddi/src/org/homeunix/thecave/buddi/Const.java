/*
 * Created on May 12, 2006 by wyatt
 */
package org.homeunix.thecave.buddi;

import java.awt.Color;
import java.io.File;

import javax.swing.JList;
import javax.swing.filechooser.FileFilter;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.builtin.cellrenderer.ChequeTransactionCellRenderer;
import org.homeunix.thecave.buddi.plugin.builtin.cellrenderer.ConciseTransactionCellRenderer;
import org.homeunix.thecave.buddi.plugin.builtin.cellrenderer.DefaultTransactionCellRenderer;
import org.homeunix.thecave.buddi.plugin.builtin.cellrenderer.SimpleTransactionCellRenderer;
import org.homeunix.thecave.buddi.plugin.builtin.preference.AdvancedPreferences;
import org.homeunix.thecave.buddi.plugin.builtin.preference.LocalePreferences;
import org.homeunix.thecave.buddi.plugin.builtin.preference.NetworkPreferences;
import org.homeunix.thecave.buddi.plugin.builtin.preference.PluginPreferences;
import org.homeunix.thecave.buddi.plugin.builtin.preference.ViewPreferences;
import org.homeunix.thecave.buddi.plugin.builtin.report.AverageIncomeExpenseByCategory;
import org.homeunix.thecave.buddi.plugin.builtin.report.ExpensesPieGraph;
import org.homeunix.thecave.buddi.plugin.builtin.report.IncomeExpenseReportByCategory;
import org.homeunix.thecave.buddi.plugin.builtin.report.IncomePieGraph;
import org.homeunix.thecave.buddi.plugin.builtin.report.NetWorthOverTime;


/**
 * A container for constants, used throughout the rest of the program.
 * 
 * @author wyatt
 *
 */
public class Const {
	public static final String PROJECT_NAME = "Buddi";
	
	public static final String STABLE = "STABLE";
	public static final String DEVELOPMENT = "DEVELOPMENT";
	
	//Version variables
	public static final String BRANCH = STABLE;
	public static final boolean DEVEL = BRANCH.equals(DEVELOPMENT);
	
	//Language constants
	public final static String LANGUAGE_EXTENSION = ".lang";
	public final static String LANGUAGE_FOLDER = "Languages";
	public final static String LANGUAGE_RESOURCE_PATH = "/Languages";
	
	//Data file constants
	public final static String DATA_FILE_EXTENSION = ".buddi3";
	public final static String BACKUP_FILE_EXTENSION = ".buddi3bak";
	public final static String AUTOSAVE_FILE_EXTENSION = ".buddi3autosave";
	
	//Preference file constants
	public static final String PREFERENCE_FILE_NAME = "Buddi3_Prefs.xml"; 
		
	//Report constants
	public static final String REPORT_FOLDER = "Reports";
	
	//Plugin Constants
	public static final String PLUGIN_FOLDER = "Plugins";
	public static final String PLUGIN_EXTENSION = ".buddi3plugin";
	public static final String PLUGIN_PROPERTIES = "plugin.properties";
	public static final String PLUGIN_PROPERTIES_ROOT = "PLUGIN_ROOT";  //Used for plugins to define where to look for plugin classes
	public static final String PLUGIN_PROPERTIES_VERSION = "VERSION";
	
	//File filter for loading / saving data files
	public static final FileFilter FILE_FILTER_DATA = new FileFilter(){
		@Override
		public boolean accept(File f) {
			if (f.isDirectory() 
					|| f.getName().endsWith(Const.DATA_FILE_EXTENSION)){
				return true;
			}
			return false;
		}

		@Override
		public String getDescription() {
			return PrefsModel.getInstance().getTranslator().get(BuddiKeys.FILE_DESCRIPTION_BUDDI_DATA_FILES);
		}
	};
	
	//File filter for loading backup files
	public static final FileFilter FILE_FILTER_BACKUP = new FileFilter(){
		@Override
		public boolean accept(File f) {
			if (f.isDirectory() 
					|| f.getName().endsWith(Const.BACKUP_FILE_EXTENSION)){
				return true;
			}
			return false;
		}

		@Override
		public String getDescription() {
			return PrefsModel.getInstance().getTranslator().get(BuddiKeys.FILE_DESCRIPTION_BUDDI_DATA_FILES);
		}
	};
	
	//Web addresses
	public final static String DONATE_URL = "http://sourceforge.net/donate/index.php?group_id=167026";
	public final static String PROJECT_URL = "http://buddi.digitalcave.ca/";
	public final static String DOWNLOAD_URL_STABLE = "http://buddi.digitalcave.ca/buddi";
	public final static String DOWNLOAD_URL_UNSTABLE = "http://buddi.digitalcave.ca/buddi-development";
	public final static String VERSION_FILE = "version.txt";
	
	//File Types
	public final static String DOWNLOAD_TYPE_OSX = ".dmg";
	public final static String DOWNLOAD_TYPE_WINDOWS = ".exe";
	public final static String DOWNLOAD_TYPE_WINDOWS_INSTALLER = "-Installer.exe";
	public final static String DOWNLOAD_TYPE_GENERIC = ".jar";
	public final static String DOWNLOAD_TYPE_DEBIAN = ".deb";
	public final static String DOWNLOAD_TYPE_REDHAT = ".rpm";
	public final static String DOWNLOAD_TYPE_SLACKWARE = "-Slackware.tgz";
	public final static String DOWNLOAD_TYPE_UNIX = ".tgz";
	
	//Local help paths
	public final static String HELP_FOLDER = "Help";
	public final static String HELP_FILE = "index.html";
	
	//File names
	public final static String LOG_FILE = "Buddi.log";
	
	//Colors
	public final static Color COLOR_JLIST_SELECTED_BACKGROUND = new JList().getSelectionBackground(); // new Color(181, 213, 255);
	public final static Color COLOR_JLIST_SELECTED_TEXT = new JList().getSelectionForeground();
	public final static Color COLOR_JLIST_UNSELECTED_TEXT = new JList().getForeground();
	public final static Color COLOR_TRANSPARENT = new Color(0, 0, 0, 255);
	public final static Color COLOR_EVEN_ROW = new Color(237, 243, 254);
	public final static Color COLOR_ODD_ROW = Color.WHITE;

	
	//Languages which are included in the .jar file.  Needed since there
	// is no good method of reading the main jar file by itself (since
	// it may be wrapped in a .exe, etc).
	public final static String[] BUNDLED_LANGUAGES = {
//		"Czech",
		"Deutsch",
		"English_(US)",
		"English",
		"Espanol",
		"Francais",
		"Greek",
		"Hebrew",
		"Italiano",
		"Nederlands",
		"Norsk",
		"Portugues",
		"Russian",
		"Serbian",
		"Svenska",
	};
	
	//Date formats to appear in Preferences.
	public final static String[] DATE_FORMATS = {
		"yyyy/MM/dd",
		"yyyy/MMM/dd",
		"dd/MM/yyyy",
		"dd/MMM/yyyy",
		"MM/dd/yyyy",
		"MMM/dd/yyyy",
		"MMMM dd, yyyy",
		"MMM dd yyyy"
	};
	
	//Currency formats to appear in Preferences
	public final static String[] CURRENCY_FORMATS = {
		"$",
		"\u20ac", //Euro
		"\u00a3", //British Pounds
		"p.",     //Russian Ruble
		"\u00a5", //Yen
		"\u20a3", //French Franc
		"SFr", 		//Swiss Franc (?)
		"Rs", 		//Indian Rupees
		"Kr", 		//Norwegian
		"Bs", 		//Venezuela
		"S/.", 		//Peru
		"\u20b1", //Peso
		"\u20aa", //Israel Sheqel 
		"Mex$",		//Mexican Peso
		"R$",			//Brazilian Real
		"Ch$",		//Chilean Peso
		"C",			//Costa Rican Colon
		"Arg$",		//Argentinan Peso
		"Kc"	//Something else; requested by a user
	};
	
	//Built in Preference Panes 
	public final static String[] BUILT_IN_PREFERENCE_PANELS = {
		ViewPreferences.class.getCanonicalName(),
		PluginPreferences.class.getCanonicalName(),
		LocalePreferences.class.getCanonicalName(),
		NetworkPreferences.class.getCanonicalName(),
		AdvancedPreferences.class.getCanonicalName(),
//		SkinChooserPreferencePane.class.getCanonicalName(),
	};
	
	//Built in Reports
	public final static String[] BUILT_IN_REPORTS = {
		IncomeExpenseReportByCategory.class.getCanonicalName(),
		AverageIncomeExpenseByCategory.class.getCanonicalName(),
		IncomePieGraph.class.getCanonicalName(),
		ExpensesPieGraph.class.getCanonicalName(),
		NetWorthOverTime.class.getCanonicalName(),
	};
	
	//Built in Imports
	public static final String[] BUILT_IN_IMPORTS = {
//		ImportLegacyData.class.getCanonicalName(),
//		ImportTestData.class.getCanonicalName(),
//		CSVImportDoneRight.class.getCanonicalName(),
	};
	
	//Built in Exports
	public static final String[] BUILT_IN_EXPORTS = {};
	
	//Built in Synchronizes
	public static final String[] BUILT_IN_SYNCHRONIZES = {};
	
	//Built in Runnable plugins
	public static final String[] BUILT_IN_RUNNABLES = {
//		SkinChangerPlugin.class.getCanonicalName(),
	};
	
	//Built in Panel plugins
	public static final String[] BUILT_IN_PANELS = {
	};
	
	//Built in transaction cell renderers
	public static final String[] BUILT_IN_TRANSACTION_CELL_RENDERERS = {
		DefaultTransactionCellRenderer.class.getCanonicalName(),
		SimpleTransactionCellRenderer.class.getCanonicalName(),
		ChequeTransactionCellRenderer.class.getCanonicalName(),
		ConciseTransactionCellRenderer.class.getCanonicalName(),
	};
	
	//Built in main screen tabs
	public static final String[] BUILT_IN_MAIN_FRAME_TABS = {};
	
	private Const(){}
}
