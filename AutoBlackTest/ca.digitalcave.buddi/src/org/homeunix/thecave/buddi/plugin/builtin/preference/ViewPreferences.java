/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.preference;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

public class ViewPreferences extends BuddiPreferencePlugin {
	public static final long serialVersionUID = 0;
	
	private final JCheckBox showDeleted;
	private final JCheckBox showClear;
	private final JCheckBox showAutoComplete;
	private final JCheckBox showReconcile;
	private final JCheckBox showFlatAccounts;
	private final JCheckBox showFlatBudget;
	private final JCheckBox showFlatBudgetInSourceCombobox;
	private final JCheckBox showOverdraftLimit;
	private final JCheckBox showCreditLimit;
	private final JCheckBox showInterestRate;
	private final JCheckBox showTooltips;
	

	public ViewPreferences() {
		showDeleted = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_DELETED));
		showAutoComplete = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_AUTO_COMPLETE_TRANSACTION_INFORMATION));
		showClear = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_CLEAR));
		showReconcile = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_RECONCILE));
		showFlatAccounts = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_FLAT_ACCOUNTS));
		showFlatBudget = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_FLAT_BUDGET));
		showFlatBudgetInSourceCombobox = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_FLAT_BUDGET_IN_SOURCE_COMBOBOX));
		showOverdraftLimit = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_OVERDRAFT_LIMIT));
		showCreditLimit = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_CREDIT_LIMIT));
		showInterestRate = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_INTEREST_RATE));
		showTooltips = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.PREFERENCE_SHOW_TOOLTIPS));
	}

	@Override
	public JPanel getPreferencesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel autoCompletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel clearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel reconcilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel flatAccountsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel flatBudgetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel flatBudgetPanelInSourceCombobox = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel overdraftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel creditLimitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel interestRatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel tooltipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		deletePanel.add(showDeleted);
		autoCompletePanel.add(showAutoComplete);
		clearPanel.add(showClear);
		reconcilePanel.add(showReconcile);
		flatAccountsPanel.add(showFlatAccounts);
		flatBudgetPanel.add(showFlatBudget);
		flatBudgetPanelInSourceCombobox.add(showFlatBudgetInSourceCombobox);
		overdraftPanel.add(showOverdraftLimit);
		creditLimitPanel.add(showCreditLimit);
		interestRatePanel.add(showInterestRate);
		tooltipPanel.add(showTooltips);
		
		panel.add(autoCompletePanel);
		panel.add(clearPanel);
		panel.add(reconcilePanel);
		panel.add(deletePanel);
		panel.add(flatAccountsPanel);
		panel.add(flatBudgetPanel);
		panel.add(flatBudgetPanelInSourceCombobox);
		panel.add(overdraftPanel);
		panel.add(creditLimitPanel);
		panel.add(interestRatePanel);
		panel.add(tooltipPanel);
		panel.add(Box.createVerticalGlue());
		
		return panel;
	}
	
	public void load() {
		showDeleted.setSelected(PrefsModel.getInstance().isShowDeleted());
		showAutoComplete.setSelected(PrefsModel.getInstance().isShowAutoComplete());
		showClear.setSelected(PrefsModel.getInstance().isShowCleared());
		showReconcile.setSelected(PrefsModel.getInstance().isShowReconciled());
		showFlatAccounts.setSelected(PrefsModel.getInstance().isShowFlatAccounts());
		showFlatBudget.setSelected(PrefsModel.getInstance().isShowFlatBudget());
		showFlatBudgetInSourceCombobox.setSelected(PrefsModel.getInstance().isShowFlatBudgetInSourceCombobox());
		showOverdraftLimit.setSelected(PrefsModel.getInstance().isShowOverdraft());
		showCreditLimit.setSelected(PrefsModel.getInstance().isShowCreditRemaining());
		showInterestRate.setSelected(PrefsModel.getInstance().isShowInterestRates());
		showTooltips.setSelected(PrefsModel.getInstance().isShowTooltips());	
	}
	
	public boolean save() {
		boolean restart = false;
		
		PrefsModel.getInstance().setShowDeleted(showDeleted.isSelected());
		PrefsModel.getInstance().setShowAutoComplete(showAutoComplete.isSelected());
		PrefsModel.getInstance().setShowCleared(showClear.isSelected());
		PrefsModel.getInstance().setShowReconciled(showReconcile.isSelected());
		PrefsModel.getInstance().setShowFlatAccounts(showFlatAccounts.isSelected());
		PrefsModel.getInstance().setShowFlatBudget(showFlatBudget.isSelected());
		PrefsModel.getInstance().setShowFlatBudgetInSourceCombobox(showFlatBudgetInSourceCombobox.isSelected());
		PrefsModel.getInstance().setShowOverdraft(showOverdraftLimit.isSelected());
		PrefsModel.getInstance().setShowCreditRemaining(showCreditLimit.isSelected());
		PrefsModel.getInstance().setShowInterestRates(showInterestRate.isSelected());
		PrefsModel.getInstance().setShowTooltips(showTooltips.isSelected());
		
		return restart;
	}
	
	public String getName() {
		return BuddiKeys.VIEW.toString();
	}
}
