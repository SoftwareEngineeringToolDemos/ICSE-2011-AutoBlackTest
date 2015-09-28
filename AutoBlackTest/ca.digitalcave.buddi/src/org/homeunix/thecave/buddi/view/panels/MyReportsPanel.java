/*
 * Created on May 6, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.BuddiPluginHelper;
import org.homeunix.thecave.buddi.plugin.BuddiPluginHelper.DateChoice;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.homeunix.thecave.buddi.view.dialogs.CustomDateDialog;

import ca.digitalcave.moss.swing.MossPanel;
import ca.digitalcave.moss.swing.exception.WindowOpenException;

/**
 * @author wyatt
 */
public class MyReportsPanel extends MossPanel {
	public static final long serialVersionUID = 0;
	
	private final MainFrame parent;
	
	public MyReportsPanel(MainFrame parent){
		super(true);
		this.parent = parent;
		
		open();
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		super.init();
		
		JPanel pluginsPanel = new JPanel();
		pluginsPanel.setLayout(new BoxLayout(pluginsPanel, BoxLayout.Y_AXIS));
						
		for (BuddiReportPlugin report : (List<BuddiReportPlugin>) BuddiPluginFactory.getPlugins(BuddiReportPlugin.class)) {
			//Select the correct options for the dropdown, based on the plugin
			Vector<DateChoice> dateChoices;
			if (report.getDateRangeChoice().equals(PluginReportDateRangeChoices.INTERVAL))
				dateChoices = BuddiPluginHelper.getInterval((Document) parent.getDocument());
			else if (report.getDateRangeChoice().equals(PluginReportDateRangeChoices.START_ONLY))
				dateChoices = BuddiPluginHelper.getStartOnly((Document) parent.getDocument());
			else if (report.getDateRangeChoice().equals(PluginReportDateRangeChoices.END_ONLY))
				dateChoices = BuddiPluginHelper.getEndOnly((Document) parent.getDocument());
			else
				dateChoices = new Vector<DateChoice>();

			final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			final JLabel label = new JLabel(PrefsModel.getInstance().getTranslator().get(report.getDescription()));
			final JComboBox dateChooser = new JComboBox(dateChoices);
			dateChooser.setPreferredSize(InternalFormatter.getComboBoxSize(dateChooser));
			dateChooser.setRenderer(new DefaultListCellRenderer(){
				public static final long serialVersionUID = 0;
				
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value == null) 
						this.setText("<html><font color='gray'>" + TextFormatter.getTranslation(BuddiKeys.REPORTS_SELECT_DATE_RANGE) + "</font></html>");
					if (index == -1)
						this.setText(TextFormatter.getTranslation(BuddiKeys.REPORTS_SELECT_DATE_RANGE));
					return this;
				}
			});
				
			panel.add(label);
			panel.add(dateChooser);
			
			final BuddiReportPlugin finalReport = report;
			
			dateChooser.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					//Find out which item was clicked on
					Object o = dateChooser.getSelectedItem();

					//If the object was a date choice, access the encoded dates
					if (o instanceof DateChoice){
						DateChoice choice = (DateChoice) o;

						//As long as the choice is not custom, our job is easy
						if (!choice.isCustom()){
							//Launch a new reports window with given parameters							
							BuddiPluginHelper.openReport(parent, finalReport, choice.getStartDate(), choice.getEndDate());
						}
						//If they want a custom window, it's a little 
						// harder... we need to open the custom date
						// window, which then launches the plugin.
						else{
							try {
								new CustomDateDialog(parent, finalReport).openWindow();
							}
							catch (WindowOpenException woe){}
						}
					}

					dateChooser.setSelectedItem(null);
				}
			});
			
			if (report.isPluginActive())
				pluginsPanel.add(panel);
		}
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(pluginsPanel, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBorder(null);
		scrollPane.setOpaque(false);
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}
}
