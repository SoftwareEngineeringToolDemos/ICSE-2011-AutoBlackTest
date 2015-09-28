/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.prefs;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.Buddi;
import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiTranslator;
import org.homeunix.thecave.buddi.util.FileFunctions;

import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.common.Version;

public class PrefsModel {
	//Singleton Instance
	public static PrefsModel getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		private static PrefsModel instance = new PrefsModel();		
	}

	private PrefsModelBean prefsModel;
	private BuddiTranslator translate;
	
	//If you want to load from a custom location, set this static variable 
	// before you call the singleton getInstance() method. 
	private static File prefsFile = OperatingSystemUtil.getUserFile(Const.PROJECT_NAME, Const.PREFERENCE_FILE_NAME);

	private PrefsModel() {
		try {
			XMLDecoder prefsDecoder = new XMLDecoder(new FileInputStream(prefsFile));
			prefsModel = (PrefsModelBean) prefsDecoder.readObject();
			if (prefsModel == null)
				throw new Exception("Error loading preferences - creating new file.");
		}
		catch (RuntimeException re){
			newPrefsFile();
		}
		catch (Exception e){
			newPrefsFile();
		}
	}
	
	private void newPrefsFile(){
		//There was a problem loading the file; create a new one.
		prefsModel = new PrefsModelBean();
		prefsModel.setCurrencySign("$");
		prefsModel.setLanguage("English"); //TODO Prompt for language
		prefsModel.setNumberOfBackups(10);
		prefsModel.setShowAutoComplete(true);
		prefsModel.setShowDeleted(true);
		prefsModel.setSendCrashReports(true);
		prefsModel.setShowUpdateNotifications(true);
		prefsModel.setShowTooltips(true);
//		prefsModel.setMainWindowSize(new Dimension(640, 480));

		//Save the file
		save();
	}

	public void save() {
		if (prefsFile != null){
			try {
				if (!prefsFile.getParentFile().exists())
					prefsFile.getParentFile().mkdirs();
				XMLEncoder encoder = new XMLEncoder(new FileOutputStream(prefsFile));
				encoder.writeObject(prefsModel);
				encoder.flush();
				encoder.close();
			}
			catch (FileNotFoundException fnfe){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Problem saving preferences file: ", fnfe);
			}
		}
	}
	
	/**
	 * Streams the current Preferences object as an XML encoded string.  This is primarily meant
	 * for troubleshooting crashes. 
	 * @return
	 */
	public String saveToString(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(prefsModel);
		encoder.flush();
		encoder.close();
		
		return baos.toString();
	}

	public BuddiTranslator getTranslator(){
		if (translate == null)
			translate = new BuddiTranslator();
		return translate;
	}
	
	public int getAutosaveDelay() {
		return prefsModel.getAutosaveDelay();
	}

	public void setAutosaveDelay(int autosaveDelay) {
		prefsModel.setAutosaveDelay(autosaveDelay);
	}
	
	public boolean isShowFlatAccounts() {
		return prefsModel.isShowFlatAccounts();
	}
	public void setShowFlatAccounts(boolean showFlatAccounts) {
		prefsModel.setShowFlatAccounts(showFlatAccounts);
	}
	public boolean isShowFlatBudget() {
		return prefsModel.isShowFlatBudget();
	}
	public void setShowFlatBudget(boolean showFlatBudget) {
		prefsModel.setShowFlatBudget(showFlatBudget);
	}
	public boolean isShowFlatBudgetInSourceCombobox() {
		return prefsModel.isShowFlatBudgetInSourceCombobox();
	}
	public void setShowFlatBudgetInSourceCombobox(boolean showFlatBudgetInSourceCombobox) {
		prefsModel.setShowFlatBudgetInSourceCombobox(showFlatBudgetInSourceCombobox);
	}	
	
	public Version getLastVersion(){
		if (prefsModel.getLastVersion() == null)
			return null;
		return new Version(prefsModel.getLastVersion());
	}
	
	public void updateVersion(){
		prefsModel.setLastVersion(Buddi.getVersion().toString());
	}

	public String getCurrencySign() {
		return prefsModel.getCurrencySign();
	}

	public void setCurrencySign(String currencySign) {
		if (currencySign != null && currencySign.length() > 0)
			prefsModel.setCurrencySign(currencySign);
	}

	public String getDateFormat() {
		if (prefsModel.getDateFormat() == null || prefsModel.getDateFormat().length() == 0) 
			prefsModel.setDateFormat("yyyy/MM/dd");
		return prefsModel.getDateFormat();
	}

	public void setDateFormat(String dateFormat) {
		if (dateFormat != null && dateFormat.length() > 0)
			prefsModel.setDateFormat(dateFormat);
	}

	public String getLanguage() {
		return (prefsModel.getLanguage() != null && prefsModel.getLanguage().length() > 0
				? prefsModel.getLanguage()
						: "English");
	}

	public void setLanguage(String language) {
		if (language != null && language.length() > 0)
			prefsModel.setLanguage(language);
	}

	public List<File> getLastDataFiles() {
		if (prefsModel.getLastDataFiles() == null || prefsModel.getLastDataFiles().size() == 0)
			return null;
		List<File> files = new LinkedList<File>();
		for (String s : prefsModel.getLastDataFiles()) {
			files.add(new File(s));
		}
		return files;
	}

	public void setLastDataFiles(List<File> lastDataFiles) {
		if (lastDataFiles != null){
			List<String> strings = new LinkedList<String>();
			for (File file : lastDataFiles) {
				strings.add(file + "");
			}
			prefsModel.setLastDataFiles(strings);
		}
	}

