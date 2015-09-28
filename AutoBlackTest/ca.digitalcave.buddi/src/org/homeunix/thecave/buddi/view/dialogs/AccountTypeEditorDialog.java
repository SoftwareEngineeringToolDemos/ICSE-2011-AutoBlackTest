/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.AccountTypeListFrame;

import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossDialog;
import ca.digitalcave.moss.swing.MossHintTextField;

public class AccountTypeEditorDialog extends MossDialog implements ActionListener {

	public static final long serialVersionUID = 0;

	private final MossHintTextField name;
	private final JRadioButton credit;
	private final JRadioButton debit;

	private final JButton ok;
	private final JButton cancel;

	private final AccountType selected;
	private final Document model;

	public AccountTypeEditorDialog(AccountTypeListFrame frame, AccountType selected) {
		super(frame);

		this.selected = selected;
		this.model = (Document) frame.getDocument();

		name = new MossHintTextField(PrefsModel.getInstance().getTranslator().get(BuddiKeys.HINT_NAME));
		credit = new JRadioButton(PrefsModel.getInstance().getTranslator().get(BuddiKeys.CREDIT));
		debit = new JRadioButton(PrefsModel.getInstance().getTranslator().get(BuddiKeys.DEBIT));

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

		textPanelLeft.add(new JLabel(PrefsModel.getInstance().getTranslator().get(BuddiKeys.NAME)));
		textPanelLeft.add(new JLabel(PrefsModel.getInstance().getTranslator().get(BuddiKeys.DEBIT_CREDIT_TYPE)));
		textPanelLeft.add(new JLabel(""));

		textPanelRight.add(name);
		textPanelRight.add(debit);
		textPanelRight.add(credit);
		
		ButtonGroup group = new ButtonGroup();
		group.add(credit);
		group.add(debit);

		ok.setPreferredSize(InternalFormatter.getButtonSize(ok));
		cancel.setPreferredSize(InternalFormatter.getButtonSize(cancel));
		
		name.setPreferredSize(InternalFormatter.getComponentSize(cancel, 150));

		ok.addActionListener(this);
		cancel.addActionListener(this);

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
		credit.addFocusListener(focusListener);
		debit.addFocusListener(focusListener);

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
	}

	public void updateContent() {
		super.updateContent();

		if (selected == null){
			name.setText("");
			debit.setSelected(true);
		}
		else {
			name.setText(PrefsModel.getInstance().getTranslator().get(selected.getName()));
			if (selected.isCredit())
				credit.setSelected(true);
			else
				debit.setSelected(true);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(ok)){
			AccountType at;
			try {
				if (selected == null){
					at = ModelFactory.createAccountType(name.getText(), credit.isSelected());
					Logger.getLogger(this.getClass().getName()).finest("Created new AccountType " + at);

					model.addAccountType(at);
				}
				else {
					at = selected;
					at.setName(name.getText());
					at.setCredit(credit.isSelected());
				}
				
				closeWindow();
			}
			catch (ModelException me){
				String[] options = new String[1];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(this, 
								TextFormatter.getTranslation(BuddiKeys.ACCOUNT_TYPE_EDITOR_ERROR_UPDATING_ACCOUNT_TYPE), 
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
	}
}
