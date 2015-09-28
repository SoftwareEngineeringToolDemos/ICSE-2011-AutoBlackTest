/*
 * Created on May 20, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginHelper;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;
import org.jdesktop.swingx.JXDatePicker;

import ca.digitalcave.moss.common.DateUtil;
import ca.digitalcave.moss.swing.MossDialog;

/**
 * The dialog which allows users to choose a custom date range, or
 * start / end date only.  Designed to launch a plugin when the user 
 * has chosen the date.
 * 
 * @author wyatt
 *
 */
public class CustomDateDialog extends MossDialog implements ActionListener {
	public static final long serialVersionUID = 0;

	protected final JButton okButton;
	protected final JButton cancelButton;

	protected final JLabel mainLabel;
	protected final JLabel middleLabel;

	protected final JXDatePicker startDateChooser;
	protected final JXDatePicker endDateChooser;

	protected final BuddiReportPlugin report;
	private final MainFrame reportFrame;
	
	private static Date startDate = null; //We initialize this to null to get an approximate guess for the first time it is run.  After that, we remember the value.
	private static Date endDate = new Date();

	public CustomDateDialog(MainFrame reportFrame, BuddiReportPlugin plugin){
		super(reportFrame);
		
		this.reportFrame = reportFrame;
		this.report = plugin;

		okButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_OK));
		cancelButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL));

		Dimension buttonSize = new Dimension(100, okButton.getPreferredSize().height);
		okButton.setPreferredSize(buttonSize);
		cancelButton.setPreferredSize(buttonSize);

		startDateChooser = new JXDatePicker();
		endDateChooser = new JXDatePicker();

		startDateChooser.setEditor(new JFormattedTextField(new SimpleDateFormat(PrefsModel.getInstance().getDateFormat())));
		endDateChooser.setEditor(new JFormattedTextField(new SimpleDateFormat(PrefsModel.getInstance().getDateFormat())));

		Dimension textFieldSize = new Dimension(180, startDateChooser.getPreferredSize().height);

		startDateChooser.setPreferredSize(textFieldSize);
		endDateChooser.setPreferredSize(textFieldSize);

		JPanel r1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel r2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		mainLabel = new JLabel();
		middleLabel = new JLabel(); 

		r1.add(mainLabel);

		r2.add(startDateChooser);
		r2.add(middleLabel);
		r2.add(endDateChooser);

		JPanel textPanelSpacer = new JPanel();
		textPanelSpacer.setLayout(new BoxLayout(textPanelSpacer, BoxLayout.Y_AXIS));
		textPanelSpacer.setBorder(BorderFactory.createEmptyBorder(7, 17, 17, 17));
		textPanelSpacer.add(r1);
		textPanelSpacer.add(r2);

		JPanel mainBorderPanel = new JPanel();
		mainBorderPanel.setLayout(new BorderLayout());
		mainBorderPanel.setBorder(BorderFactory.createTitledBorder(""));
		mainBorderPanel.add(textPanelSpacer);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);

		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(7, 12, 12, 12));

		mainPanel.add(mainBorderPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(mainPanel);
		this.getRootPane().setDefaultButton(okButton);
		this.setTitle(TextFormatter.getTranslation(BuddiKeys.CHOOSE_DATE_INTERVAL));

		if (startDate == null){
			//Set the start date to be (by default) a time in the past
			// back as far as the current interval.  For instance, if the 
			// interval is set to 1 month, and the day is Feb 15, set the 
			// date to Jan 1.
			// This is not guaranteed to give perfect results every time,
			// but if we are giving the user a choice in the date interva;,
			// we may as well start at a time in the past rather than just
			// give them the current date, as we have done before now.
			// Added to address feature request #1649972.
			BudgetCategoryType period = ModelFactory.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH);
			startDate = period.getStartOfBudgetPeriod(new Date());
		}
		
		startDateChooser.setDate(startDate);			
		endDateChooser.setDate(endDate);

		setVisibility(plugin.getDateRangeChoice());
	}

	private void setVisibility(PluginReportDateRangeChoices rangeChoiceType){
		if (rangeChoiceType.equals(PluginReportDateRangeChoices.INTERVAL)){
			mainLabel.setText(TextFormatter.getTranslation(BuddiKeys.REPORT_BETWEEN));
//			middleLabel.setText(TextFormatter.getTranslation(TranslateKeys.AND));					
			middleLabel.setText(" - ");
		}
		else{
			mainLabel.setText(TextFormatter.getTranslation(BuddiKeys.REPORT_AS_OF_DATE));
			middleLabel.setVisible(false);

			if (rangeChoiceType.equals(PluginReportDateRangeChoices.START_ONLY)){
				endDateChooser.setVisible(false);
			}
			else if (rangeChoiceType.equals(PluginReportDateRangeChoices.END_ONLY)){
				startDateChooser.setVisible(false);
			}
		}
	}


	public void init() {
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(okButton)){
			try {
				startDateChooser.commitEdit();
				endDateChooser.commitEdit();
			} catch (ParseException e1) {
				String[] options = new String[1];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(
						null, 
						TextFormatter.getTranslation(BuddiKeys.CANNOT_PARSE_DATE), 
						TextFormatter.getTranslation(BuddiKeys.REPORT_DATE_ERROR),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[0]
				);
				return;
			}
			
			System.out.println(startDateChooser.getDate() + ", " + endDateChooser.getDate());
			
			startDate = DateUtil.getStartOfDay(startDateChooser.getDate());
			endDate = DateUtil.getEndOfDay(endDateChooser.getDate());
			
			System.out.println(startDate + ", " + endDate);

			if (report.getDateRangeChoice().equals(PluginReportDateRangeChoices.INTERVAL)
					&& endDate.before(startDate)){
				String[] options = new String[1];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(
						null, 
						TextFormatter.getTranslation(BuddiKeys.START_DATE_AFTER_END_DATE), 
						TextFormatter.getTranslation(BuddiKeys.REPORT_DATE_ERROR),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[0]
				);
				return;
			}

			if (Const.DEVEL) Logger.getLogger(this.getClass().getName()).finest("Getting transactions between " + startDate + " and " + endDate);

			CustomDateDialog.this.setVisible(false);

			BuddiPluginHelper.openReport(reportFrame, report, startDate, endDate);
		}
		else if (e.getSource().equals(cancelButton)){
			this.closeWindow();
		}
	}

}
