/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.prefs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PrefsModelBean {
	//Window location, size
//	private Dimension mainWindowSize;
//	private Point mainWindowLocation;
//	private Dimension transactionWindowSize;
//	private Point transactionWindowLocation;
//	private Point scheduledWindowLocation;
//	private Point preferencesWindowLocation;
	
	//Plugin-saved information.  The plugin author will access this
	// via a string key.  The current implementation of
	// PluginPreferenceBean contains a single string; you can
	// extend this class to get more data if you wish.  Be sure
	// to create standard getters and setters for all data you wish
	// to save.
	private Map<String, String> pluginPreferences;
	private Map<String, List<String>> pluginListPreferences;
	
	//Data file information
	private List<String> lastDataFiles;
	private Map<String, Dimension> windowSize;
	private Map<String, Point> windowLocation;
	
	//State
	private String lastVersion;
	private String availableVersion;
	
	//Locale
	private String language;
	private String dateFormat;
	private String currencySign;
	private boolean showCurrencyAfterAmount;
	
	//View Options
	private boolean showDeleted;
	private boolean showAutoComplete;
	private boolean showCleared;
	private boolean showReconciled;
	private boolean showFlatAccounts;
	private boolean showFlatBudget;
	private boolean showOverdraft;
	private boolean showCreditRemaining;
	private boolean showInterestRates;
	private boolean showTooltips;
	private boolean showNegativeSign;
	private boolean showFlatBudgetInSourceCombobox;
	//Currently must be set to 4.  If we change this, we will need to change 
	// the menu item code which displays the dates to copy to / from, as well 
	// as other things.
	private int numberOfBudgetColumns; 
	
	//Network Options
	private boolean showProxySettings;
	private String proxyServer;
	private int port;
	//TODO get more options here
	
	//Advanced Options
	private int numberOfBackups = 10;
	private int autosaveDelay = 30;
	private boolean sendCrashReports;
	private boolean showUpdateNotifications;
	private boolean showPromptAtStartup;
	private String transactionCellRenderer; 
	
	//Transaction Pane collapsible panes
	private boolean searchPaneVisible;
	private boolean totalPaneVisible;
	private String searchText;
	private String dateFilter;
	private String clearedFilter;
	private String reconciledFilter;
	
	//Maintain option state
	private int lastDeleteOption = 0;
	
	//Define the max / min number of budget columns visible (not currently used)
	private int MIN_BUDGET_COLUMNS = 2;
	private int MAX_BUDGET_COLUMNS = 13;
	
	public int getNumberOfBudgetColumns() {
		if (numberOfBudgetColumns < MIN_BUDGET_COLUMNS || numberOfBudgetColumns > MAX_BUDGET_COLUMNS)
			numberOfBudgetColumns = 4;
		
		return numberOfBudgetColumns;
	}
	
	public Map<String, List<String>> getPluginListPreferences() {
		return pluginListPreferences;
	}

	public void setPluginListPreferences(
			Map<String, List<String>> pluginListPreferences) {
		this.pluginListPreferences = pluginListPreferences;
	}

	public boolean isShowTooltips() {
		return showTooltips;
	}

	public void setShowTooltips(boolean showTooltips) {
		this.showTooltips = showTooltips;
	}

	public boolean isShowFlatAccounts() {
		return showFlatAccounts;
	}
	public void setShowFlatAccounts(boolean showFlatAccounts) {
		this.showFlatAccounts = showFlatAccounts;
	}
	public boolean isShowFlatBudget() {
		return showFlatBudget;
	}
	public void setShowFlatBudget(boolean showFlatBudget) {
		this.showFlatBudget = showFlatBudget;
	}

	/**
	 * Sets the number of columns in the My Budget window.  For now, do 
	 * not change this, as we make certain assumptions about the number 
	 * of visible columns.  If we find a need to change this in the future,
	 * we may do so.  
	 * @param numberOfBudgetColumns
	 */
	public void setNumberOfBudgetColumns(int numberOfBudgetColumns) {
		if (numberOfBudgetColumns < MIN_BUDGET_COLUMNS)
			numberOfBudgetColumns = MIN_BUDGET_COLUMNS;
		if (numberOfBudgetColumns > MAX_BUDGET_COLUMNS)
			numberOfBudgetColumns = MAX_BUDGET_COLUMNS;
		this.numberOfBudgetColumns = numberOfBudgetColumns;
	}

	public String getCurrencySign() {
		return currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	public int getAutosaveDelay() {
		return autosaveDelay;
	}

	public void setAutosaveDelay(int autosaveDelay) {
		this.autosaveDelay = autosaveDelay;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public boolean isShowCreditRemaining() {
		return showCreditRemaining;
	}

	public void setShowCreditRemaining(boolean showCreditRemaining) {
		this.showCreditRemaining = showCreditRemaining;
	}

	public boolean isShowOverdraft() {
		return showOverdraft;
	}

	public void setShowOverdraft(boolean showOverdraft) {
		this.showOverdraft = showOverdraft;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getLastDataFiles() {
		return lastDataFiles;
	}

	public void setLastDataFiles(List<String> lastDataFiles) {
		this.lastDataFiles = lastDataFiles;
	}

	public int getNumberOfBackups() {
		return numberOfBackups;
	}

	public void setNumberOfBackups(int numberOfBackups) {
		this.numberOfBackups = numberOfBackups;
	}

	public String getProxyServer() {
		return proxyServer;
	}

	public void setProxyServer(String proxyServer) {
		this.proxyServer = proxyServer;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isShowAutoComplete() {
		return showAutoComplete;
	}

	public void setShowAutoComplete(boolean showAutoComplete) {
		this.showAutoComplete = showAutoComplete;
	}

	public boolean isShowCleared() {
		return showCleared;
	}

	public void setShowCleared(boolean showCleared) {
		this.showCleared = showCleared;
	}

	public boolean isShowCurrencyAfterAmount() {
		return showCurrencyAfterAmount;
	}

	public void setShowCurrencyAfterAmount(boolean showCurrencyAfterAmount) {
		this.showCurrencyAfterAmount = showCurrencyAfterAmount;
	}

	public boolean isShowDeleted() {
		return showDeleted;
	}

	public void setShowDeleted(boolean showDeleted) {
		this.showDeleted = showDeleted;
	}

	public boolean isShowPromptAtStartup() {
		return showPromptAtStartup;
	}

	public void setShowPromptAtStartup(boolean showPromptAtStartup) {
		this.showPromptAtStartup = showPromptAtStartup;
	}

	public boolean isShowProxySettings() {
		return showProxySettings;
	}

	public void setShowProxySettings(boolean showProxySettings) {
		this.showProxySettings = showProxySettings;
	}

	public boolean isShowReconciled() {
		return showReconciled;
	}

	public void setShowReconciled(boolean showReconciled) {
		this.showReconciled = showReconciled;
	}

//	public boolean isShowTypes() {
//		return showTypes;
//	}
//
//	public void setShowTypes(boolean showTypes) {
//		this.showTypes = showTypes;
//	}

	public boolean isShowUpdateNotifications() {
		return showUpdateNotifications;
	}

	public void setShowUpdateNotifications(boolean showUpdateNotifications) {
		this.showUpdateNotifications = showUpdateNotifications;
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}

	public boolean isSendCrashReports() {
		return sendCrashReports;
	}

	public void setSendCrashReports(boolean sendCrashReports) {
		this.sendCrashReports = sendCrashReports;
	}

//	public Point getMainWindowLocation() {
//		return mainWindowLocation;
//	}
//
//	public void setMainWindowLocation(Point mainWindowLocation) {
//		this.mainWindowLocation = mainWindowLocation;
//	}
//
//	public Dimension getMainWindowSize() {
//		return mainWindowSize;
//	}
//
//	public void setMainWindowSize(Dimension mainWindowSize) {
//		this.mainWindowSize = mainWindowSize;
//	}

	public Map<String, String> getPluginPreferences() {
		if (pluginPreferences == null)
			pluginPreferences = new HashMap<String, String>();
		return pluginPreferences;
	}

	public void setPluginPreferences(Map<String, String> pluginPreferences) {
		this.pluginPreferences = pluginPreferences;
	}

	public Map<String, Point> getWindowLocation() {
		if (windowLocation == null)
			windowLocation = new HashMap<String, Point>();
		return windowLocation;
	}

	public void setWindowLocation(Map<String, Point> windowLocation) {
		this.windowLocation = windowLocation;
	}

	public Map<String, Dimension> getWindowSize() {
		if (windowSize == null)
			windowSize = new HashMap<String, Dimension>();
		return windowSize;
	}

	public void setWindowSize(Map<String, Dimension> windowSize) {
		this.windowSize = windowSize;
	}

	public boolean isShowNegativeSign() {
		return showNegativeSign;
	}

	public void setShowNegativeSign(boolean showNegativeSign) {
		this.showNegativeSign = showNegativeSign;
	}

	public boolean isSearchPaneVisible() {
		return searchPaneVisible;
	}

	public void setSearchPaneVisible(boolean searchPaneVisible) {
		this.searchPaneVisible = searchPaneVisible;
	}

	public boolean isTotalPaneVisible() {
		return totalPaneVisible;
	}

	public void setTotalPaneVisible(boolean totalPaneVisible) {
		this.totalPaneVisible = totalPaneVisible;
	}

	public String getTransactionCellRenderer() {
		return transactionCellRenderer;
	}

	public void setTransactionCellRenderer(String transactionCellRenderer) {
		this.transactionCellRenderer = transactionCellRenderer;
	}

	public String getClearedFilter() {
		return clearedFilter;
	}

	public void setClearedFilter(String clearedFilter) {
		this.clearedFilter = clearedFilter;
	}

	public String getDateFilter() {
		return dateFilter;
	}

	public void setDateFilter(String dateFilter) {
		this.dateFilter = dateFilter;
	}

	public String getReconciledFilter() {
		return reconciledFilter;
	}

	public void setReconciledFilter(String reconciledFilter) {
		this.reconciledFilter = reconciledFilter;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public int getLastDeleteOption() {
		return lastDeleteOption;
	}
	public void setLastDeleteOption(int lastDeleteOption) {
		this.lastDeleteOption = lastDeleteOption;
	}
	
	public String getAvailableVersion() {
		return availableVersion;
	}
	public void setAvailableVersion(String availableVersion) {
		this.availableVersion = availableVersion;
	}
	
	public boolean isShowInterestRates() {
		return showInterestRates;
	}
	public void setShowInterestRates(boolean showInterestRates) {
		this.showInterestRates = showInterestRates;
	}
	public boolean isShowFlatBudgetInSourceCombobox() {
		return showFlatBudgetInSourceCombobox;
	}
	public void setShowFlatBudgetInSourceCombobox(
			boolean showFlatBudgetInSourceCombobox) {
		this.showFlatBudgetInSourceCombobox = showFlatBudgetInSourceCombobox;
	}
}
