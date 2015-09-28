/*
 * Created on May 6, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.TransactionClearedFilterKeys;
import org.homeunix.thecave.buddi.i18n.keys.TransactionDateFilterKeys;
import org.homeunix.thecave.buddi.i18n.keys.TransactionReconciledFilterKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.impl.SplitImpl;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.model.swing.TransactionListModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiTransactionCellRendererPlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.plugin.builtin.cellrenderer.DefaultTransactionCellRenderer;
import org.homeunix.thecave.buddi.util.Formatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.menu.bars.BuddiMenuBar;
import org.homeunix.thecave.buddi.view.panels.TransactionEditorPanel;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;
import ca.digitalcave.moss.common.ClassLoaderFunctions;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossAssociatedDocumentFrame;
import ca.digitalcave.moss.swing.MossSearchField;
import ca.digitalcave.moss.swing.MossSearchField.SearchTextChangedEvent;
import ca.digitalcave.moss.swing.MossSearchField.SearchTextChangedEventListener;

public class TransactionFrame extends MossAssociatedDocumentFrame implements ActionListener {
	public static final long serialVersionUID = 0;	

	private static final int MIN_WIDTH = 400;
	private static final int MIN_HEIGHT = 200;

	private final JXList list;

	private final TransactionEditorPanel transactionEditor;
	private final JButton recordButton;
	private final JButton clearButton;
	private final JButton deleteButton;
	private final MossSearchField searchField;
	private final JComboBox dateFilterComboBox;
	private final JComboBox clearedFilterComboBox;
	private final JComboBox reconciledFilterComboBox;
	private final JLabel overdraftCreditLimit;

	private final JLabel balancesLabel;
	private final JLabel sumsLabel;
	private final JLabel accountBalance;
	private final JLabel clearedBalance;
	private final JLabel reconciledBalance;
	private final JLabel clearedSum;
	private final JLabel reconciledSum;
	private final JLabel notClearedSum;
	private final JLabel notReconciledSum;
	private final JPanel totalPanel;

	private final TransactionListModel listModel;

	private final Account associatedAccount;
	private final Source associatedSource; //Only used for updating title.
	private final MainFrame parent;

	private boolean disableListEvents = false;
	
	private final DocumentChangeListener listener;

	public TransactionFrame(MainFrame parent, Source source){
		super(parent, TransactionFrame.class.getName() + ((Document) parent.getDocument()).getUid() + "_" + parent.getDocument().getFile() + "_" + (source != null ? source.getFullName() : ""));
		this.setIconImage(ClassLoaderFunctions.getImageFromClasspath("img/BuddiFrameIcon.gif"));
		this.associatedSource = source;
		if (source instanceof Account)
			this.associatedAccount = (Account) source;
		else
			this.associatedAccount = null;
		this.listModel = new TransactionListModel((Document) parent.getDocument(), source);
		this.parent = parent;
		overdraftCreditLimit = new JLabel();

		totalPanel = new JPanel(new BorderLayout());

		dateFilterComboBox = new JComboBox();
		clearedFilterComboBox = new JComboBox();
		reconciledFilterComboBox = new JComboBox();

		balancesLabel = new JLabel();
		sumsLabel = new JLabel();
		accountBalance = new JLabel();
		clearedBalance = new JLabel();
		reconciledBalance = new JLabel();
		clearedSum = new JLabel();
		notClearedSum = new JLabel();
		reconciledSum = new JLabel();
		notReconciledSum = new JLabel();
		
		listener = new DocumentChangeListener(){
			public void documentChange(DocumentChangeEvent event) {
				listModel.update();
			}
		};

		//Set up the transaction list.  We don't set the model here, for performance reasons.
		// We set it after we have already established the prototype value.
		list = new JXList(){
			public final static long serialVersionUID = 0;

			@Override
			public String getToolTipText(MouseEvent event) {
				if (!PrefsModel.getInstance().isShowTooltips())
					return null;
				int i = locationToIndex(event.getPoint());
				if (i >= 0 && i < getModel().getSize()){
					Object o = getModel().getElementAt(i);

					if (o instanceof Transaction){
						Transaction transaction = (Transaction) o;

						if (transaction != null){
							StringBuilder sb = new StringBuilder();

							sb.append("<html>");
							sb.append(transaction.getDescription());

							if (transaction.getNumber() != null){
								if (transaction.getNumber().length() > 0){
									sb.append("<br>");
									sb.append("#");
									sb.append(transaction.getNumber());
								}
							}

							sb.append("<br>");
							if (TransactionFrame.this.associatedAccount != null){
								if (InternalFormatter.isRed(transaction, transaction.getTo().equals(TransactionFrame.this.associatedAccount)))
									sb.append("<font color='red'>");
								sb.append(TextFormatter.getFormattedCurrency(transaction.getAmount()));
								if (InternalFormatter.isRed(transaction, transaction.getTo().equals(TransactionFrame.this.associatedAccount)))
									sb.append("</font>");
							}

							sb.append("  ");
							sb.append(transaction.getFrom().getFullName())
							.append(" ")
							.append(PrefsModel.getInstance().getTranslator().get(BuddiKeys.TO))
							.append(" ")
							.append(transaction.getTo().getFullName());

							if (transaction.getMemo() != null 
									&& transaction.getMemo().length() > 0){
								sb.append("<br>");
								sb.append(transaction.getMemo());
							}

							sb.append("</html>");

							return sb.toString();
						}
					}
				}

				return "";
			}
		};


		//Set up the editing portion
		transactionEditor = new TransactionEditorPanel(this, (Document) parent.getDocument(), associatedSource, false);

		recordButton = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_RECORD));
		clearButton = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CLEAR));
		deleteButton = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_DELETE));
		searchField = new MossSearchField(OperatingSystemUtil.isMac() ? "" : PrefsModel.getInstance().getTranslator().get(BuddiKeys.DEFAULT_SEARCH));
	}

	/**
	 * Opens the window, and selects the given transaction.
	 * @param source
	 * @param transaction
	 */
	public TransactionFrame(MainFrame parent, Source source, Transaction transaction) {
		this(parent, source);

		// Iterate backwards through the list of transacations, looking for the transaction
		// which was just passed in.

		for (int index = this.listModel.getSize() - 1; index >= 0; --index) {
			Transaction transactionToCheck = (Transaction) this.listModel.getElementAt(index);
			if (transactionToCheck.equals(transaction)) {
				list.ensureIndexIsVisible(index);
				list.setSelectedIndex(index);
				break;
			}
		}

		this.requestFocusInWindow();
	}

	@Override
	public void initPostPack() {
		super.initPostPack();

		if (((Document) getDocument()).getBudgetCategories().size() > 0){
			try {
				list.setPrototypeCellValue(ModelFactory.createTransaction(new Date(), "Relatively long description", 12345678, ((Document) getDocument()).getBudgetCategories().get(0), ((Document) getDocument()).getBudgetCategories().get(0)));
			}
			catch (InvalidValueException ive){}
		}

		list.setModel(listModel);		
		list.ensureIndexIsVisible(listModel.getSize() - 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(){
		super.init();

		((Document) getDocument()).updateAllBalances();

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		BuddiTransactionCellRendererPlugin renderer = new DefaultTransactionCellRenderer();
		for (BuddiTransactionCellRendererPlugin r : (List<BuddiTransactionCellRendererPlugin>) BuddiPluginFactory.getPlugins(BuddiTransactionCellRendererPlugin.class)) {
			if (r.getClass().getCanonicalName().equals(PrefsModel.getInstance().getTransactionCellRenderer())){
				renderer = r;
				break;
			}
		}
		
		renderer.setAccount(associatedAccount);
		list.setCellRenderer(renderer);

		recordButton.setPreferredSize(InternalFormatter.getButtonSize(recordButton));
		clearButton.setPreferredSize(InternalFormatter.getButtonSize(clearButton));
		deleteButton.setPreferredSize(InternalFormatter.getButtonSize(deleteButton));
		searchField.setPreferredSize(InternalFormatter.getComponentSize(searchField, 160));
		searchField.setMaximumSize(searchField.getPreferredSize());
		dateFilterComboBox.setPreferredSize(InternalFormatter.getComboBoxSize(dateFilterComboBox));
		clearedFilterComboBox.setPreferredSize(InternalFormatter.getComboBoxSize(clearedFilterComboBox));
		reconciledFilterComboBox.setPreferredSize(InternalFormatter.getComboBoxSize(reconciledFilterComboBox));

		balancesLabel.setHorizontalAlignment(JLabel.RIGHT);
		sumsLabel.setHorizontalAlignment(JLabel.RIGHT);
		accountBalance.setHorizontalAlignment(JLabel.RIGHT);
		clearedBalance.setHorizontalAlignment(JLabel.RIGHT);
		reconciledBalance.setHorizontalAlignment(JLabel.RIGHT);
		clearedSum.setHorizontalAlignment(JLabel.RIGHT);
		notClearedSum.setHorizontalAlignment(JLabel.RIGHT);
		reconciledSum.setHorizontalAlignment(JLabel.RIGHT);
		notReconciledSum.setHorizontalAlignment(JLabel.RIGHT);


		JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		topRightPanel.add(clearedFilterComboBox);
		topRightPanel.add(reconciledFilterComboBox);
		topRightPanel.add(dateFilterComboBox);
		topRightPanel.add(searchField);

		JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		topLeftPanel.add(overdraftCreditLimit);

		JPanel topPanelHolder = new JPanel(new BorderLayout());
//		topPanelHolder.add(topLeftPanel, BorderLayout.NORTH);
		topPanelHolder.add(topRightPanel, BorderLayout.SOUTH);

		final JXCollapsiblePane topCollapsiblePanel = new JXCollapsiblePane(new BorderLayout());
		topCollapsiblePanel.setCollapsed(!PrefsModel.getInstance().isSearchPaneVisible());
		topCollapsiblePanel.add(topPanelHolder, BorderLayout.CENTER);

		final Icon collapsed = UIManager.getIcon("Tree.collapsedIcon");
		final Icon expanded = UIManager.getIcon("Tree.expandedIcon");
		final JLabel searchCheck = new JLabel(PrefsModel.getInstance().isSearchPaneVisible() ? expanded : collapsed);
		searchCheck.setVerticalAlignment(JLabel.TOP);
		//Disable the Enter key for the search field
		searchField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER){
		        	e.consume();
					return;
		        }
		        else
		        	super.keyPressed(e);
			}
		});
		searchCheck.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if (searchCheck.getIcon().equals(expanded))
					searchCheck.setIcon(collapsed);
				else
					searchCheck.setIcon(expanded);
				boolean hide = searchCheck.getIcon().equals(collapsed);
				topCollapsiblePanel.setCollapsed(hide);

				PrefsModel.getInstance().setSearchPaneVisible(!hide);
			}
		});

		JPanel spacerPanel = new JPanel(new BorderLayout());
		spacerPanel.add(topCollapsiblePanel, BorderLayout.NORTH);

		JPanel informationPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.ipadx = 10;
		
		gbc.gridx = 0;
		informationPanel.add(sumsLabel, gbc);
		
		gbc.gridx = 1;
		informationPanel.add(clearedSum, gbc);
		
		gbc.gridx = 2;
		informationPanel.add(reconciledSum, gbc);

		//Second row
		gbc.gridy = 1;
		
		gbc.gridx = 1;
		informationPanel.add(notClearedSum, gbc);
		
		gbc.gridx = 2;
		informationPanel.add(notReconciledSum, gbc);

		//Third row
		gbc.gridy = 2;
		
		gbc.gridx = 0;
		informationPanel.add(balancesLabel, gbc);
		
		gbc.gridx = 1;
		informationPanel.add(clearedBalance, gbc);
		
		gbc.gridx = 2;
		informationPanel.add(reconciledBalance, gbc);

		gbc.gridx = 3;
		informationPanel.add(accountBalance, gbc);



		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(searchCheck, BorderLayout.WEST);
		topPanel.add(topLeftPanel, BorderLayout.NORTH);
		topPanel.add(spacerPanel, BorderLayout.CENTER);		