//	public Dimension getMainWindowSize() {
//		return (prefsModel.getMainWindowSize() != null
//				? prefsModel.getMainWindowSize() 
//						: new Dimension(500, 300));
//	}
//
//	public Dimension getTransactionWindowSize() {
//		return (prefsModel.getTransactionWindowSize() != null
//				? prefsModel.getTransactionWindowSize() 
//						: new Dimension(600, 400));
//	}
//
//
//	public Dimension getScheduledTransactionWindowSize() {
//		return (prefsModel.getScheduledPlacement().getSize() != null
//				? prefsModel.getScheduledPlacement().getSize() 
//						: new Dimension(600, 400));
//	}
//	
//	public Dimension getReportWindowSize() {
//		return (prefsModel.getReportsPlacement().getSize() != null
//				? prefsModel.getReportsPlacement().getSize() 
//						: new Dimension(800, 400));
//	}
//
//	public Dimension getPreferencesWindowSize() {
//		return (prefsModel.getPrefsPlacement().getSize() != null
//				? prefsModel.getPrefsPlacement().getSize() 
//						: new Dimension(400, 300));
//	}
//
//	public Point getMainWindowLocation() {
//		return (prefsModel.getMainWindowLocation() != null
//				? prefsModel.getMainWindowLocation() 
//						: new Point(100, 100));
//	}
//
//	public Point getBudgetWindowLocation() {
//		return (prefsModel.getBudgetPlacement().getLocation() != null
//				? prefsModel.getBudgetPlacement().getLocation() 
//						: new Point(100, 100));
//	}
//
//	public Point getTransactionWindowLocation() {
//		return (prefsModel.getTransactionWindowLocation() != null
//				? prefsModel.getTransactionWindowLocation() 
//						: new Point(100, 100));
//	}
//
//	public Point getScheduledTransactionWindowLocation() {
//		return (prefsModel.getScheduledWindowLocation() != null
//				? prefsModel.getScheduledWindowLocation() 
//						: new Point(100, 100));
//	}
//
//	public Point getReportWindowLocation() {
//		return (prefsModel.getReportsPlacement().getLocation() != null
//				? prefsModel.getReportsPlacement().getLocation() 
//						: new Point(100, 100));
//	}
//	
//	public Point getPreferencesWindowLocation() {
//		return (prefsModel.getPreferencesWindowLocation() != null
//				? prefsModel.getPreferencesWindowLocation() 
//						: new Point(100, 100));
//	}
//
//	public void setMainWindowSize(Dimension size){
//		if (size != null)
//			prefsModel.setMainWindowSize(size);
//	}
//
//	public void setBudgetWindowSize(Dimension size){
//		if (size != null)
//			prefsModel.getBudgetPlacement().setSize(size);
//	}
//
//	public void setTransactionWindowSize(Dimension size){
//		if (size != null)
//			prefsModel.setTransactionWindowSize(size);
//	}
//	
//	public void setScheduledTransactionWindowSize(Dimension size){
//		if (size != null)
//			prefsModel.getScheduledPlacement().setSize(size);
//	}

//	public void setReportWindowSize(Dimension size){
//		if (size != null)
//			prefsModel.getReportsPlacement().setSize(size);
//	}
	
