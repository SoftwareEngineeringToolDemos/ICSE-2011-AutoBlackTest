/*
 * Created on Aug 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.model.swing.SourceComboBoxModel;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.TransactionFrame;
import org.homeunix.thecave.buddi.view.swing.SourceListCellRenderer;

import ca.digitalcave.moss.swing.MossDecimalField;
import ca.digitalcave.moss.swing.MossDialog;
import ca.digitalcave.moss.swing.MossScrollingComboBox;

/**
 * This class lets a user modify / create a list of split transactions.  When this is done, 
 * call the getSplits() method on it to return the list of splits as currently defined in it.
 * 
 * @author wyatt
 *
 */
public class SplitTransactionDialog extends MossDialog implements ActionListener {

	public static final long serialVersionUID = 0;

	private final JButton save;
	private final JButton cancel;
	private final JButton add;
	
	private final JLabel desired;
	private final JLabel current;
	private final JLabel difference;

	private final Document model;
	private final Source associatedSource;
	private final List<TransactionSplit> workingSplits;
	private List<TransactionSplit> completedSplits; //How we pass the splits back to the calling frame.  This is null if canceled, and working splits if saved.
	private final long amount; //The sum of all splits must equal this number.
	private final boolean from; //Is this for a To or From?  This will affect what types of BCs can be used.
	
	private final JPanel splitPanels;

	public SplitTransactionDialog(TransactionFrame frame, Document model, List<TransactionSplit> splits, Source associatedSource, long amount, boolean from) {
		super(frame, true);

		this.model = model;
		this.workingSplits = new ArrayList<TransactionSplit>(splits != null ? splits : new ArrayList<TransactionSplit>());
		this.associatedSource = associatedSource;
		this.amount = amount;
		this.from = from;
		
		this.desired = new JLabel();
		this.current = new JLabel();
		this.difference = new JLabel();

		splitPanels = new JPanel();
		
		save = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_SAVE));
		cancel = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CANCEL));
		add = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_ADD_SPLIT));		
	}

	public void init() {
		super.init();

		try {
			if (this.workingSplits.size() == 0)
				this.workingSplits.add(ModelFactory.createTransactionSplit(null, 0l));
		}
		catch (InvalidValueException ive){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error populating split list", ive);
		}

		
		this.setTitle(TextFormatter.getTranslation(BuddiKeys.SPLIT_EDITOR));
		this.setLayout(new BorderLayout());
		
		save.setPreferredSize(InternalFormatter.getButtonSize(save));
		cancel.setPreferredSize(InternalFormatter.getButtonSize(cancel));
		current.getPreferredSize().width = 200;
		desired.getPreferredSize().width = 200;
		difference.getPreferredSize().width = 200;
		
		splitPanels.setLayout(new GridBagLayout());
		this.add(splitPanels, BorderLayout.NORTH);

		JPanel saveCancelButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		addButtonPanel.add(add);
		saveCancelButtonsPanel.add(addButtonPanel);
		saveCancelButtonsPanel.add(cancel);
		saveCancelButtonsPanel.add(save);
		
		JPanel bottomPanel = new JPanel(new GridLayout(0, 2));
		
		bottomPanel.add(addButtonPanel);
		bottomPanel.add(saveCancelButtonsPanel);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		save.addActionListener(this);
		cancel.addActionListener(this);
		add.addActionListener(this);
		
		this.getRootPane().setDefaultButton(save);
	}

	public void updateButtons() {
		super.updateButtons();

		long amount = 0;
		boolean allSplitsValid = true;
		for (TransactionSplit split : workingSplits) {
			amount += split.getAmount();
			if (split.getSource() == null || split.getAmount() == 0l)
				allSplitsValid = false;
		}
		
		//The Add button is enabled only if there are no blank splits.  Thus, we can have at most
		// one blank row at a time.
		add.setEnabled(allSplitsValid);
		
		desired.setText(TextFormatter.getHtmlWrapper(TextFormatter.getFormattedCurrency(this.amount)));
		current.setText(TextFormatter.getHtmlWrapper(TextFormatter.getFormattedCurrency(amount)));
		difference.setText(TextFormatter.getHtmlWrapper(TextFormatter.getFormattedCurrency(this.amount - amount)));

		save.setEnabled(amount == this.amount && allSplitsValid);
	}

	/**
	 * In this class, updateContent() will force a complete refresh of all
	 * split entries.
	 */
	public void updateContent() {
		splitPanels.removeAll();
		
		splitPanels.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		for (TransactionSplit split : workingSplits) {
			final MossScrollingComboBox source = new MossScrollingComboBox();
			final MossDecimalField amount = new MossDecimalField();
			final TransactionSplit finalSplit = split;
			
			amount.addKeyListener(new KeyAdapter(){
				@Override
				public void keyTyped(KeyEvent e) {
					setAmount(source, finalSplit, amount);
					updateButtons();
				}
			});
			
			source.setPreferredSize(new Dimension(150, source.getPreferredSize().height));
			amount.setPreferredSize(new Dimension(100, amount.getPreferredSize().height));
			
			source.setModel(new SourceComboBoxModel(model, from, false, associatedSource));
			source.setRenderer(new SourceListCellRenderer(TextFormatter.getTranslation(BuddiKeys.HINT_SOURCE), source));
			
			source.setSelectedItem(split.getSource());
			amount.setValue(split.getAmount());

			c.weightx = 0.2;
			c.gridx = 0;
			splitPanels.add(source, c);
			
			c.weightx = 0.1;
			c.gridx = 1;
			splitPanels.add(amount, c);

			c.weightx = 0.0;
			c.gridx = 2;
			
			JButton remove = new JButton("-");
			remove.setFocusable(false);
			remove.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					workingSplits.remove(finalSplit);
					updateContent();
				}
			});
			
			FocusAdapter fa = new FocusAdapter(){
				@Override
				public void focusGained(FocusEvent e) {
					super.focusGained(e);
					updateButtons();
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					super.focusLost(e);
					setAmount(source, finalSplit, amount);
					updateButtons();
				}
			};
			source.addFocusListener(fa);
			amount.addFocusListener(fa);
			
			splitPanels.add(remove, c);
			source.requestFocusInWindow();
			
			c.gridy++;
		}
		
