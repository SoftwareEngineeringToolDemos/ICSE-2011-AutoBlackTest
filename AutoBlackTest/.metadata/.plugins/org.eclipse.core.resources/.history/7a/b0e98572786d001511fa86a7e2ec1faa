/*
 * Created on May 6, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view.dialogs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequency;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.dialogs.schedule.BiWeeklyCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.DailyCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.EveryXDaysCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.MonthlyByDateCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.MultipleMonthsEachYearCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.MultipleWeeksEachMonthCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.OneDayEveryMonthCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.ScheduleCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.WeekdayCard;
import org.homeunix.thecave.buddi.view.dialogs.schedule.WeeklyCard;
import org.homeunix.thecave.buddi.view.panels.TransactionEditorPanel;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;
import org.jdesktop.swingx.JXDatePicker;

import ca.digitalcave.moss.common.DateUtil;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossDialog;
import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossHintTextArea;
import ca.digitalcave.moss.swing.MossHintTextField;
import ca.digitalcave.moss.swing.MossScrollingComboBox;

public class ScheduledTransactionEditorDialog extends MossDialog implements ActionListener {
	public static final long serialVersionUID = 0;

	private final JButton okButton;
	private final JButton cancelButton;

	private final ScheduledTransaction schedule;

	private final MossHintTextField scheduleName;
	private final MossHintTextArea message;
	private final MossScrollingComboBox frequencyPulldown;
	private final JXDatePicker startDateChooser;
	private final JXDatePicker endDateChooser;
	private final JCheckBox endDateChooserEnabled;
	private final TransactionEditorPanel transactionEditor;

	//Each card
	private final MonthlyByDateCard monthly;
	private final OneDayEveryMonthCard oneDayMonthly;
	private final WeeklyCard weekly;
	private final BiWeeklyCard biWeekly;
	private final WeekdayCard everyWeekday;
	private final DailyCard everyDay;
	private final EveryXDaysCard everyXDays;
	private final MultipleWeeksEachMonthCard multipleWeeksMonthly;
	private final MultipleMonthsEachYearCard multipleMonthsYearly;

	private final Map<String, ScheduleCard> cardMap = new HashMap<String, ScheduleCard>();

	private final CardLayout cardLayout;
	private final JPanel cardHolder;

	private final Document model;

	public ScheduledTransactionEditorDialog(MossDocumentFrame parentFrame, ScheduledTransaction scheduleToEdit){
		super(parentFrame);

		this.model = (Document) parentFrame.getDocument();

		this.schedule = scheduleToEdit;

		okButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_OK));
		cancelButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL));

		startDateChooser = new JXDatePicker();
		endDateChooser = new JXDatePicker();
		endDateChooserEnabled = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.ENDING_ON));
		transactionEditor = new TransactionEditorPanel(null, (Document) parentFrame.getDocument(), null, true);

		scheduleName = new MossHintTextField(TextFormatter.getTranslation(BuddiKeys.HINT_SCHEDULED_TRANSACTION_NAME));

		message = new MossHintTextArea(TextFormatter.getTranslation(BuddiKeys.HINT_MESSAGE));

		//This is where we give the Frequency dropdown options
		frequencyPulldown = new MossScrollingComboBox(ScheduleFrequency.values());


		monthly = new MonthlyByDateCard();
		oneDayMonthly = new OneDayEveryMonthCard();
		weekly = new WeeklyCard();
		biWeekly = new BiWeeklyCard();
		everyWeekday = new WeekdayCard();
		everyDay = new DailyCard();
		everyXDays = new EveryXDaysCard();
		multipleWeeksMonthly = new MultipleWeeksEachMonthCard();
		multipleMonthsYearly = new MultipleMonthsEachYearCard();

		cardLayout = new CardLayout();
		cardHolder = new JPanel(cardLayout);
	}

	protected String getType(){
		return TextFormatter.getTranslation(BuddiKeys.ACCOUNT);
	}

	public void init() {
		if (PrefsModel.getInstance().isShowTooltips())
			message.setToolTipText(TextFormatter.getTranslation(BuddiKeys.TOOLTIP_SCHEDULED_MESSAGE));

		okButton.setPreferredSize(InternalFormatter.getButtonSize(okButton));
		cancelButton.setPreferredSize(InternalFormatter.getButtonSize(cancelButton));

		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		JScrollPane messageScroller = new JScrollPane(message);
		messageScroller.setPreferredSize(new Dimension(200, 100));

		endDateChooserEnabled.setSelected(true);
		
		startDateChooser.setEditor(new JFormattedTextField(new SimpleDateFormat(PrefsModel.getInstance().getDateFormat())));
		startDateChooser.setDate(new Date());

		endDateChooser.setEditor(new JFormattedTextField(new SimpleDateFormat(PrefsModel.getInstance().getDateFormat())));
		endDateChooser.setDate(DateUtil.addYears(new Date(), 1));

		Dimension textSize = new Dimension(Math.max(250, scheduleName.getPreferredSize().width), scheduleName.getPreferredSize().height);
		startDateChooser.setPreferredSize(textSize);
		endDateChooser.setPreferredSize(textSize);
		scheduleName.setPreferredSize(textSize);

		transactionEditor.setPreferredSize(new Dimension(10, transactionEditor.getPreferredSize().height));


		//The top part of the schedule information
		JLabel intro = new JLabel(TextFormatter.getTranslation(BuddiKeys.REPEAT_THIS_ACTION));
		JPanel introHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		introHolder.add(intro);
		introHolder.add(frequencyPulldown);

		//The middle part of the schedule information
		JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		startDatePanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.STARTING_ON)));
		startDatePanel.add(startDateChooser);
		
		//The middle part of the schedule information
		JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		endDatePanel.add(endDateChooserEnabled);
		endDatePanel.add(endDateChooser);

		JPanel middlePanel = new JPanel(new GridLayout(0, 1));
		middlePanel.add(startDatePanel);
		middlePanel.add(endDatePanel);
		
		JPanel cardHolderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		cardHolderPanel.add(cardHolder);
		
		//Put the schedule panel together properly...
		//The schedulePanel is where we keep all the options for scheduling
		JPanel scheduleDetailsPanel = new JPanel(new BorderLayout());
		scheduleDetailsPanel.add(introHolder, BorderLayout.NORTH);
		scheduleDetailsPanel.add(middlePanel, BorderLayout.CENTER);
		scheduleDetailsPanel.add(cardHolderPanel, BorderLayout.SOUTH);

		JPanel scheduleNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		scheduleNamePanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.SCHEDULED_TRANSACTION_NAME)));
		scheduleNamePanel.add(scheduleName);

		//The namePanel is where we keep the Schedule Name.
		JPanel entireSchedulePanel = new JPanel(new BorderLayout());
		entireSchedulePanel.setBorder(BorderFactory.createTitledBorder((String) null));
		entireSchedulePanel.add(scheduleNamePanel, BorderLayout.NORTH);
		entireSchedulePanel.add(scheduleDetailsPanel, BorderLayout.CENTER);

		//We new define each 'card' separately, with the correct
		// options for each frequency.  This is then added to the
		// card holder for quick random access when the frequency 
		// pulldown changes.
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_MONTHLY_BY_DATE.toString(), monthly);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_MONTHLY_BY_DAY_OF_WEEK.toString(), oneDayMonthly);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_WEEKLY.toString(), weekly);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_BIWEEKLY.toString(), biWeekly);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_WEEKDAY.toString(), everyWeekday);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_DAY.toString(), everyDay);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_X_DAYS.toString(), everyXDays);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_MULTIPLE_WEEKS_EVERY_MONTH.toString(), multipleWeeksMonthly);
		cardMap.put(ScheduleFrequency.SCHEDULE_FREQUENCY_MULTIPLE_MONTHS_EVERY_YEAR.toString(), multipleMonthsYearly);

		cardHolder.add(monthly, ScheduleFrequency.SCHEDULE_FREQUENCY_MONTHLY_BY_DATE.toString());
		cardHolder.add(oneDayMonthly, ScheduleFrequency.SCHEDULE_FREQUENCY_MONTHLY_BY_DAY_OF_WEEK.toString());
		cardHolder.add(weekly, ScheduleFrequency.SCHEDULE_FREQUENCY_WEEKLY.toString());
		cardHolder.add(biWeekly, ScheduleFrequency.SCHEDULE_FREQUENCY_BIWEEKLY.toString());
		cardHolder.add(new JPanel(), ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_WEEKDAY.toString());
		cardHolder.add(new JPanel(), ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_DAY.toString());
		cardHolder.add(everyXDays, ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_X_DAYS.toString());
		cardHolder.add(multipleWeeksMonthly, ScheduleFrequency.SCHEDULE_FREQUENCY_MULTIPLE_WEEKS_EVERY_MONTH.toString());
		cardHolder.add(multipleMonthsYearly, ScheduleFrequency.SCHEDULE_FREQUENCY_MULTIPLE_MONTHS_EVERY_YEAR.toString());

		//Done all the fancy stuff... now just put all the panels together
		JPanel scheduleAndMessagePanel = new JPanel(new BorderLayout());
		scheduleAndMessagePanel.add(entireSchedulePanel, BorderLayout.CENTER);
		scheduleAndMessagePanel.add(messageScroller, BorderLayout.EAST);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(scheduleAndMessagePanel, BorderLayout.CENTER);
		mainPanel.add(transactionEditor, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);

		this.setTitle(TextFormatter.getTranslation(BuddiKeys.SCHEDULED_TRANSACTION));
		this.getRootPane().setDefaultButton(okButton);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		if (OperatingSystemUtil.isMac()){
			messageScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			messageScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}

		//If we are viewing existing transactions, we cannot 
		// modify the schedule at all.
		//TODO Make a more intelligent enabled check thingy
		startDateChooser.setEnabled(schedule == null);
		endDateChooser.setEnabled(schedule == null);
		endDateChooserEnabled.setEnabled(schedule == null);
		frequencyPulldown.setEnabled(schedule == null);
		monthly.setEnabled(schedule == null);
		oneDayMonthly.setEnabled(schedule == null);
		weekly.setEnabled(schedule == null);
		biWeekly.setEnabled(schedule == null);
		everyWeekday.setEnabled(schedule == null);
		everyDay.setEnabled(schedule == null);
		everyXDays.setEnabled(schedule == null);
		multipleWeeksMonthly.setEnabled(schedule == null);
		multipleMonthsYearly.setEnabled(schedule == null);

		updateSchedulePulldown();

		if (schedule == null){
			endDateChooserEnabled.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					endDateChooser.setEnabled(endDateChooserEnabled.isSelected() && schedule == null);
				}
			});
		}
		
		frequencyPulldown.setRenderer(new TranslatorListCellRenderer());
		frequencyPulldown.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				ScheduledTransactionEditorDialog.this.updateSchedulePulldown();
			}
		});

		loadSchedule();
	}

	private void updateSchedulePulldown(){
		if (frequencyPulldown.getSelectedItem() != null) {
			cardLayout.show(cardHolder, frequencyPulldown.getSelectedItem().toString());

			Logger.getLogger(this.getClass().getName()).info("Showing card " + frequencyPulldown.getSelectedItem().toString());
		}
	}

//	private boolean ensureInfoCorrect(){
//
//		//We must have filled in at least the name and the date.
//		if ((scheduleName.getText().length() == 0)
//				|| (startDateChooser.getDate() == null)){
//			return false;
//		}
//
//		//If we're just fillinf in the transaction, we need at least
//		// amount, description, to, and from.
//		if ((transactionEditor.getAmount() != 0)
//				&& (transactionEditor.getDescription() != null && transactionEditor.getDescription().length() > 0)
//				&& (transactionEditor.getTo() != null)
//				&& (transactionEditor.getFrom() != null)){
//			return true;
//		}
//
//		//If the message is filled in, we can let the action succeed
//		// without the transaction being filled out.  However, if any
//		// part of the transaction is filled in, it all must be.
//		if ((message.getText().length() > 0)
//				&& (transactionEditor.getAmount() == 0)
//				&& (transactionEditor.getDescription() == null 
//						|| transactionEditor.getDescription().length() == 0)
//				&& (transactionEditor.getTo() == null)
//				&& (transactionEditor.getFrom() == null)){
//			return true;
//		}
//
//		return false;
//	}

	/**
	 * Saves the changes to the scheduled transaction.  Returns true if the 
	 * save succeeded (and we can close / switch selection / etc), or false
	 * if if did not succeed, and you need to force the user to continue.
	 * @return
	 */
	private boolean saveScheduledTransaction(){
		try {
			ScheduledTransaction s;
			final boolean needToAdd;

			model.startBatchChange();
			
			//If the currently edited schedule is null, we need to create a new one, and
			// flag to add it to the model.
			if (schedule == null) {
				Transaction t = transactionEditor.getTransactionNew();
				s = ModelFactory.createScheduledTransaction(
						this.scheduleName.getText(),
						this.message.getText(),
						
						startDateChooser.getDate(),
						(endDateChooserEnabled.isSelected() ? endDateChooser.getDate() : null),
						frequencyPulldown.getSelectedItem().toString(),
						getSelectedCard().getScheduleDay(),
						getSelectedCard().getScheduleWeek(),
						getSelectedCard().getScheduleMonth(),

						t.getDescription(),
						t.getAmount(),
						t.getFrom(),
						t.getTo());
				needToAdd = true;
			}
			else {
				Transaction t = transactionEditor.getTransactionUpdated();
				s = schedule;
				
				s.setScheduleName(this.scheduleName.getText());
				s.setMessage(this.message.getText());
				s.setAmount(t.getAmount());
				s.setDescription(t.getDescription());
				s.setNumber(t.getNumber());
				s.setTo(t.getTo());
				s.setFrom(t.getFrom());
				s.setMemo(t.getMemo());
				needToAdd = false;
			}

			if (transactionEditor.isTransactionValid()){
				String[] options = new String[2];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);
				options[1] = TextFormatter.getTranslation(ButtonKeys.BUTTON_CANCEL);

				//We check for this in the validation, but if we do it here we can have a nice error message as well. 
				for (ScheduledTransaction existingSched : model.getScheduledTransactions()) {
					if (existingSched.getScheduleName().equalsIgnoreCase(s.getScheduleName())){
						String[] errorOptions = new String[1];
						errorOptions[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

						JOptionPane.showOptionDialog(ScheduledTransactionEditorDialog.this, 
								TextFormatter.getTranslation(BuddiKeys.SCHEDULED_DUPLICATE_NAME),
								TextFormatter.getTranslation(BuddiKeys.SCHEDULED_DUPLICATE_NAME_TITLE),
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE,
								null,
								errorOptions,
								errorOptions[0]);
						
						model.finishBatchChange();
						return false;
					}
				}				

				
				if (!ScheduledTransactionEditorDialog.this.startDateChooser.getDate().before(new Date())
						|| schedule != null		//If the schedule has already been defined, we won't bother people again 
						|| JOptionPane.showOptionDialog(ScheduledTransactionEditorDialog.this, 
								TextFormatter.getTranslation(BuddiKeys.START_DATE_IN_THE_PAST), 
								TextFormatter.getTranslation(BuddiKeys.START_DATE_IN_THE_PAST_TITLE), 
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.PLAIN_MESSAGE,
								null,
								options,
								options[0]) == JOptionPane.OK_OPTION){

					//TODO We should not have to save this, as it cannot be modified.
//					if (needToAdd){
//						s.setStartDate(startDateChooser.getDate());
//						if (endDateChooserEnabled.isSelected())
//							s.setEndDate(endDateChooser.getDate());
//						s.setFrequencyType(frequencyPulldown.getSelectedItem().toString());
//						s.setScheduleDay(getSelectedCard().getScheduleDay());
//						s.setScheduleWeek(getSelectedCard().getScheduleWeek());
//						s.setScheduleMonth(getSelectedCard().getScheduleMonth());
//					}

					if (needToAdd)
						model.addScheduledTransaction(s);

					model.finishBatchChange();
					return true;
				}
				else
					if (Const.DEVEL) Logger.getLogger(this.getClass().getName()).finest("Cancelled from either start date in the past, or info not correct");
			}
			else {
				String[] options = new String[1];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(ScheduledTransactionEditorDialog.this, 
						TextFormatter.getTranslation(BuddiKeys.SCHEDULED_NOT_ENOUGH_INFO),
						TextFormatter.getTranslation(BuddiKeys.SCHEDULED_NOT_ENOUGH_INFO_TITLE),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE,
						null,
						options,
						options[0]);
			}
		}
		catch (ModelException me){
			String[] errorOptions = new String[1];
			errorOptions[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

			JOptionPane.showOptionDialog(ScheduledTransactionEditorDialog.this, 
					TextFormatter.getTranslation(BuddiKeys.SCHEDULED_UNHANDLED_EXCEPTION) + "\n\n" + me.getLocalizedMessage(),
					TextFormatter.getTranslation(BuddiKeys.SCHEDULED_UNHANDLED_EXCEPTION_TITLE),
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE,
					null,
					errorOptions,
					errorOptions[0]);
			
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error when saving scheduled transaction", me);
		}

		model.finishBatchChange();
		return false;
	}

	private ScheduleCard getSelectedCard(){
		if (frequencyPulldown.getSelectedItem() == null)
			return null;
		return cardMap.get(frequencyPulldown.getSelectedItem().toString());
	}

	private void loadSchedule(){
		if (schedule != null){
			transactionEditor.updateContent();
			updateSchedulePulldown();

			//Load the changeable fields, including Transaction
			scheduleName.setText(schedule.getScheduleName());
			message.setText(schedule.getMessage());
			transactionEditor.setTransaction(schedule, true, okButton);

			//Load the schedule pulldowns, based on which type of 
			// schedule we're following.
			startDateChooser.setDate(schedule.getStartDate());
			if (schedule.getEndDate() != null){
				endDateChooser.setDate(schedule.getEndDate());
				endDateChooserEnabled.setSelected(true);
			}
			else {
				endDateChooserEnabled.setSelected(false);
				endDateChooser.setDate(null);
			}
			if (schedule.getFrequencyType() != null)
				frequencyPulldown.setSelectedItem(ScheduleFrequency.valueOf(schedule.getFrequencyType()));

			updateSchedulePulldown();

			//Load the schedule in the selected card.
			if (getSelectedCard() != null)
				getSelectedCard().loadSchedule(schedule);
		}
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(okButton)){
			if (saveScheduledTransaction()){
				model.updateScheduledTransactions();
				model.updateAllBalances();
				this.closeWindow();
			}
		}
		else if (e.getSource().equals(cancelButton)){
			this.closeWindow();
		}
	}

//	public void loadSchedule(ScheduledTransaction s){
//	this.schedule = s;

//	if (s != null){
//	transactionEditor.updateContent();

//	//Load the changeable fields, including Transaction
//	scheduleName.setValue(s.getScheduleName());
//	message.setValue(s.getMessage());
//	transactionEditor.setTransaction(s, true);

//	//Load the schedule pulldowns, based on which type of 
//	// schedule we're following.
//	startDateChooser.setDate(s.getStartDate());
//	frequencyPulldown.setSelectedItem(s.getFrequencyType());

//	updateSchedulePulldown();

//	//Load the schedule in the selected card.
//	if (getSelectedCard() != null)
//	getSelectedCard().loadSchedule(s);
//	}
//	}
}
