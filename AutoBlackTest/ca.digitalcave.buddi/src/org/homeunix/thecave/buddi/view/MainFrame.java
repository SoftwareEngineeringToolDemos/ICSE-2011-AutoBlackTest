/*
 * Created on Aug 19, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.DocumentImpl;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiPanelPlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.menu.bars.BuddiMenuBar;
import org.homeunix.thecave.buddi.view.menu.items.FileSave;
import org.homeunix.thecave.buddi.view.panels.MyAccountsPanel;
import org.homeunix.thecave.buddi.view.panels.MyBudgetPanel;
import org.homeunix.thecave.buddi.view.panels.MyReportsPanel;

import ca.digitalcave.moss.common.ClassLoaderFunctions;
import ca.digitalcave.moss.swing.ApplicationModel;
import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossFrame;

public class MainFrame extends MossDocumentFrame {
	public static final long serialVersionUID = 0;

	private final MyAccountsPanel myAccounts;
	private final MyBudgetPanel myBudget;
	private final MyReportsPanel myReports;
	
	private final JTabbedPane tabs;
	
	private List<BuddiPanelPlugin> panelPlugins;
	
	@SuppressWarnings("unchecked")
	public MainFrame(Document model) {
		super(model, MainFrame.class.getName() + model.getUid() + "_" + model.getFile());
		this.setIconImage(ClassLoaderFunctions.getImageFromClasspath("img/BuddiFrameIcon.gif"));
		myAccounts = new MyAccountsPanel(this);
		myBudget = new MyBudgetPanel(this);
		myReports = new MyReportsPanel(this);
		panelPlugins = (List<BuddiPanelPlugin>) BuddiPluginFactory.getPlugins(BuddiPanelPlugin.class);
		tabs = new JTabbedPane();
	}
	
	@Override
	public void init() {
		super.init();
		
		tabs.addTab(TextFormatter.getTranslation(BuddiKeys.MY_ACCOUNTS), myAccounts);
		tabs.addTab(TextFormatter.getTranslation(BuddiKeys.MY_BUDGET), myBudget);
		tabs.addTab(TextFormatter.getTranslation(BuddiKeys.MY_REPORTS), myReports);

		if (panelPlugins != null){
			for (BuddiPanelPlugin panelPlugin : panelPlugins){
				if (panelPlugin.isStartedAutomatically() || panelPlugin.wasInUse()){
					panelPlugin.setParentFrame(this);
					addPanel(TextFormatter.getTranslation(panelPlugin.getTabLabelKey()), panelPlugin);
				}
			}
		}
		
		this.setJMenuBar(new BuddiMenuBar(this));
		
		ComponentAdapter tabSelectListener = new ComponentAdapter(){
			@Override
			public void componentShown(ComponentEvent e) {
				super.componentShown(e);
				
				updateContent();
			}
		};
		
		myAccounts.addComponentListener(tabSelectListener);
		myBudget.addComponentListener(tabSelectListener);
		myReports.addComponentListener(tabSelectListener);
		
		if (panelPlugins != null){
			for (BuddiPanelPlugin panelPlugin : panelPlugins){
				panelPlugin.addComponentListener(tabSelectListener);
			}
		}
		
		this.add(tabs, BorderLayout.CENTER);
	}
	
	@Override
	public void updateContent() {
		super.updateContent();
		
		String dataFile = getDocument().getFile() == null ? "" : " - " + getDocument().getFile().getAbsolutePath();
		this.setTitle(TextFormatter.getTranslation(BuddiKeys.BUDDI) + dataFile);

		myAccounts.updateContent();
		myBudget.updateContent();
		myReports.updateContent();

		if (panelPlugins != null){
			for (BuddiPanelPlugin panelPlugin : panelPlugins){
				panelPlugin.updateContent();
			}
		}
		
		this.repaint();
	}
	
	@Override
	public boolean canClose() {
		if (getDocument().isChanged()){
			String[] buttons = new String[3];
			buttons[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_YES);
			buttons[1] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_NO);
			buttons[2] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CANCEL);

			int reply = JOptionPane.showOptionDialog(
					this, 
					PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_PROMPT_FOR_SAVE),
					PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_PROMPT_FOR_SAVE_TITLE),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					buttons,
					buttons[0]);

			//Question is "Do you want to save?"
			if (reply == JOptionPane.YES_OPTION){
				//We want to exit, but save first.
				new FileSave(this).doClick();
				return true;
			}
			else if (reply == JOptionPane.NO_OPTION){
				//Don't save, but exit
				return true;
			}
			else {
				//Cancel or anything else (click Clost button, esc, etc)
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Object closeWindow() {
		super.closeWindow();
		List<MossFrame> frames = ApplicationModel.getInstance().getOpenFrames();
		List<File> openFiles = new LinkedList<File>();
		for (MossFrame frame : frames) {
			if (frame instanceof MainFrame){
				openFiles.add(((MainFrame) frame).getDocument().getFile());
			}
		}
		if (openFiles.size() > 0)
			PrefsModel.getInstance().setLastDataFiles(openFiles);
		
		PrefsModel.getInstance().save();
		
		return null;
	}
	
	@Override
	public void closeWindowWithoutPrompting() {
		//Wait until we are finished saving, if applicable
		if (((DocumentImpl) getDocument()).isCurrentlySaving()){			
			try {
				((DocumentImpl) getDocument()).waitUntilFinishedSaving();
			}
			catch (InterruptedException ie) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Thread interrupted while waiting for save operation to complete.  Cancelling exit request.", ie);
				return;
			}
		}
		
		PrefsModel.getInstance().putWindowSize(this.getDocument().getFile() + "", this.getSize());
		PrefsModel.getInstance().putWindowLocation(this.getDocument().getFile() + "", this.getLocation());
		PrefsModel.getInstance().save();
		
		ModelFactory.getAutoSaveLocation(getDocument().getFile()).delete();
		
		super.closeWindowWithoutPrompting();
	}
	
	public List<BudgetCategory> getSelectedBudgetCategories(){
		if (isMyBudgetTabSelected())
			return myBudget.getSelectedBudgetCategories();
			
		return new LinkedList<BudgetCategory>();
	}
	
	/**
	 * Returns all budget categories associated with the currently selected 
	 * budget period type.  List does not dynamically update, so you should
	 * not cache the results and expect them to change; call this method 
	 * again if you need to access the data again.
	 * @return
	 */
	public List<BudgetCategory> getBudgetCategoriesInSelectedPeriod(){
		List<BudgetCategory> budgetCategories = new LinkedList<BudgetCategory>();
		BudgetCategoryType periodType = myBudget.getTreeTableModel().getSelectedBudgetPeriodType();
		
		for (BudgetCategory bc : ((Document) getDocument()).getBudgetCategories()) {
			if (bc.getBudgetPeriodType().equals(periodType))
				budgetCategories.add(bc);
		}
		
		return budgetCategories;
	}
	
	public MyAccountsPanel getMyAccountsPanel(){
		return myAccounts;
	}
	
	public MyBudgetPanel getMyBudgetPanel(){
		return myBudget;
	}
	
	public List<Account> getSelectedAccounts(){
		if (isMyAccountsTabSelected())
			return myAccounts.getSelectedAccounts();
		
		return new LinkedList<Account>();
	}
	
	public boolean isMyAccountsTabSelected(){
		return tabs.getSelectedIndex() == 0;
	}
	public boolean isMyBudgetTabSelected(){
		return tabs.getSelectedIndex() == 1;
	}
	public boolean isMyReportsTabSelected(){
		return tabs.getSelectedIndex() == 2;
	}
	
	public boolean isPanelOpen(BuddiPanelPlugin plugin){
		return (tabs.indexOfComponent(plugin) != -1);
	}

	public void addPanel(String title, BuddiPanelPlugin plugin){
		tabs.addTab(title, plugin);
	}

	public void removePanel(BuddiPanelPlugin plugin){
		tabs.removeTabAt(tabs.indexOfComponent(plugin));
	}
	
	/**
	 * Calls updateContent() for each open MainFrame object.
	 */
	public static void updateAllContent(){
		for (MossFrame frame : ApplicationModel.getInstance().getOpenFrames()) {
			frame.updateContent();
			if (frame instanceof MainFrame)
				((MainFrame) frame).fireStructureChanged();
		}
	}
	
	public void fireStructureChanged(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				myAccounts.fireStructureChanged();
				myBudget.fireStructureChanged();
			}
		});
	}
}
