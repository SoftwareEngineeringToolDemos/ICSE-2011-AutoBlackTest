/*
 * Created on May 6, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.dialogs.AccountTypeEditorDialog;
import org.homeunix.thecave.buddi.view.menu.bars.BuddiMenuBar;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;
import ca.digitalcave.moss.common.ClassLoaderFunctions;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossAssociatedDocumentFrame;
import ca.digitalcave.moss.swing.exception.WindowOpenException;
import ca.digitalcave.moss.swing.model.BackedListModel;

public class AccountTypeListFrame extends MossAssociatedDocumentFrame implements ActionListener {
	public static final long serialVersionUID = 0;

	private final JButton doneButton;
	private final JButton newButton;
	private final JButton editButton;
	private final JButton deleteButton;

	private final Document model;
	private final JXList list;
	
	private final DocumentChangeListener listener;

	public AccountTypeListFrame(MainFrame parent){
		super(parent, AccountTypeListFrame.class.getName() + ((Document) parent.getDocument()).getUid() + "_" + parent.getDocument().getFile());
		this.setIconImage(ClassLoaderFunctions.getImageFromClasspath("img/BuddiFrameIcon.gif"));

		this.model = (Document) parent.getDocument();
		
		doneButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_DONE));
		newButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_NEW));
		editButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_EDIT));
		deleteButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_DELETE));

		final BackedListModel<AccountType> types = new BackedListModel<AccountType>(model.getAccountTypes());
		listener = new DocumentChangeListener(){
			public void documentChange(DocumentChangeEvent event) {
				types.fireListChanged();
			}
		};
		model.addDocumentChangeListener(listener);
		list = new JXList(types);
		list.addHighlighter(HighlighterFactory.createAlternateStriping(Const.COLOR_EVEN_ROW, Const.COLOR_ODD_ROW));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		Dimension buttonSize = new Dimension(Math.max(100, editButton.getPreferredSize().width), editButton.getPreferredSize().height);
		doneButton.setPreferredSize(buttonSize);
		newButton.setPreferredSize(buttonSize);
		editButton.setPreferredSize(buttonSize);
		deleteButton.setPreferredSize(buttonSize);

		JScrollPane listScroller = new JScrollPane(list);

		JPanel scrollPanel = new JPanel(new BorderLayout());
		scrollPanel.add(listScroller, BorderLayout.CENTER);

		JPanel editTransactionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		editTransactionsButtonPanel.add(newButton);
		editTransactionsButtonPanel.add(editButton);
		editTransactionsButtonPanel.add(deleteButton);

		scrollPanel.add(editTransactionsButtonPanel, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(doneButton);

		this.setTitle(TextFormatter.getTranslation(BuddiKeys.EDIT_ACCOUNT_TYPES));
		this.setLayout(new BorderLayout());
		this.add(scrollPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(doneButton);

		if (OperatingSystemUtil.isMac()){
			list.putClientProperty("Quaqua.List.style", "striped");
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
	}

	public void init() {
		super.init();

		newButton.addActionListener(this);
		editButton.addActionListener(this);
		doneButton.addActionListener(this);
		deleteButton.addActionListener(this);

		list.setCellRenderer(new DefaultListCellRenderer(){
			public static final long serialVersionUID = 0;
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				if (value instanceof AccountType){
					AccountType at = (AccountType) value;
					
					this.setText(at.getName());
					
					this.setForeground((at.isCredit() ? Color.RED : Const.COLOR_JLIST_UNSELECTED_TEXT));

				}
				
				return this;
			}
		});
		
		list.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				AccountTypeListFrame.this.updateButtons();
			}
		});
		
		list.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2)
					editButton.doClick();
			}
		});
		
		this.setJMenuBar(new BuddiMenuBar(this));
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(doneButton)){
			AccountTypeListFrame.this.setVisible(false);
			AccountTypeListFrame.this.closeWindow();
		}
		else if (e.getSource().equals(newButton)){
			try {
				new AccountTypeEditorDialog(this, null).openWindow();
				Logger.getLogger(this.getClass().getName()).finest("Done creating new Type from TypeModifyDialog");
				updateContent();
			}
			catch (WindowOpenException woe){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error when opening TypeModifyDialog window", woe);
			}
		}
		else if (e.getSource().equals(editButton)){
			Object o = list.getSelectedValue();
			if (o instanceof AccountType){
				try {
					AccountType t = (AccountType) o;
					new AccountTypeEditorDialog(this, t).openWindow();
					Logger.getLogger(this.getClass().getName()).finest("Done editing Type from TypeModifyDialog");
					updateContent();
				}
				catch (WindowOpenException woe){}
			}
		}
		else if (e.getSource().equals(deleteButton)){
			Object o = list.getSelectedValue();
			if (o instanceof AccountType){
				AccountType t = (AccountType) o;
				
				try {
					model.removeAccountType(t);
				}
				catch (ModelException me){
					String[] options = new String[1];
					options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

					JOptionPane.showOptionDialog(
							this,
							TextFormatter.getTranslation(BuddiKeys.MESSAGE_CANNOT_DELETE_TYPE),
							TextFormatter.getTranslation(BuddiKeys.ERROR),
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE,
							null,
							options,
							options[0]);
				}
				updateContent();
			}
		}
	}

	public void updateButtons(){
		super.updateButtons();

		editButton.setEnabled(list.getSelectedIndices().length > 0);
		deleteButton.setEnabled(list.getSelectedIndices().length > 0);
	}
}
