/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.FilteredLists;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.model.swing.BudgetDateSpinnerModel;
import org.homeunix.thecave.buddi.model.swing.MyBudgetTreeTableModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.menu.items.EditEditTransactions;
import org.homeunix.thecave.buddi.view.swing.MyBudgetTableAmountCellEditor;
import org.homeunix.thecave.buddi.view.swing.MyBudgetTableAmountCellRenderer;
import org.homeunix.thecave.buddi.view.swing.MyBudgetTreeNameCellRenderer;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossDecimalField;
import ca.digitalcave.moss.swing.MossPanel;

public class MyBudgetPanel extends MossPanel implements ActionListener {
	public static final long serialVersionUID = 0;

	private final JXTreeTable tree;
	private final JLabel balanceLabel;

	private final BudgetDateSpinnerModel dateSpinnerModel;
	private final JSpinner dateSpinner;
	private final JComboBox periodTypeComboBox;

	private final MyBudgetTreeTableModel treeTableModel;
	
	private final Map<BudgetCategoryType, Date> periodDateMap = new HashMap<BudgetCategoryType, Date>();

	private final MainFrame parent;

	public MyBudgetPanel(MainFrame parent) {
		super(true);
		this.parent = parent;
		this.treeTableModel = new MyBudgetTreeTableModel((Document) parent.getDocument());
		tree = new JXTreeTable(){
			public static final long serialVersionUID = 0;
			@Override
			public String getToolTipText(MouseEvent event) {
				if (PrefsModel.getInstance().isShowTooltips()){
					Point p = event.getPoint();
					int rowIndex = rowAtPoint(p);

					if (this.getPathForRow(rowIndex) != null){
						Object[] path = this.getPathForRow(rowIndex).getPath();
						if (path != null && path.length > 0){
							Object node = path[path.length - 1];
							if (node instanceof BudgetCategory)
								return ((BudgetCategory) node).getNotes();
						}
					}
				}
				return null;
			}
		};
		tree.setColumnFactory(new ColumnFactory(){
			@Override
			public void configureTableColumn(TableModel arg0, TableColumnExt arg1) {
				super.configureTableColumn(arg0, arg1);
				
				MossDecimalField editor = new MossDecimalField(0, true, 2);
				arg1.setCellRenderer(new MyBudgetTableAmountCellRenderer());
				arg1.setCellEditor(new MyBudgetTableAmountCellEditor(editor));
			}
		});
		tree.setTreeTableModel(treeTableModel);

		balanceLabel = new JLabel();
		dateSpinnerModel = new BudgetDateSpinnerModel(treeTableModel);
		dateSpinner = new JSpinner(dateSpinnerModel);
//		dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), DateUtil.getDate(1900, Calendar.JANUARY), DateUtil.getDate(3000, Calendar.DECEMBER), Calendar.MONTH));
//		monthComboBox = new JComboBox(new DefaultComboBoxModel(MonthKeys.values()));
		periodTypeComboBox = new JComboBox(BudgetCategoryTypes.values());

		open();
	}

	public List<BudgetCategory> getSelectedBudgetCategories(){
		List<BudgetCategory> budgetCategories = new LinkedList<BudgetCategory>();

		for (Integer i : tree.getSelectedRows()) {
			budgetCategories.add((BudgetCategory) tree.getModel().getValueAt(i, -1));
		}

		return budgetCategories;
	}
	
	public void setNextPeriod(){
		dateSpinner.setValue(dateSpinnerModel.getNextValue());
	}
	