//		JPanel informationLabelsPanel = new JPanel(new BorderLayout());
//		informationLabelsPanel.add(sumsLabel, BorderLayout.NORTH);
////		informationLabelsPanel.add(new JLabel(" "));
//		informationLabelsPanel.add(balancesLabel, BorderLayout.SOUTH);
//
//		JPanel accountInformationPanel = new JPanel(new BorderLayout());
////		accountInformationPanel.add(new JLabel(" "));
////		accountInformationPanel.add(new JLabel(" "));
//		accountInformationPanel.add(accountBalance, BorderLayout.SOUTH);
//
//		JPanel clearedInformationPanel = new JPanel(new VerticalLayout());
//		clearedInformationPanel.add(clearedSum);
//		clearedInformationPanel.add(notClearedSum);
//		clearedInformationPanel.add(clearedBalance);
//
//		JPanel reconciledInformationPanel = new JPanel(new VerticalLayout());
//		reconciledInformationPanel.add(reconciledSum);
//		reconciledInformationPanel.add(notReconciledSum);
//		reconciledInformationPanel.add(reconciledBalance);
//
//		JPanel clearedReconcileInformationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		clearedReconcileInformationPanel.add(informationLabelsPanel);
//		clearedReconcileInformationPanel.add(reconciledInformationPanel);
//		clearedReconcileInformationPanel.add(clearedInformationPanel);
//		clearedReconcileInformationPanel.add(accountInformationPanel);
//		clearedReconcileInformationPanel.setAlignmentY(JLabel.TOP_ALIGNMENT);
//		clearedReconcileInformationPanel.setAlignmentX(JLabel.TOP_ALIGNMENT);

		JPanel informationPanelHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		informationPanelHolder.add(informationPanel);
		
		final JXCollapsiblePane bottomCollapsiblePanel = new JXCollapsiblePane(new BorderLayout());
		bottomCollapsiblePanel.setCollapsed(!PrefsModel.getInstance().isTotalPaneVisible());
		bottomCollapsiblePanel.add(informationPanelHolder, BorderLayout.CENTER);

		final JLabel totalCheck = new JLabel(PrefsModel.getInstance().isTotalPaneVisible() ? expanded : collapsed);
		totalCheck.setVerticalAlignment(JLabel.TOP);
		totalCheck.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if (totalCheck.getIcon().equals(expanded))
					totalCheck.setIcon(collapsed);
				else
					totalCheck.setIcon(expanded);
				boolean hide = totalCheck.getIcon().equals(collapsed);
				bottomCollapsiblePanel.setCollapsed(hide);

				PrefsModel.getInstance().setTotalPaneVisible(!hide);
			}
		});

		JPanel totalSpacerPanel = new JPanel(new BorderLayout());
		totalSpacerPanel.add(bottomCollapsiblePanel, BorderLayout.NORTH);

		totalPanel.add(totalCheck, BorderLayout.WEST);
		totalPanel.add(totalSpacerPanel, BorderLayout.CENTER);

		this.getRootPane().setDefaultButton(recordButton);

		JPanel buttonPanelRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanelRight.add(clearButton);
		buttonPanelRight.add(recordButton);

		JPanel buttonPanelLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanelLeft.add(deleteButton);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(buttonPanelRight, BorderLayout.EAST);
		buttonPanel.add(buttonPanelLeft, BorderLayout.WEST);

		JScrollPane listScroller = new JScrollPane(list);

		JPanel scrollPanel = new JPanel(new BorderLayout());
		scrollPanel.add(listScroller, BorderLayout.CENTER);
		scrollPanel.add(transactionEditor, BorderLayout.SOUTH);

		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(scrollPanel, BorderLayout.CENTER);
		mainPanel.add(totalPanel, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

//		this.add(mainPanel, BorderLayout.CENTER);

		recordButton.addActionListener(this);
		clearButton.addActionListener(this);
		deleteButton.addActionListener(this);

		this.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				if (e.getComponent().getWidth() < MIN_WIDTH)
					e.getComponent().setSize(MIN_WIDTH, e.getComponent().getHeight());
				if (e.getComponent().getHeight() < MIN_HEIGHT)
					e.getComponent().setSize(e.getComponent().getWidth(), MIN_HEIGHT);
			}
		});

		list.addHighlighter(HighlighterFactory.createAlternateStriping(Const.COLOR_EVEN_ROW, Const.COLOR_ODD_ROW));

		if (OperatingSystemUtil.isMac()){
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		else {
			transactionEditor.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		}

		dateFilterComboBox.setModel(new DefaultComboBoxModel(TransactionDateFilterKeys.values()));
		dateFilterComboBox.setRenderer(new TranslatorListCellRenderer());

		clearedFilterComboBox.setModel(new DefaultComboBoxModel(TransactionClearedFilterKeys.values()));
		clearedFilterComboBox.setRenderer(new TranslatorListCellRenderer());

		reconciledFilterComboBox.setModel(new DefaultComboBoxModel(TransactionReconciledFilterKeys.values()));
		reconciledFilterComboBox.setRenderer(new TranslatorListCellRenderer());

		dateFilterComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (dateFilterComboBox.getSelectedItem() == null){
					if (e.getItem().equals(dateFilterComboBox.getItemAt(0))){
						dateFilterComboBox.setSelectedIndex(1);
					}
					dateFilterComboBox.setSelectedIndex(0);
				}
				if (e.getStateChange() == ItemEvent.SELECTED) {
					listModel.setDateFilter((TransactionDateFilterKeys) dateFilterComboBox.getSelectedItem());
				}
				TransactionFrame.this.updateContent();
				list.ensureIndexIsVisible(listModel.getSize() - 1);
			}			
		});
		clearedFilterComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (clearedFilterComboBox.getSelectedItem() == null){
					if (e.getItem().equals(clearedFilterComboBox.getItemAt(0))){
						clearedFilterComboBox.setSelectedIndex(1);
					}
					clearedFilterComboBox.setSelectedIndex(0);
				}
				if (e.getStateChange() == ItemEvent.SELECTED) {
					listModel.setClearedFilter((TransactionClearedFilterKeys) clearedFilterComboBox.getSelectedItem());
				}
				TransactionFrame.this.updateContent();
				list.ensureIndexIsVisible(listModel.getSize() - 1);
			}			
		});
		reconciledFilterComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (reconciledFilterComboBox.getSelectedItem() == null){
					if (e.getItem().equals(reconciledFilterComboBox.getItemAt(0))){
						reconciledFilterComboBox.setSelectedIndex(1);
					}
					reconciledFilterComboBox.setSelectedIndex(0);
				}
				if (e.getStateChange() == ItemEvent.SELECTED) {
					listModel.setReconciledFilter((TransactionReconciledFilterKeys) reconciledFilterComboBox.getSelectedItem());
				}
				TransactionFrame.this.updateContent();
				list.ensureIndexIsVisible(listModel.getSize() - 1);
			}			
		});

		searchField.addSearchTextChangedEventListener(new SearchTextChangedEventListener(){
			public void searchTextChangedEventOccurred(SearchTextChangedEvent evt) {
				listModel.setSearchText(searchField.getText());
				TransactionFrame.this.updateContent();
				list.ensureIndexIsVisible(listModel.getSize() - 1);
			}
		});

		list.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting() && !TransactionFrame.this.disableListEvents){
					//Check if the user has changed the selected transaction
					if (transactionEditor.isChanged() 
							&& transactionEditor.getTransaction() != (Transaction) list.getSelectedValue()){
						int ret;

						if (transactionEditor.isTransactionValid(TransactionFrame.this.associatedAccount)){
							String[] options = new String[2];
							options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_YES);
							options[1] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_NO);

							ret = JOptionPane.showOptionDialog(
									null, 
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.TRANSACTION_CHANGED_MESSAGE), 
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.TRANSACTION_CHANGED_TITLE),
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.PLAIN_MESSAGE,
									null,
									options,
									options[0]);
							if (ret == JOptionPane.YES_OPTION){
								recordButton.doClick();
							}
							else if (ret == JOptionPane.NO_OPTION){
								transactionEditor.setChanged(false);
//								list.setSelectedValue(editableTransaction.getTransaction(), true);
//								return;
							}
							else if (ret == JOptionPane.CANCEL_OPTION){
								transactionEditor.setChanged(false);
								list.setSelectedValue(transactionEditor.getTransaction(), true);
								transactionEditor.setChanged(true);
								return;
							}
						}
						else{
							String[] options = new String[2];
							options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_YES);
							options[1] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_NO);

							ret = JOptionPane.showOptionDialog(
									null, 
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.TRANSACTION_CHANGED_INVALID_MESSAGE), 
									PrefsModel.getInstance().getTranslator().get(BuddiKeys.TRANSACTION_CHANGED_TITLE),
									JOptionPane.YES_NO_OPTION,
									JOptionPane.PLAIN_MESSAGE,
									null,
									options,
									options[0]);
							if (ret == JOptionPane.NO_OPTION){
								transactionEditor.setChanged(false);

								if (transactionEditor.getTransaction() == null)
									list.clearSelection();
								else
									list.setSelectedValue(transactionEditor.getTransaction(), true);

								transactionEditor.setChanged(true);
								return;
							}
							else if (ret == JOptionPane.YES_OPTION){
								transactionEditor.setChanged(false);
							}
						}
					}

					if (list.getSelectedValue() instanceof Transaction) {
						Transaction t = (Transaction) list.getSelectedValue();

						transactionEditor.setTransaction(t, false, recordButton);

						Logger.getLogger(this.getClass().getName()).finest("Set transaction to " + t);
					}
					else if (list.getSelectedValue() == null){
						transactionEditor.setTransaction(null, false, recordButton);
						transactionEditor.updateContent();

						Logger.getLogger(this.getClass().getName()).finest("Set transaction to null");
					}

					updateButtons();
				}
			}
		});

		//Load state of filters
		if (PrefsModel.getInstance().getSearchText() != null){
			searchField.setText(PrefsModel.getInstance().getSearchText());
		}
		listModel.setSearchText(searchField.getText());
		TransactionFrame.this.updateContent();
		list.ensureIndexIsVisible(listModel.getSize() - 1);
			
		if (PrefsModel.getInstance().getDateFilter() != null){
			try {
				dateFilterComboBox.setSelectedItem(TransactionDateFilterKeys.valueOf(PrefsModel.getInstance().getDateFilter()));
			}
			catch (IllegalArgumentException iae){
				dateFilterComboBox.setSelectedItem(TransactionDateFilterKeys.TRANSACTION_FILTER_ALL_DATES);
			}
		}
		if (PrefsModel.getInstance().getReconciledFilter() != null){
			try {
				reconciledFilterComboBox.setSelectedItem(TransactionReconciledFilterKeys.valueOf(PrefsModel.getInstance().getReconciledFilter()));
			}
			catch (IllegalArgumentException iae){
				reconciledFilterComboBox.setSelectedItem(TransactionReconciledFilterKeys.TRANSACTION_FILTER_ALL_RECONCILED);
			}
		}
		if (PrefsModel.getInstance().getClearedFilter() != null){
			try {
				clearedFilterComboBox.setSelectedItem(TransactionClearedFilterKeys.valueOf(PrefsModel.getInstance().getClearedFilter()));
			}
			catch (IllegalArgumentException iae){
				clearedFilterComboBox.setSelectedItem(TransactionClearedFilterKeys.TRANSACTION_FILTER_ALL_CLEARED);
			}
		}
		
		getDocument().addDocumentChangeListener(listener);

		String dataFile = getDocument().getFile() == null ? "" : " - " + getDocument().getFile();
		this.setTitle(TextFormatter.getTranslation((associatedSource == null ? BuddiKeys.ALL_TRANSACTIONS : BuddiKeys.TRANSACTIONS)) 
				+ (associatedSource != null ? " - " + associatedSource.getFullName() : "") 
				+ dataFile + " - " + PrefsModel.getInstance().getTranslator().get(BuddiKeys.BUDDI));
		this.setJMenuBar(new BuddiMenuBar(this));
	}

	@Override
	public void closeWindowWithoutPrompting() {
		PrefsModel.getInstance().setSearchText(searchField.getText());
		PrefsModel.getInstance().setDateFilter(dateFilterComboBox.getSelectedItem() + "");
		PrefsModel.getInstance().setReconciledFilter(reconciledFilterComboBox.getSelectedItem() + "");
		PrefsModel.getInstance().setClearedFilter(clearedFilterComboBox.getSelectedItem() + "");
		
		PrefsModel.getInstance().putWindowSize(getDocument().getFile() + (associatedSource == null ? BuddiKeys.ALL_TRANSACTIONS.toString() : associatedSource.getFullName()), this.getSize());
		PrefsModel.getInstance().putWindowLocation(getDocument().getFile() + (associatedSource == null ? BuddiKeys.ALL_TRANSACTIONS.toString() : associatedSource.getFullName()), this.getLocation());
		PrefsModel.getInstance().save();

		super.closeWindowWithoutPrompting();
	}

	@Override
	public void updateContent() {
		super.updateContent();

		if (associatedAccount != null
				&& associatedAccount.getOverdraftCreditLimit() > 0
				&& ((PrefsModel.getInstance().isShowOverdraft() 
						&& !associatedAccount.getAccountType().isCredit())
						|| (PrefsModel.getInstance().isShowCreditRemaining() 
								&& associatedAccount.getAccountType().isCredit()))){
			long amountLeft;
			amountLeft = (associatedAccount.getOverdraftCreditLimit() + associatedAccount.getBalance());
			double percentLeft = ((double) amountLeft) / associatedAccount.getOverdraftCreditLimit() * 100.0;

			StringBuffer sb = new StringBuffer();
			sb.append("<html>");
			sb.append(
					TextFormatter.getTranslation(
							(associatedAccount.getAccountType().isCredit() ? 
									BuddiKeys.AVAILABLE_CREDIT 
									: BuddiKeys.AVAILABLE_FUNDS)))
									.append(": ")
									.append(InternalFormatter.isRed(associatedAccount, amountLeft) ? "<font color='red'>" : "")
									.append(TextFormatter.getFormattedCurrency(amountLeft))
									.append(InternalFormatter.isRed(associatedAccount, (long) percentLeft) ? "</font>" : "");
			if (associatedAccount.getAccountType().isCredit()){
				sb.append(" (")
				.append(Formatter.getDecimalFormat().format(percentLeft))
				.append("%)");
			}
			if (amountLeft < 0)
				sb.append("</font></html>");

			overdraftCreditLimit.setText(sb.toString());
			if (!associatedAccount.getAccountType().isCredit() && PrefsModel.getInstance().isShowTooltips())
				overdraftCreditLimit.setToolTipText(TextFormatter.getTranslation(BuddiKeys.TOOLTIP_AVAILABLE_FUNDS));
			overdraftCreditLimit.setVisible(true);
		}
		else {
			overdraftCreditLimit.setText("");
			overdraftCreditLimit.setToolTipText("");
			overdraftCreditLimit.setVisible(false);
		}

		//Update the cleared and reconciled totals at the bottom
		if (associatedSource != null){
			long clearedBalanceTotal = 0, clearedTotal = 0, notClearedTotal = 0;
			long reconciledBalanceTotal = 0, reconciledTotal = 0, notReconciledTotal = 0;

			if (associatedAccount != null){
				clearedBalanceTotal = associatedAccount.getStartingBalance();
				reconciledBalanceTotal = associatedAccount.getStartingBalance();
			}

			for (Transaction t : listModel) {
				if (!t.isDeleted()){
					if (t.getFrom().equals(associatedSource)){
						if (t.isClearedFrom()){
							clearedTotal -= t.getAmount();
							clearedBalanceTotal -= t.getAmount();
						}
						else
							notClearedTotal -= t.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));
						
						if (t.isReconciledFrom()){
							reconciledTotal -= t.getAmount();
							reconciledBalanceTotal -= t.getAmount();
						}
						else
							notReconciledTotal -= t.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));
					}
					else if (t.getTo().equals(associatedSource)){
						if (t.isClearedTo()){
							clearedTotal += t.getAmount();
							clearedBalanceTotal += t.getAmount();
						}
						else
							notClearedTotal += t.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));

						if (t.isReconciledTo()){
							reconciledTotal += t.getAmount();
							reconciledBalanceTotal += t.getAmount();
						}
						else
							notReconciledTotal += t.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));
					}
					else if (t.getFrom() instanceof SplitImpl){
						for (TransactionSplit split : t.getFromSplits()){
							if (split.getSource().equals(associatedSource)){
								if (t.isClearedFrom()){
									clearedTotal -= split.getAmount();
									clearedBalanceTotal -= split.getAmount();
								}
								else
									notClearedTotal -= split.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));
								
								if (t.isReconciledFrom()){
									reconciledTotal -= split.getAmount();
									reconciledBalanceTotal -= split.getAmount();
								}
								else
									notReconciledTotal -= split.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));
								
							}
						}
					}
					else if (t.getTo() instanceof SplitImpl){
						for (TransactionSplit split : t.getToSplits()){
							if (split.getSource().equals(associatedSource)){
								if (t.isClearedTo()){
									clearedTotal += split.getAmount();
									clearedBalanceTotal += split.getAmount();
								}
								else
									notClearedTotal += split.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));

								if (t.isReconciledTo()){
									reconciledTotal += split.getAmount();
									reconciledBalanceTotal += split.getAmount();
								}
								else
									notReconciledTotal += split.getAmount(); //(t.getAmount() * (t.isInflow() ? 1 : -1));
							}
						}
					}
					else {
						Logger.getLogger(this.getClass().getName()).severe("Neither TO nor FROM equals the associated source.  The value of TO is " + t.getTo().getFullName() + " and the value of FROM is " + t.getFrom().getFullName() + ", and associatedSource is " + associatedSource);
					}
				}
			}

			clearedBalance.setText("<html>" + TextFormatter.getTranslation(BuddiKeys.BALANCE_OF_TRANSACTIONS_CLEARED) + " " + TextFormatter.getFormattedCurrency(clearedBalanceTotal) + "</html>");
			clearedSum.setText("<html>" + TextFormatter.getTranslation(BuddiKeys.SUM_OF_TRANSACTIONS_CLEARED) + " " + TextFormatter.getFormattedCurrency(clearedTotal) + "</html>");
			notClearedSum.setText("<html>" + TextFormatter.getTranslation(BuddiKeys.SUM_OF_TRANSACTIONS_NOT_CLEARED) + " " + TextFormatter.getFormattedCurrency(notClearedTotal) + "</html>");
			
			reconciledBalance.setText("<html>" + TextFormatter.getTranslation(BuddiKeys.BALANCE_OF_TRANSACTIONS_RECONCILED) + " " + TextFormatter.getFormattedCurrency(reconciledBalanceTotal) + "</html>");
			reconciledSum.setText("<html>" + TextFormatter.getTranslation(BuddiKeys.SUM_OF_TRANSACTIONS_RECONCILED) + " " + TextFormatter.getFormattedCurrency(reconciledTotal) + "</html>");
			notReconciledSum.setText("<html>" + TextFormatter.getTranslation(BuddiKeys.SUM_OF_TRANSACTIONS_NOT_RECONCILED) + " " + TextFormatter.getFormattedCurrency(notReconciledTotal) + "</html>");			
		}


		//Set visibility of totals

		//Labels
		boolean accountNotFiltered = associatedAccount != null && !listModel.isListFiltered(); 
		boolean listFiltered = listModel.isListFiltered();
		boolean clearedOrReconciled = PrefsModel.getInstance().isShowCleared() || PrefsModel.getInstance().isShowReconciled();
		
		if (associatedAccount != null)
			accountBalance.setText("<html><b>" + (clearedOrReconciled ? TextFormatter.getTranslation(BuddiKeys.BALANCE_OF_ACCOUNT) : "") + " " + TextFormatter.getFormattedCurrency(associatedAccount.getBalance()) + "</b></html>");
		
		//Labels
		if (listFiltered)
			sumsLabel.setText("<html><b>" + TextFormatter.getTranslation(BuddiKeys.FILTERED_SUM_LABEL) + "</b></html>");
		else
			sumsLabel.setText("<html><b>" + TextFormatter.getTranslation(BuddiKeys.SUM_LABEL) + "</b></html>");
		balancesLabel.setText("<html><b>" + TextFormatter.getTranslation((clearedOrReconciled ? BuddiKeys.BALANCES_LABEL : BuddiKeys.BALANCE_LABEL)) + "</b></html>");

		balancesLabel.setVisible(accountNotFiltered);
		sumsLabel.setVisible(PrefsModel.getInstance().isShowCleared() || PrefsModel.getInstance().isShowReconciled());
		
		//Account
		accountBalance.setVisible(accountNotFiltered);

		//Reconciled
		reconciledBalance.setVisible(accountNotFiltered && PrefsModel.getInstance().isShowReconciled());
		reconciledSum.setVisible(PrefsModel.getInstance().isShowReconciled());
		notReconciledSum.setVisible(PrefsModel.getInstance().isShowReconciled());

		//Cleared
		clearedBalance.setVisible(accountNotFiltered && PrefsModel.getInstance().isShowCleared());
		clearedSum.setVisible(PrefsModel.getInstance().isShowCleared());
		notClearedSum.setVisible(PrefsModel.getInstance().isShowCleared());

		//Everything
		totalPanel.setVisible(accountNotFiltered || PrefsModel.getInstance().isShowCleared() || PrefsModel.getInstance().isShowReconciled());

		//Set visibility of filters
		clearedFilterComboBox.setVisible(PrefsModel.getInstance().isShowCleared());
		reconciledFilterComboBox.setVisible(PrefsModel.getInstance().isShowReconciled());

		this.repaint();
	}

	public void updateButtons(){
		super.updateButtons();

		if (transactionEditor == null 
				|| transactionEditor.getTransaction() == null){
			recordButton.setText(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_RECORD));
			clearButton.setText(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CLEAR));
			deleteButton.setEnabled(false);
		}
		else{
			recordButton.setText(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_UPDATE));
			clearButton.setText(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_NEW));
			deleteButton.setEnabled(true);
		}
		
		if (list.getSelectedValue() instanceof Transaction){
			if (((Transaction) list.getSelectedValue()).isDeleted())
				deleteButton.setText(TextFormatter.getTranslation(ButtonKeys.BUTTON_UNDELETE));
			else
				deleteButton.setText(TextFormatter.getTranslation(ButtonKeys.BUTTON_DELETE));
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(recordButton)){
			if (!transactionEditor.isTransactionValid(this.associatedSource)){
				String[] options = new String[1];
				options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(
						TransactionFrame.this,
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.RECORD_BUTTON_ERROR),
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.ERROR),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[0]
				);
				return;
			}

			//Don't fire updates for a while
			getDocument().startBatchChange();

			Transaction t;
			boolean isUpdate;

			if (recordButton.getText().equals(TextFormatter.getTranslation(ButtonKeys.BUTTON_UPDATE)))
				isUpdate = true;
			else if (recordButton.getText().equals(TextFormatter.getTranslation(ButtonKeys.BUTTON_RECORD)))
				isUpdate = false;
			else {
				Logger.getLogger(this.getClass().getName()).warning("Unknown record button state: " + recordButton.getText());
				return;
			}

			if (isUpdate && transactionEditor.isDangerouslyChanged()) {
				String[] options = new String[2];
				options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CREATE_NEW_TRANSACTION);
				options[1] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OVERWRITE_TRANSACTON);

				int ret = JOptionPane.showOptionDialog(
						null, 
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_CHANGE_EXISTING_TRANSACTION),
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.MESSAGE_CHANGE_EXISTING_TRANSACTION_TITLE),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						options[0]);

				if (ret == JOptionPane.YES_OPTION){ // create new transaction
					isUpdate = false;
				} // else continue with update
			}

			try {
				if(isUpdate) {
					t = transactionEditor.getTransactionUpdated();

				} else {
					t = transactionEditor.getTransactionNew();
					getDataModel().addTransaction(t);
				}
			}
			catch (ModelException me){
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error in record button in Transaction frame", me);
				return;
			}

			getDataModel().updateAllBalances();

			getDataModel().finishBatchChange();

			transactionEditor.setTransaction(null, true, recordButton);
			list.clearSelection();
			updateButtons();

			// Scroll the list view to make sure the newly-created transaction is visible.
			if (!isUpdate){
				Date currentTransactionDate = t.getDate();

				// Iterate backwards through the list of transacations, looking for a transaction
				// with the most recently inserted date, and scroll to that transaction's index.
				// For the common case of inserted new transactions near the current day, this
				// will be almost as fast as just scrolling to the end of the list, but always correct.
				for (int index = listModel.getSize() - 1; index >= 0; --index) {
					Transaction transactionToCheck = (Transaction) listModel.getElementAt(index);
					if (transactionToCheck.getDate().equals(currentTransactionDate)) {
						list.ensureIndexIsVisible(index);
						break;
					}
				}
			}

			disableListEvents = false;

			transactionEditor.resetSelection();
			transactionEditor.setChanged(false);

			listModel.update();
			this.updateContent();
			parent.updateContent();
		}
		else if (e.getSource().equals(clearButton)){
			String[] options = new String[2];
			options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_YES);
			options[1] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_NO);

			if (!transactionEditor.isChanged()
					|| JOptionPane.showOptionDialog(
							TransactionFrame.this,
							PrefsModel.getInstance().getTranslator().get(BuddiKeys.CLEAR_TRANSACTION_LOSE_CHANGES),
							PrefsModel.getInstance().getTranslator().get(BuddiKeys.CLEAR_TRANSACTION),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[0]) == JOptionPane.YES_OPTION){

				transactionEditor.setTransaction(null, true, recordButton);
				transactionEditor.updateContent();
				list.ensureIndexIsVisible(list.getModel().getSize() - 1);
				list.clearSelection();

				updateButtons();
			}
		}
		else if (e.getSource().equals(deleteButton)){
			if (deleteButton.getText().equals(TextFormatter.getTranslation(ButtonKeys.BUTTON_UNDELETE))){
				Transaction t = (Transaction) list.getSelectedValue();
				try {
					t.setDeleted(false);
				}
				catch (InvalidValueException ive){
					Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Invalid Value Exception", ive);
				}
				
				((Document) getDocument()).updateAllBalances();
				updateContent();
				this.repaint();
			}
			else if (deleteButton.getText().equals(TextFormatter.getTranslation(ButtonKeys.BUTTON_DELETE))){
				String[] options = new String[3];
				options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_VOID);
				options[1] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_DELETE);
				options[2] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CANCEL);

				int deleteVoid = JOptionPane.showOptionDialog(
						TransactionFrame.this, 
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.DELETE_TRANSACTION_OR_VOID_TRANSACTION),
						PrefsModel.getInstance().getTranslator().get(BuddiKeys.DELETE_TRANSACTION),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[PrefsModel.getInstance().getLastDeleteOption()]);

				//No means to Delete
				if (deleteVoid == JOptionPane.NO_OPTION){
					PrefsModel.getInstance().setLastDeleteOption(1);
					options = new String[2];
					options[0] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_YES);
					options[1] = PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_NO);

					if (JOptionPane.showOptionDialog(
							TransactionFrame.this, 
							PrefsModel.getInstance().getTranslator().get(BuddiKeys.DELETE_TRANSACTION_LOSE_CHANGES),
							PrefsModel.getInstance().getTranslator().get(BuddiKeys.DELETE_TRANSACTION),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[0]) == JOptionPane.YES_OPTION){

						Transaction t = (Transaction) list.getSelectedValue();
						int position = list.getSelectedIndex();
						try {
							((Document) getDocument()).removeTransaction(t);
							((Document) getDocument()).updateAllBalances();
//							baseModel.remove(t, listModel);

//							updateAllTransactionWindows();
							updateButtons();
//							MainFrame.getInstance().getAccountListPanel().updateContent();

							list.setSelectedIndex(position);
							if (list.getSelectedValue() instanceof Transaction){
								t = (Transaction) list.getSelectedValue();
								transactionEditor.setTransaction(t, true, recordButton);
								list.ensureIndexIsVisible(position);
							}
							else{
								transactionEditor.setTransaction(null, true, recordButton);
								list.clearSelection();
							}

							transactionEditor.setChanged(false);
						}
						catch (ModelException me){
							Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Model Exception", me);
						}
					}
				}
				//Yes is to Void.
				else if (deleteVoid == JOptionPane.YES_OPTION){
					PrefsModel.getInstance().setLastDeleteOption(0);
					if (list.getSelectedValue() instanceof Transaction){
						Transaction t = (Transaction) list.getSelectedValue();
						try {
							t.setDeleted(true);
						}
						catch (InvalidValueException ive){
							Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Invalid Value Exception", ive);
						}
						
						((Document) getDocument()).updateAllBalances();
						updateContent();
						this.repaint();
					}
				}
			}
		}
	}

	public void doClickRecord(){
		recordButton.doClick();
	}

	public void doClickClearAndAdvance(){
		transactionEditor.toggleCleared();
		advanceList();
	}	
	
	public void doClickReconcileAndAdvance(){
		transactionEditor.toggleReconciled();
		advanceList();
	}
	
	private void advanceList(){
		int newIndex = Math.min(list.getSelectedIndex() + 1, list.getModel().getSize() - 1);
		list.setSelectedIndex(newIndex);
	}

	public void doClickClear(){
		clearButton.doClick();
	}

	public void doClickDelete(){
		deleteButton.doClick();
	}

	private Document getDataModel(){
		return (Document) getDocument();
	}

	public void fireStructureChanged(){
		parent.fireStructureChanged();
	}

	/**
	 * Returns the associated account.  If this transaction frame was opened 
	 * by double clicking on an account, this will be the account which was
	 * clicked on.  If this transaction frame was opened by double clicking
	 * on a budget category, or by using the 'All Transactions' menu item,
	 * this will return null.
	 * @return
	 */
	public Account getAssociatedAccount(){
		return associatedAccount;
	}

	/**
	 * Returns the associated source.  If this transaction frame was opened 
	 * by double clicking on an account, this will be the account which was
	 * clicked on.  If this transaction frame was opened by double clicking
	 * on a budget category, this will be the budget category which was clicked
	 * on.  If this was opened by using the 'All Transactions' menu item (and
	 * thus is not associated with any source), this will return null.
	 * @return
	 */
	public Source getAssociatedSource(){
		return associatedSource;
	}
}