//	public void setPreferencesWindowSize(Dimension size){
//		if (size != null)
//			prefsModel.getPrefsPlacement().setSize(size);
//	}
//
//
//	public void setMainWindowLocation(Point location){
//		if (location != null)
//			prefsModel.setMainWindowLocation(location);
//	}
//
//	public void setBudgetWindowLocation(Point location){
//		if (location != null)
//			prefsModel.getBudgetPlacement().setLocation(location);
//	}
//
//	public void setTransactionWindowLocation(Point location){
//		if (location != null)
//			prefsModel.setTransactionWindowLocation(location);
//	}
//
//	public void setScheduledTransactionWindowLocation(Point location){
//		if (location != null)
//			prefsModel.setScheduledWindowLocation(location);
//	}
//
//	public void setReportWindowLocation(Point location){
//		if (location != null)
//			prefsModel.getReportsPlacement().setLocation(location);
//	}
//	
//	public void setPreferencesWindowLocation(Point location){
//		if (location != null)
//			prefsModel.setPreferencesWindowLocation(location);
//	}
//
	public int getNumberOfBackups() {
		return prefsModel.getNumberOfBackups();
	}

	public void setNumberOfBackups(int numberOfBackups) {
		prefsModel.setNumberOfBackups(numberOfBackups);
	}

	public String getProxyServer() {
		return (prefsModel.getProxyServer() != null ? prefsModel.getProxyServer() : "");
	}

	public void setProxyServer(String proxyServer) {
		if (proxyServer != null)
			prefsModel.setProxyServer(proxyServer);
	}
	
	public int getPort() {
		return prefsModel.getPort();
	}

	public void setPort(int port) {
		prefsModel.setPort(port);
	}


	public boolean isShowAutoComplete() {
		return prefsModel.isShowAutoComplete();
	}

	public void setShowAutoComplete(boolean showAutoComplete) {
		prefsModel.setShowAutoComplete(showAutoComplete);
	}

	public boolean isShowCleared() {
		return prefsModel.isShowCleared();
	}

	public void setShowCleared(boolean showCleared) {
		prefsModel.setShowCleared(showCleared);
	}

	public boolean isShowCurrencyAfterAmount() {
		return prefsModel.isShowCurrencyAfterAmount();
	}

	public void setShowCurrencyAfterAmount(boolean showCurrencyAfterAmount) {
		prefsModel.setShowCurrencyAfterAmount(showCurrencyAfterAmount);
	}

	public boolean isShowDeleted() {
		return prefsModel.isShowDeleted();
	}

	public void setShowDeleted(boolean showDeleted) {
		prefsModel.setShowDeleted(showDeleted);
	}

	public boolean isShowPromptAtStartup() {
		return prefsModel.isShowPromptAtStartup();
	}

	public void setShowPromptAtStartup(boolean showPromptAtStartup) {
		prefsModel.setShowPromptAtStartup(showPromptAtStartup);
	}

	public boolean isShowProxySettings() {
		return prefsModel.isShowProxySettings();
	}

	public void setShowProxySettings(boolean showProxySettings) {
		prefsModel.setShowProxySettings(showProxySettings);
	}

	public boolean isShowReconciled() {
		return prefsModel.isShowReconciled();
	}

	public void setShowReconciled(boolean showReconciled) {
		prefsModel.setShowReconciled(showReconciled);
	}

	public boolean isShowUpdateNotifications() {
		return prefsModel.isShowUpdateNotifications();
	}

	public void setShowUpdateNotifications(boolean showUpdateNotifications) {
		prefsModel.setShowUpdateNotifications(showUpdateNotifications);
	}

	public int getNumberOfBudgetColumns() {
		return prefsModel.getNumberOfBudgetColumns();
	}

	public void setNumberOfBudgetColumns(int numberOfBudgetColumns) {
		prefsModel.setNumberOfBudgetColumns(numberOfBudgetColumns);
	}

	public static void setPrefsFile(File file){
		if (file != null && FileFunctions.isFolderWritable(file)){
			PrefsModel.prefsFile = file;
		}
	}
	
	public boolean isSendCrashReports() {
		return prefsModel.isSendCrashReports();
	}

	public void setSendCrashReports(boolean sendCrashReports) {
		prefsModel.setSendCrashReports(sendCrashReports);
	}
	
	public String getPluginPreference(String key){
		return prefsModel.getPluginPreferences().get(key);
	}
	
	public void putPluginPreference(String key, String value){
		prefsModel.getPluginPreferences().put(key, value);
	}
	
	public List<String> getPluginListPreference(String key){
		if (prefsModel.getPluginListPreferences() == null)
			prefsModel.setPluginListPreferences(new HashMap<String, List<String>>());
		return prefsModel.getPluginListPreferences().get(key);
	}
	
	public void putPluginListPreference(String key, List<String> value){
		if (prefsModel.getPluginListPreferences() == null)
			prefsModel.setPluginListPreferences(new HashMap<String, List<String>>());
		prefsModel.getPluginListPreferences().put(key, value);
	}
	
	public boolean isDontShowNegativeSign() {
		return prefsModel.isShowNegativeSign();
	}

	public void setShowNegativeSign(boolean showNegativeSign) {
		prefsModel.setShowNegativeSign(showNegativeSign);
	}
	
	public boolean isShowCreditRemaining() {
		return prefsModel.isShowCreditRemaining();
	}

	public void setShowCreditRemaining(boolean showCreditRemaining) {
		prefsModel.setShowCreditRemaining(showCreditRemaining);
	}

	public boolean isShowOverdraft() {
		return prefsModel.isShowOverdraft();
	}

	public void setShowOverdraft(boolean showOverdraft) {
		prefsModel.setShowOverdraft(showOverdraft);
	}
	
	public boolean isShowTooltips() {
		return prefsModel.isShowTooltips();
	}

	public void setShowTooltips(boolean showTooltips) {
		prefsModel.setShowTooltips(showTooltips);
	}

	/**
	 * Associates a certain location with the given UID.  The UID will differ by window type, as
	 * defined below:
	 * 
	 * MainFrame: File.getAbsolutePath()
	 * TransactionFrame: File.getAbsolutePath() + Account.getFullName()
	 * Other: Window Name (About, Scheduled Transactions, etc).
	 * @param uid
	 * @param location
	 */
	public void putWindowLocation(String uid, Point location){
		prefsModel.getWindowLocation().put(uid, location);
	}
	
	/**
	 * Returns the window location associated with the given UID.  UID should be as follows:
	 * 
	 * MainFrame: File.getAbsolutePath()
	 * TransactionFrame: File.getAbsolutePath() + Account.getFullName()
	 * Other: Window Name (About, Scheduled Transactions, etc).
	 * @param uid
	 * @return
	 */
	public Point getWindowLocation(String uid){
		Point p = prefsModel.getWindowLocation().get(uid);
		if (p != null)
			return p;
		return new Point(100, 100);
	}
	
	/**
	 * Associates a certain size with the given UID.  The UID will differ by window type, as
	 * defined below:
	 * 
	 * MainFrame: File.getAbsolutePath()
	 * TransactionFrame: File.getAbsolutePath() + Account.getFullName()
	 * Other: Window Name (About, Scheduled Transactions, etc).
	 * @param uid
	 * @param size
	 */
	public void putWindowSize(String uid, Dimension size){
		prefsModel.getWindowSize().put(uid, size);
	}
	
	/**
	 * Returns the window size associated with the given UID.  UID should be as follows:
	 * 
	 * MainFrame: File.getAbsolutePath()
	 * TransactionFrame: File.getAbsolutePath() + Account.getFullName()
	 * Other: Window Name (About, Scheduled Transactions, etc).
	 * @param uid
	 * @return
	 */
	public Dimension getWindowSize(String uid){
		Dimension d = prefsModel.getWindowSize().get(uid);
		if (d != null)
			return d;
		return new Dimension(640, 480);
	}
	
	public boolean isSearchPaneVisible() {
		return prefsModel.isSearchPaneVisible();
	}

	public void setSearchPaneVisible(boolean searchPaneVisible) {
		prefsModel.setSearchPaneVisible(searchPaneVisible);
	}

	public boolean isTotalPaneVisible() {
		return prefsModel.isTotalPaneVisible();
	}

	public void setTotalPaneVisible(boolean totalPaneVisible) {
		prefsModel.setTotalPaneVisible(totalPaneVisible);
	}
	
	public String getTransactionCellRenderer() {
		return prefsModel.getTransactionCellRenderer();
	}

	public void setTransactionCellRenderer(String transactionCellRenderer) {
		prefsModel.setTransactionCellRenderer(transactionCellRenderer);
	}
	
	public String getClearedFilter() {
		return prefsModel.getClearedFilter();
	}

	public void setClearedFilter(String clearedFilter) {
		prefsModel.setClearedFilter(clearedFilter);
	}

	public String getDateFilter() {
		return prefsModel.getDateFilter();
	}

	public void setDateFilter(String dateFilter) {
		prefsModel.setDateFilter(dateFilter);
	}

	public String getReconciledFilter() {
		return prefsModel.getReconciledFilter();
	}

	public void setReconciledFilter(String reconciledFilter) {
		prefsModel.setReconciledFilter(reconciledFilter);
	}

	public String getSearchText() {
		return prefsModel.getSearchText();
	}

	public void setSearchText(String searchText) {
		prefsModel.setSearchText(searchText);
	}
	
	public int getLastDeleteOption() {
		return prefsModel.getLastDeleteOption();
	}
	public void setLastDeleteOption(int lastDeleteOption) {
		prefsModel.setLastDeleteOption(lastDeleteOption);
	}
	
	public String getAvailableVersion() {
		return prefsModel.getAvailableVersion();
	}
	public void setAvailableVersion(String availableVersion) {
		prefsModel.setAvailableVersion(availableVersion);
	}
	
	public boolean isShowInterestRates(){
		return prefsModel.isShowInterestRates();
	}
	
	public void setShowInterestRates(boolean showInterestRates){
		prefsModel.setShowInterestRates(showInterestRates);
	}
}

