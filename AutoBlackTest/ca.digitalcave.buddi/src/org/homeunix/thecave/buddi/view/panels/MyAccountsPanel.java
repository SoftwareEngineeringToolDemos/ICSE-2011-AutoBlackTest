/*
 * Created on Jul 30, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.model.swing.MyAccountTreeTableModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.menu.items.EditEditTransactions;
import org.homeunix.thecave.buddi.view.swing.MyAccountTableAmountCellRenderer;
import org.homeunix.thecave.buddi.view.swing.MyAccountTreeNameCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossPanel;

public class MyAccountsPanel extends MossPanel {
	public static final long serialVersionUID = 0;

	private final JXTreeTable tree;
	private final JLabel balanceLabel;

	private final MyAccountTreeTableModel treeTableModel;

	private final MainFrame parent;

	private final DocumentChangeListener listener;

	public MyAccountsPanel(MainFrame parent) {
		super(true);
		this.parent = parent;
		this.treeTableModel = new MyAccountTreeTableModel((Document) parent.getDocument());

		listener = new DocumentChangeListener(){
			public void documentChange(DocumentChangeEvent event) {
				updateContent();
			}
		};

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
							if (node instanceof Account)
								return ((Account) node).getNotes();
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

				arg1.setCellRenderer(new MyAccountTableAmountCellRenderer((Document) MyAccountsPanel.this.parent.getDocument()));
			}
		});
		tree.setTreeTableModel(treeTableModel);
		balanceLabel = new JLabel("Change Me");

		open();
	}

	public List<Account> getSelectedAccounts(){
		List<Account> accounts = new LinkedList<Account>();

		for (Integer i : tree.getSelectedRows()) {
			if (tree.getModel().getValueAt(i, -1) instanceof Account)
				accounts.add((Account) tree.getModel().getValueAt(i, -1));
		}

		return accounts;
	}

	public List<AccountType> getSelectedTypes(){
		List<AccountType> types = new LinkedList<AccountType>();

		for (Integer i : tree.getSelectedRows()) {
			if (tree.getModel().getValueAt(i, -1) instanceof AccountType)
				types.add((AccountType) tree.getModel().getValueAt(i, -1));
		}

		return types;
	}

	public void actionPerformed(ActionEvent e) {

	}

	public void init() {
		super.init();

		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setAutoResizeMode(JXTreeTable.AUTO_RESIZE_ALL_COLUMNS);
		tree.setClosedIcon(null);
		tree.setOpenIcon(null);
		tree.setLeafIcon(null);
//		tree.setColumnSelectionAllowed(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addHighlighter(HighlighterFactory.createAlternateStriping(Const.COLOR_EVEN_ROW, Const.COLOR_ODD_ROW));		

//		int treeColumnWidth = 30;
//		tree.getColumn(0).setMaxWidth(treeColumnWidth);
//		tree.getColumn(0).setMinWidth(treeColumnWidth);
//		tree.getColumn(0).setPreferredWidth(treeColumnWidth);

		tree.setTreeCellRenderer(new MyAccountTreeNameCellRenderer());

		parent.getDocument().addDocumentChangeListener(listener);

		JScrollPane listScroller = new JScrollPane(tree);

		JPanel balanceLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		balanceLabelPanel.add(balanceLabel);

		JPanel listScrollerPanel = new JPanel(new BorderLayout());
		listScrollerPanel.add(listScroller, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(listScrollerPanel, BorderLayout.CENTER);
		mainPanel.add(balanceLabelPanel, BorderLayout.SOUTH);

		if (OperatingSystemUtil.isMac()){
			listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}

//		tree.addMouseListener(new MouseAdapter(){
//		@Override
//		public void mouseClicked(MouseEvent e) {
//		// TODO Auto-generated method stub
//		super.mouseClicked(e);
//		}
//		});

		tree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent arg0) {
				parent.updateButtons();
			}
		});

		tree.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() >= 2)
					new EditEditTransactions(parent).doClick();
				super.mouseClicked(arg0);
			}
		});

		tree.addTreeExpansionListener(new TreeExpansionListener(){
			public void treeCollapsed(TreeExpansionEvent event) {
				Object o = event.getPath().getPath()[event.getPath().getPath().length - 1];
				if (o instanceof AccountType){
					AccountType t = (AccountType) o;
					t.setExpanded(false);
				}
			}
			public void treeExpanded(TreeExpansionEvent event) {
				Object o = event.getPath().getPath()[event.getPath().getPath().length - 1];
				if (o instanceof AccountType){
					AccountType t = (AccountType) o;
					t.setExpanded(true);
				}				
			}
		});

		updateButtons();

//		this.setJMenuBar(new AccountFrameMenuBar(this));
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
	}

	public void updateContent() {
		super.updateContent();

		//Fire a change event on the table model.
//		treeTableModel.fireStructureChanged();

		//Restore the state of the expanded / unrolled nodes.
		for (AccountType t : ((Document) parent.getDocument()).getAccountTypes()) {
			TreePath path = new TreePath(new Object[]{treeTableModel.getRoot(), t});
			if (t.isExpanded())
				tree.expandPath(path);
			else
				tree.collapsePath(path);
		}

		long netWorth = ((Document) parent.getDocument()).getNetWorth(null);
		balanceLabel.setText(TextFormatter.getHtmlWrapper(
				PrefsModel.getInstance().getTranslator().get(BuddiKeys.NET_WORTH) 
				+ ": " 
				+ TextFormatter.getFormattedCurrency(netWorth)));

		tree.invalidate();
	}

	public void fireStructureChanged(){
		treeTableModel.fireStructureChanged();
	}
}
