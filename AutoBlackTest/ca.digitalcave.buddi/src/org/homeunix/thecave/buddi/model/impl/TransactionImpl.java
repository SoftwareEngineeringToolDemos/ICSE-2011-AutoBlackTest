/*
 * Created on Jul 29, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ModelObject;
import org.homeunix.thecave.buddi.model.Source;
import org.homeunix.thecave.buddi.model.Split;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.plugin.api.exception.InvalidValueException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

import ca.digitalcave.moss.common.DateUtil;

/**
 * Default implementation of a Transaction.  You should not create this object directly; 
 * instead, please use the ModelFactory to create it, as this will ensure that all
 * required fields are correctly set.
 * @author wyatt
 *
 */
public class TransactionImpl extends ModelObjectImpl implements Transaction {
	protected Day date;
//	protected String dateString;
	protected String description;
	protected String number;
	protected long amount;
	protected Source from;
	protected Source to;
	protected boolean deleted;
	protected boolean clearedFrom;
	protected boolean clearedTo;
	protected boolean reconciledFrom;
	protected boolean reconciledTo; 
	protected String memo;

	protected boolean scheduled;

	protected final Map<String, Long> balances = new HashMap<String, Long>();

	protected List<TransactionSplit> fromSplits;// = new ArrayList<TransactionSplit>();
	protected List<TransactionSplit> toSplits;// = new ArrayList<TransactionSplit>();