	public void setPreviousPeriod(){
		dateSpinner.setValue(dateSpinnerModel.getPreviousValue());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(periodTypeComboBox)){
			periodDateMap.put(treeTableModel.getSelectedBudgetPeriodType(), (Date) dateSpinnerModel.getValue());
			if (periodTypeComboBox != null && periodTypeComboBox.getSelectedItem() != null)
				treeTableModel.setSelectedBudgetPeriodType(ModelFactory.getBudgetCategoryType(periodTypeComboBox.getSelectedItem().toString()));
			
			if (periodDateMap.get(treeTableModel.getSelectedBudgetPeriodType()) == null)
				dateSpinnerModel.setValue(treeTableModel.getSelectedBudgetPeriodType().getStartOfBudgetPeriod(dateSpinnerModel.getDate()));
			else
				dateSpinnerModel.setValue(periodDateMap.get(treeTableModel.getSelectedBudgetPeriodType()));
			this.fireStructureChanged();
			this.updateContent();
			parent.updateMenus();
		}
	}

	public void init() {
		super.init();
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setAutoResizeMode(JXTreeTable.AUTO_RESIZE_ALL_COLUMNS);
		tree.setClosedIcon(null);
		tree.setOpenIcon(null);
		tree.setLeafIcon(null);
		tree.setTreeCellRenderer(new MyBudgetTreeNameCellRenderer());

//		for (int i = 1; i < treeTableModel.getColumnCount(); i++){			
			final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			//This passes arrow keys on to the table, to allow cell navigation.
			manager.addKeyEventDispatcher(new KeyEventDispatcher(){
//				private int lastColumnSelected = 2;
				
				public boolean dispatchKeyEvent(KeyEvent e) {
					if (MyBudgetPanel.this.parent.isActive()
							&& MyBudgetPanel.this.parent.isMyBudgetTabSelected()
							&& MyBudgetPanel.this.tree.getSelectedRow() != -1
							&& MyBudgetPanel.this.tree.getSelectedColumn() != -1
							&& e.getModifiers() == 0
							&& (e.getKeyCode() == KeyEvent.VK_UP
									|| e.getKeyCode() == KeyEvent.VK_DOWN
									|| e.getKeyCode() == KeyEvent.VK_RIGHT
									|| e.getKeyCode() == KeyEvent.VK_LEFT)){
						
//						if (e.getKeyCode() == KeyEvent.VK_RIGHT 
//								&& lastColumnSelected == 3
//								&& MyBudgetPanel.this.tree.getSelectedColumn() == 3){
//							Object previous = dateSpinnerModel.getPreviousValue();
//							if (previous instanceof Date){
////								treeTableModel.setSelectedDate((Date) previous);
//								dateSpinner.setValue(previous);
////								fireStructureChanged();
////								updateContent();
//							}
//						}
//						else if (e.getKeyCode() == KeyEvent.VK_LEFT
//								&& lastColumnSelected == 1
//								&& MyBudgetPanel.this.tree.getSelectedColumn() == 1){
//							Object next = dateSpinnerModel.getNextValue();
//							if (next instanceof Date){
////								treeTableModel.setSelectedDate((Date) next);
//								dateSpinner.setValue(next);
////								fireStructureChanged();
////								updateContent();
//							}		
//						}
//						
//						System.out.println(lastColumnSelected + ", " + MyBudgetPanel.this.tree.getSelectedColumn());
//						lastColumnSelected = MyBudgetPanel.this.tree.getSelectedColumn();
						
						manager.redispatchEvent(tree, e);
						e.consume();
						
						return true;
					}
					return false;
				}
			});
//			tree.getColumn(i).setCellRenderer(new MyBudgetTableAmountCellRenderer());
//			tree.getColumn(i).setCellEditor(new MyBudgetTableAmountCellEditor(editor));
//		}
//		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//		tree.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.addHighlighter(HighlighterFactory.createAlternateStriping(Const.COLOR_EVEN_ROW, Const.COLOR_ODD_ROW));

		tree.addTreeExpansionListener(new TreeExpansionListener(){
			public void treeCollapsed(TreeExpansionEvent event) {
				Object o = event.getPath().getPath()[event.getPath().getPath().length - 1];
				if (o instanceof BudgetCategory){
					BudgetCategory bc = (BudgetCategory) o;
					bc.setExpanded(false);
				}
			}
			public void treeExpanded(TreeExpansionEvent event) {
				Object o = event.getPath().getPath()[event.getPath().getPath().length - 1];
				if (o instanceof BudgetCategory){
					BudgetCategory bc = (BudgetCategory) o;
					bc.setExpanded(true);
				}				
			}
		});
		
		tree.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() >= 2)
					new EditEditTransactions(parent).doClick();
				super.mouseClicked(arg0);
			}
		});

		JScrollPane listScroller = new JScrollPane(tree);

//		dateSpinner.setPreferredSize(new Dimension(300, dateSpinner.getPreferredSize().height));
//		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "MMMM yyyy"));
		dateSpinner.setPreferredSize(InternalFormatter.getComponentSize(dateSpinner, 120));
		dateSpinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				if (dateSpinner.getValue() instanceof Date)
					treeTableModel.setSelectedDate((Date) dateSpinner.getValue());
				fireStructureChanged();
				updateContent();
			}
		});