//		JPanel currentPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		JPanel desiredPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		JPanel differencePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		desiredPanel.add(desired);
//		currentPanel.add(current);
//		differencePanel.add(difference);

		c.anchor = GridBagConstraints.EAST;
		
		c.weightx = 0.2;
		c.gridx = 0;
		splitPanels.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.SPLIT_TOTAL)), c);
		c.weightx = 0.1;
		c.gridx = 1;
		splitPanels.add(current, c);
		c.gridy++;
		
		c.weightx = 0.2;
		c.gridx = 0;
		splitPanels.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.TRANSACTION_AMOUNT)), c);
		c.weightx = 0.1;
		c.gridx = 1;
		splitPanels.add(desired, c);
		c.gridy++;
		
		c.weightx = 0.2;
		c.gridx = 0;
		splitPanels.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.SPLIT_TRANSACTION_DIFFERENCE)), c);
		c.weightx = 0.1;
		c.gridx = 1;
		splitPanels.add(difference, c);
		
		//Force a refresh
		splitPanels.invalidate();
		
		this.pack();
		
		updateButtons();
	}
	
	private void setAmount(MossScrollingComboBox source, TransactionSplit finalSplit, MossDecimalField amount){
		try {
			if (source.getSelectedItem() instanceof Source){
				finalSplit.setSource((Source) source.getSelectedItem());
			}
			else {
				finalSplit.setSource(null);
			}
			finalSplit.setAmount(amount.getValue());
		}
		catch (InvalidValueException ive){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error setting split information", ive);
		}

	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(add)){
			try {
				workingSplits.add(ModelFactory.createTransactionSplit(null, 0l));
				updateContent();
			}
			catch (InvalidValueException ive){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error adding split", ive);
			}
		}
		else if (e.getSource().equals(cancel)){
			completedSplits = null;
			this.closeWindow();
		}
		else if (e.getSource().equals(save)){
			completedSplits = workingSplits;
			this.closeWindow();
		}
		else if (e.getActionCommand().equals("UPDATE")){
			updateButtons();
		}
	}
	
	public List<TransactionSplit> getSplits(){
		return completedSplits;
	}
}
