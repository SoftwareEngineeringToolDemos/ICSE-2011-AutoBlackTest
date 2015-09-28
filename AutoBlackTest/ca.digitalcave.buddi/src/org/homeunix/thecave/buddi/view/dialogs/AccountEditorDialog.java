/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossDecimalField;
import ca.digitalcave.moss.swing.MossDialog;
import ca.digitalcave.moss.swing.MossHintTextArea;
import ca.digitalcave.moss.swing.MossHintTextField;
import ca.digitalcave.moss.swing.model.BackedComboBoxModel;

public class AccountEditorDialog extends MossDialog implements ActionListener {

	public static final long serialVersionUID = 0;

	private final MossHintTextField name;
	private final JComboBox type;
	private final MossDecimalField startingBalance;
	private final MossHintTextArea notes;
	private final MossDecimalField overdraftCreditLimit;
	private final MossDecimalField interestRate;
	private final JLabel overdraftCreditLimitLabel;
	private final JLabel interestRateLabel;

	private final JButton ok;
	private final JButton cancel;

	private final Account selected;

	private final Document model;

	public AccountEditorDialog(MainFrame frame, Document model, Account selected) {
		super(frame);

		this.selected = selected;
		this.model = model;

		name = new MossHintTextField(PrefsModel.getInstance().getTranslator().get(BuddiKeys.HINT_NAME));
		type = new JComboBox(new BackedComboBoxModel<AccountType>(model.getAccountTypes()));
		if (type.getModel().getSize() > 0)
			type.setSelectedIndex(0);
		startingBalance = new MossDecimalField(true);
		overdraftCreditLimit = new MossDecimalField(false);
		interestRate = new MossDecimalField(0, false, 3);
		overdraftCreditLimitLabel = new JLabel();
		interestRateLabel = new JLabel(PrefsModel.getInstance().getTranslator().get(BuddiKeys.INTEREST_RATE));
		notes = new MossHintTextArea(PrefsModel.getInstance().getTranslator().get(BuddiKeys.HINT_NOTES));

		ok = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK));
		cancel = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CANCEL));
	}

	public void init() {
		super.init();

		JPanel textPanel = new JPanel(new BorderLayout());
		JPanel textPanelLeft = new JPanel(new GridLayout(0, 1));
		JPanel textPanelRight = new JPanel(new GridLayout(0, 1));
		textPanel.add(textPanelLeft, BorderLayout.WEST);
		textPanel.add(textPanelRight, BorderLayout.EAST);

		textPanelLeft.add(new JLabel(PrefsModel.getInstance().getTranslator().get(BuddiKeys.ACCOUNT_EDITOR_NAME)));
		textPanelLeft.add(new JLabel(PrefsModel.getInstance().getTranslator().get(BuddiKeys.ACCOUNT_EDITOR_TYPE)));
		textPanelLeft.add(new JLabel(PrefsModel.getInstance().getTranslator().get(BuddiKeys.ACCOUNT_EDITOR_STARTING_BALANCE)));
		textPanelRight.add(name);
		textPanelRight.add(type);
		textPanelRight.add(startingBalance);

		if (PrefsModel.getInstance().isShowOverdraft() || PrefsModel.getInstance().isShowCreditRemaining()){
			textPanelLeft.add(overdraftCreditLimitLabel);
			textPanelRight.add(overdraftCreditLimit);			
		}
		if (PrefsModel.getInstance().isShowInterestRates()){
			textPanelLeft.add(interestRateLabel);
			textPanelRight.add(interestRate);			
		}
		
		
		JScrollPane notesScroller = new JScrollPane(notes);
		notesScroller.setPreferredSize(new Dimension(150, 75));		
		textPanel.add(notesScroller, BorderLayout.SOUTH);

		ok.setPreferredSize(InternalFormatter.getButtonSize(ok));
		cancel.setPreferredSize(InternalFormatter.getButtonSize(cancel));

		ok.addActionListener(this);
		cancel.addActionListener(this);

		type.addActionListener(this);

		type.setRenderer(new DefaultListCellRenderer(){
			private static final long serialVersionUID = 0;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value instanceof AccountType){
					AccountType at = (AccountType) value;
					this.setText(PrefsModel.getInstance().getTranslator().get(at.getName()));
					
					if (isSelected)
						this.setForeground(Color.WHITE);
					else
						this.setForeground((at.isCredit() ? Color.RED : Const.COLOR_JLIST_UNSELECTED_TEXT));

				}
				else
					this.setText(" ");
				
				return this;
			}
		});

		name.addKeyListener(new KeyAdapter(){
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);

				updateButtons();
			}
		});

		FocusListener focusListener = new FocusListener(){
			public void focusGained(FocusEvent e) {}
			public void focusLost(FocusEvent e) {
				updateButtons();
			}
		};

		ok.addFocusListener(focusListener);
		cancel.addFocusListener(focusListener);
		name.addFocusListener(focusListener);
		type.addFocusListener(focusListener);
		startingBalance.addFocusListener(focusListener);
		notes.addFocusListener(focusListener);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		if (OperatingSystemUtil.isMac()){
			buttonPanel.add(cancel);			
			buttonPanel.add(ok);
		}
		else {
			buttonPanel.add(ok);
			buttonPanel.add(cancel);
		}

		this.getRootPane().setDefaultButton(ok);
		this.setLayout(new BorderLayout());
		this.add(textPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void updateButtons() {
		super.updateButtons();

		ok.setEnabled(name.getText() != null && name.getText().length() > 0);
		
		interestRate.setVisible(PrefsModel.getInstance().isShowInterestRates());
		interestRateLabel.setVisible(PrefsModel.getInstance().isShowInterestRates());
		
		if (((AccountType) type.getSelectedItem()).isCredit()){
			if (PrefsModel.getInstance().isShowCreditRemaining()){
				overdraftCreditLimitLabel.setText(TextFormatter.getTranslation(BuddiKeys.CREDIT_LIMIT));
				
				overdraftCreditLimit.setVisible(true);
				overdraftCreditLimitLabel.setVisible(true);
			}
			else {
				overdraftCreditLimit.setVisible(false);
				overdraftCreditLimitLabel.setVisible(false);				
			}
		}
		else {
			if (PrefsModel.getInstance().isShowOverdraft()){
				overdraftCreditLimitLabel.setText(TextFormatter.getTranslation(BuddiKeys.OVERDRAFT_LIMIT));
				
				overdraftCreditLimit.setVisible(true);
				overdraftCreditLimitLabel.setVisible(true);
			}
			else {
				overdraftCreditLimit.setVisible(false);
				overdraftCreditLimitLabel.setVisible(false);				
			}
		}
	}

	public void updateContent() {
		super.updateContent();

		if (selected == null){
			name.setText("");
//			type.setSelectedItem(null);
			startingBalance.setValue(0l);
			notes.setText("");
			overdraftCreditLimit.setValue(0l);
			interestRate.setValue(0l);
		}
		else {
			name.setText(PrefsModel.getInstance().getTranslator().get(selected.getName()));
			type.setSelectedItem(selected.getAccountType());
			startingBalance.setValue(selected.getStartingBalance() * (selected.getAccountType().isCredit() ? -1 : 1));
			notes.setText(PrefsModel.getInstance().getTranslator().get(selected.getNotes()));
			overdraftCreditLimit.setValue(selected.getOverdraftCreditLimit());
			interestRate.setValue(selected.getInterestRate());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(ok)){
			
			for (Account oldAccount : model.getAccounts()) {
				Object[] options = new Object[2];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);
				options[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);
				
				if (oldAccount.getName().equals(name.getText())
						&& (selected == null 
								|| !selected.equals(oldAccount))){
					int reply = JOptionPane.showOptionDialog(
							this, 
							TextFormatter.getTranslation(BuddiKeys.DUPLICATE_ACCOUNT_NAMES),
							TextFormatter.getTranslation(BuddiKeys.WARNING),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							options[0]);
					
					//Give the option to cancel save operation
					if (reply == JOptionPane.NO_OPTION)
						return;
					else if (reply ==JOptionPane.YES_OPTION)
						break;
				}
			}
			
			Account a;
			try {
				if (selected == null){
					a = ModelFactory.createAccount(name.getText(), (AccountType) type.getSelectedItem());
					a.setStartingBalance(startingBalance.getValue() * (((AccountType) type.getSelectedItem()).isCredit() ? -1 : 1));
					a.setNotes(notes.getText());
					a.setOverdraftCreditLimit(overdraftCreditLimit.getValue());
					a.setInterestRate(interestRate.getValue());
					Logger.getLogger(this.getClass().getName()).finest("Created new Account " + a);

					model.addAccount(a);
				}
				else {
					a = selected;
					a.setName(name.getText());
					a.setAccountType((AccountType) type.getSelectedItem());
					a.setStartingBalance(startingBalance.getValue() * (a.getAccountType().isCredit() ? -1 : 1));
					a.setNotes(notes.getText());
					a.setOverdraftCreditLimit(overdraftCreditLimit.getValue());
					a.setInterestRate(interestRate.getValue());
				}
				a.updateBalance();
								
				closeWindow();
			}
			catch (ModelException me){
				String[] options = new String[1];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(this, 
								TextFormatter.getTranslation(BuddiKeys.ACCOUNT_EDITOR_ERROR_UPDATING_ACCOUNT), 
								TextFormatter.getTranslation(BuddiKeys.ERROR), 
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE,
								null,
								options,
								options[0]);
			}
		}
		else if (e.getSource().equals(cancel)){
			closeWindow();
		}
		else if (e.getSource().equals(type)){
			updateButtons();
		}
	}
}