//		periodTypeComboBox.setPreferredSize(dateSpinner.getPreferredSize());
		periodTypeComboBox.setSelectedItem(ModelFactory.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH));
		periodTypeComboBox.addActionListener(this);
		periodTypeComboBox.setRenderer(new TranslatorListCellRenderer());

		JPanel balanceLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		balanceLabelPanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.BUDGET_NET_INCOME)));
		balanceLabelPanel.add(periodTypeComboBox);
		balanceLabelPanel.add(balanceLabel);

		JPanel listScrollerPanel = new JPanel(new BorderLayout());
		listScrollerPanel.add(listScroller, BorderLayout.CENTER);
		listScrollerPanel.add(balanceLabelPanel, BorderLayout.SOUTH);

		JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		spinnerPanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.CURRENT_BUDGET_PERIOD)));
		spinnerPanel.add(dateSpinner);

//		JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		periodPanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.SHOW_BUDGET_CATEGORIES_FOR_PERIOD)));
//		periodPanel.add(periodTypeComboBox);

		JPanel topPanel = new JPanel(new BorderLayout());
//		topPanel.add(periodPanel, BorderLayout.EAST);
		topPanel.add(spinnerPanel, BorderLayout.EAST);

		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(listScrollerPanel, BorderLayout.CENTER);

		if (OperatingSystemUtil.isMac()){
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}

		tree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent arg0) {
				parent.updateContent();
//				tree.editCellAt(tree.getSelectedRow(), tree.getSelectedColumn());
			}
		});

		updateButtons();

//		String dataFile = model.getFile() == null ? "" : " - " + model.getFile();
//		this.setTitle(PrefsModel.getInstance().getTranslator().get(BuddiKeys.MY_BUDGET) + dataFile + " - " + PrefsModel.getInstance().getTranslator().get(BuddiKeys.BUDDI));
//		this.setJMenuBar(new BudgetFrameMenuBar(this));

		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
	}

	public void updateContent() {
		super.updateContent();

//		int rowSelected = tree.getSelectedRow();
//		int columnSelected = tree.getSelectedColumn();
		
		//Refresh the column headers.  For some stupid reason you have
		// to do it manually, even though the model itself updates and 
		// fires a change event.
		for (int i = 1; i < treeTableModel.getColumnCount(); i++)
			tree.getColumn(i).setHeaderValue(treeTableModel.getColumnName(i));

		//Update the date spinner
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, treeTableModel.getSelectedBudgetPeriodType().getDateFormat()));

		//Fire a change event on the table model.
//		treeTableModel.fireStructureChanged();

		//Update the balance label
		long budgetedNetIncome = 0;
		//We make a new linked list containing all of the filtered list to avoid the potential of a concurrent
		// modification exception.
		for (BudgetCategory bc : new LinkedList<BudgetCategory>(new FilteredLists.BudgetCategoryListFilteredByPeriodType(
				(Document) parent.getDocument(), 
				treeTableModel.getSelectedBudgetPeriodType()))) {
			budgetedNetIncome += (bc.getAmount(treeTableModel.getSelectedDate()) * (bc.isIncome() ? 1 : -1));
		}
		balanceLabel.setText(TextFormatter.getHtmlWrapper(TextFormatter.getFormattedCurrency(budgetedNetIncome)));

		//Restore the state of the expanded / unrolled nodes.
		for (BudgetCategory bc : ((Document) parent.getDocument()).getBudgetCategories()) {
			TreePath path = new TreePath(new Object[]{treeTableModel.getRoot(), bc});
			if (bc.isExpanded())
				tree.expandPath(path);
			else
				tree.collapsePath(path);
		}
		
		//Re-select the last cell
//		System.out.println(rowSelected + " - " + columnSelected);
//		tree.changeSelection(rowSelected, columnSelected, false, false);
		
		//Update the menus (especially to get correct Copy From / To dates)
		parent.updateMenus();
	}
	
	public MyBudgetTreeTableModel getTreeTableModel(){
		return treeTableModel;
	}
	
	public void fireStructureChanged(){
		treeTableModel.fireStructureChanged();
	}
}
