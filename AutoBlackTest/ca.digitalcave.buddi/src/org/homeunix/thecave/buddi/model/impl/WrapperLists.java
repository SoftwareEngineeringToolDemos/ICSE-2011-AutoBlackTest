/*
 * Created on Aug 4, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.impl;

import java.util.List;

import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.AccountType;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.ModelObject;
import org.homeunix.thecave.buddi.model.ScheduledTransaction;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.TransactionSplit;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableModelObject;
import org.homeunix.thecave.buddi.plugin.api.model.impl.ImmutableAccountImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableAccountImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableAccountTypeImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableBudgetCategoryImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableScheduledTransactionImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableTransactionImpl;
import org.homeunix.thecave.buddi.plugin.api.model.impl.MutableTransactionSplitImpl;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;
import ca.digitalcave.moss.collections.WrapperList;

public class WrapperLists {

	private WrapperLists() {}

	private abstract static class BuddiWrapperList<T, W> extends WrapperList<T, W> {
		private final Document model;
		private final DocumentChangeListener listener;
		
		public BuddiWrapperList(Document model, List<W> wrappedList, boolean sorted) {
			super(wrappedList, sorted);
			this.model = model;
			
			listener = new DocumentChangeListener(){
				public void documentChange(DocumentChangeEvent event) {
					updateWrapperList();
				}
			};
			
			model.addDocumentChangeListener(listener);
		}

		public Document getDataModel(){
			return model;
		}
	}

//	public static class WrapperBudgetPeriodList extends BuddiWrapperList<BudgetPeriod, BudgetPeriodBean>{
//	public WrapperBudgetPeriodList(DataModel model, List<BudgetPeriodBean> wrappedList) {
//	super(model, wrappedList, true);
//	}

//	@Override
//	public BudgetPeriodBean getWrappedObject(BudgetPeriod object) {
//	return object.getBudgetPeriodBean();
//	}

//	@Override
//	public BudgetPeriod getWrapperObject(BudgetPeriodBean object) {
//	return new BudgetPeriod(getDataModel(), object);
//	}
//	}

	public static class ImmutableAccountList extends BuddiWrapperList<ImmutableAccount, Account> {
		public ImmutableAccountList(Document model, List<Account> wrappedList) {
			super(model, wrappedList, true);
		}

		@Override
		public Account getWrappedObject(ImmutableAccount object) {
			return (Account) object.getRaw();
		}

		@Override
		public ImmutableAccount getWrapperObject(Account object) {
			return new ImmutableAccountImpl(object);
		}
	}

	public static class ImmutableObjectWrapperList<T extends ImmutableModelObject, W extends ModelObject> extends BuddiWrapperList<T, W> {
		public ImmutableObjectWrapperList(Document model, List<W> wrappedList) {
			super(model, wrappedList, true);
		}

		@SuppressWarnings("unchecked")
		@Override
		public W getWrappedObject(T object) {
			return (W) object.getRaw();
		}

		@SuppressWarnings("unchecked")
		@Override
		public T getWrapperObject(W object) {
			if (object instanceof Account)
				return (T) new MutableAccountImpl((Account) object);
			if (object instanceof BudgetCategory)
				return (T) new MutableBudgetCategoryImpl((BudgetCategory) object);
			if (object instanceof Transaction)
				return (T) new MutableTransactionImpl((Transaction) object);
			if (object instanceof ScheduledTransaction)
				return (T) new MutableScheduledTransactionImpl((ScheduledTransaction) object);
			if (object instanceof AccountType)
				return (T) new MutableAccountTypeImpl((AccountType) object);
			if (object instanceof TransactionSplit)
				return (T) new MutableTransactionSplitImpl((TransactionSplit) object);

			//Catch all
			return null;
		}
	}
}