	@Override
	public int compareTo(ModelObject arg0) {
		if (arg0 instanceof Transaction){
			Transaction t = (Transaction) arg0;

			//For regular transactions, first we sort by date 
			if (!DateUtil.isSameDay(this.getDate(), t.getDate()))
				return this.getDate().compareTo(t.getDate());

			//Next we sort by debit / credit.  This is a nebulous beast, because of negative 
			// amounts, credit accounts, splits, etc.
			// We check the transaction source, and use some logic to determine if this means it
			// is inflow or outflow. 
			if (this.isInflow() != t.isInflow()){
				if (this.isInflow())
					return -1;
				return 1;
			}
			
			//Next we sort on the number field
			String thisNumber = this.getNumber() == null ? "" : this.getNumber();
			String otherNumber = t.getNumber() == null ? "" : t.getNumber();
			if (!thisNumber.equals(otherNumber))
				return thisNumber.compareTo(otherNumber);

			//If everything else is the same, we sort on description.
			if (this.getDescription() != null && t.getDescription() != null && !this.getDescription().equals(t.getDescription())){
				return this.getDescription().compareTo(t.getDescription());
			}
		}
		return super.compareTo(arg0);
	}
	public long getAmount() {
		return amount;
	}
	public long getBalance(String sourceUid) {
		if (balances.containsKey(sourceUid))
			return balances.get(sourceUid);
		return 0;
	}
	public Date getDate(){
		return date;
	}
	public String getDescription() {
		return description;
	}
	public Source getFrom() {
		return from;
	}
	public String getMemo() {
		return memo;
	}
	public String getNumber() {
		return number;
	}
	public Source getTo() {
		return to;
	}
	public boolean isClearedFrom() {
		return clearedFrom;
	}
	public boolean isClearedTo() {
		return clearedTo;
	}
	public boolean isInflow(){
		if (getFrom() instanceof BudgetCategory){
			return this.getAmount() >= 0;
		}
		if (getTo() instanceof BudgetCategory){
			return this.getAmount() < 0;
		}

		//If neither sources are BudgetCategory, this is not an inflow.
		return false;
	}
	public boolean isReconciledFrom() {
		return reconciledFrom;
	}
	public boolean isReconciledTo() {
		return reconciledTo;
	}
	public boolean isScheduled() {
		return scheduled;
	}
	public void setAmount(long amount) {
		if (this.amount != amount)
			setChanged();
		this.amount = amount;
	}
	public void setBalance(String sourceUid, long balance) {
		this.balances.put(sourceUid, balance);
	}
	public void setClearedFrom(boolean cleared) {
		if (this.clearedFrom != cleared)
			setChanged();
		this.clearedFrom = cleared;
		//If one of the To / From source is either
		// a budget category or a Prepaid account,
		// we set both of the flags to the same value.
		if (this.getTo() != null
				&& this.getFrom() != null
				&& !(this.getFrom() instanceof Split)
				&& !(this.getTo() instanceof Split)
				&& (this.getFrom() instanceof Account || this.getFrom() instanceof BudgetCategory) 
				&& (this.getTo() instanceof Account || this.getTo() instanceof BudgetCategory)
				&& (this.getTo() instanceof BudgetCategory
						|| this.getFrom() instanceof BudgetCategory
						|| ((Account) this.getTo()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT))
						|| ((Account) this.getFrom()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT)))){
			this.clearedTo = cleared;
		}
	}
	public void setClearedTo(boolean cleared) {
		if (this.clearedTo != cleared)
			setChanged();
		this.clearedTo = cleared;
		//If one of the To / From source is either
		// a budget category or a Prepaid account,
		// we set both of the flags to the same value.
		if (this.getTo() != null
				&& this.getFrom() != null
				&& !(this.getFrom() instanceof Split)
				&& !(this.getTo() instanceof Split)
				&& (this.getTo() instanceof BudgetCategory
						|| this.getFrom() instanceof BudgetCategory
						|| ((Account) this.getTo()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT))
						|| ((Account) this.getFrom()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT)))){
			this.clearedFrom = cleared;
		}
	}
	public void setDate(Date date) {
		if (this.date != null && this.date.equals(date))
			setChanged();
		this.date = new Day(date);
	}
	public void setDescription(String description) {
		if (this.description != null && !this.description.equals(description))
			setChanged();
		this.description = description;
	}
	public void setFrom(Source from) {
		setChanged();
		this.from = from;
		if (this.fromSplits == null)
			this.fromSplits = new ArrayList<TransactionSplit>();
		this.fromSplits.clear();
	}
	public void setMemo(String memo) {
		if (this.memo != null && !this.memo.equals(memo))
			setChanged();
		this.memo = memo;
	}
	public void setNumber(String number) {
		if (this.number != null && !this.number.equals(number))
			setChanged();
		this.number = number;
	}
	public void setReconciledFrom(boolean reconciled) {
		if (this.reconciledFrom != reconciled)
			setChanged();
		this.reconciledFrom = reconciled;
		//If one of the To / From source is either
		// a budget category or a Prepaid account,
		// we set both of the flags to the same value.
		if (this.getTo() != null
				&& this.getFrom() != null
				&& (this.getFrom() instanceof Account || this.getFrom() instanceof BudgetCategory)
				&& (this.getTo() instanceof Account || this.getTo() instanceof BudgetCategory)				
				&& (this.getTo() instanceof BudgetCategory
						|| this.getFrom() instanceof BudgetCategory
						|| ((Account) this.getTo()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT))
						|| ((Account) this.getFrom()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT)))){
			this.reconciledTo = reconciled;
		}
	}
	public void setReconciledTo(boolean reconciled) {
		if (this.reconciledTo != reconciled)
			setChanged();
		this.reconciledTo = reconciled;
		//If one of the To / From source is either
		// a budget category or a Prepaid account,
		// we set both of the flags to the same value.
		if (this.getTo() != null
				&& this.getFrom() != null
				&& (this.getFrom() instanceof Account || this.getFrom() instanceof BudgetCategory)
				&& (this.getTo() instanceof Account || this.getTo() instanceof BudgetCategory)
				&& (this.getTo() instanceof BudgetCategory
						|| this.getFrom() instanceof BudgetCategory
						|| ((Account) this.getTo()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT))
						|| ((Account) this.getFrom()).getAccountType().getName().equals(TextFormatter.getTranslation(BuddiKeys.PREPAID_ACCOUNT)))){
			this.reconciledFrom = reconciled;
		}
	}
	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}
	public void setTo(Source to) {
		setChanged();
		this.to = to;
		if (this.toSplits == null)
			this.toSplits = new ArrayList<TransactionSplit>();
		this.toSplits.clear();
	}
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		setChanged();
		this.deleted = deleted;
	}

	public List<TransactionSplit> getToSplits() {
		return toSplits;
	}

	public void setToSplits(List<TransactionSplit> splits) throws InvalidValueException {
		setChanged();
		if (splits == this.toSplits) return;
		if (this.toSplits == null)
			this.toSplits = new ArrayList<TransactionSplit>();
		
		this.toSplits.clear();
		
		if (splits != null) {
			this.to = new SplitImpl();
			this.toSplits.addAll(splits);			
		}
	}
	
	public List<TransactionSplit> getFromSplits() {
		return fromSplits;
	}

	public void setFromSplits(List<TransactionSplit> splits) throws InvalidValueException {
		setChanged();
		if (splits == this.toSplits) return;
		if (this.fromSplits == null)
			this.fromSplits = new ArrayList<TransactionSplit>();
		this.fromSplits.clear();
		
		if (splits != null){
			this.from = new SplitImpl();
			this.fromSplits.addAll(splits);			
		}
	}

	Transaction clone(Map<ModelObject, ModelObject> originalToCloneMap) throws CloneNotSupportedException {

		if (originalToCloneMap.get(this) != null)
			return (Transaction) originalToCloneMap.get(this);

		TransactionImpl t = new TransactionImpl();

		t.document = (Document) originalToCloneMap.get(document);
		t.amount = amount;
		t.clearedFrom = clearedFrom;
		t.clearedTo = clearedTo;
		t.date = new Day(date);
		t.deleted = deleted;
		t.description = description;
		t.from = (Source) ((SourceImpl) from).clone(originalToCloneMap);
		t.memo = memo;
		t.number = number;
		t.reconciledFrom = reconciledFrom;
		t.reconciledTo = reconciledTo;
		t.scheduled = scheduled;
		t.to = (Source) ((SourceImpl) to).clone(originalToCloneMap);
		if (this.toSplits == null)
			this.toSplits = new ArrayList<TransactionSplit>();
		if (this.fromSplits == null)
			this.fromSplits = new ArrayList<TransactionSplit>();
		if (t.toSplits == null)
			t.toSplits = new ArrayList<TransactionSplit>();
		if (t.fromSplits == null)
			t.fromSplits = new ArrayList<TransactionSplit>();		
		t.fromSplits.clear();
		for (TransactionSplit split : fromSplits) {
			t.fromSplits.add((TransactionSplit) ((TransactionSplitImpl) split).clone(originalToCloneMap));
		}
		t.toSplits.clear();
		for (TransactionSplit split : toSplits) {
			t.toSplits.add((TransactionSplit) ((TransactionSplitImpl) split).clone(originalToCloneMap));
		}
		t.modifiedTime = new Time(modifiedTime);
		
		originalToCloneMap.put(this, t);

		return t;
	}
	
	@Override
	public String toString() {
		return getDescription() + " " + getAmount() + " (" + getFrom() + " -> " + getTo() + ")";
	}
}
