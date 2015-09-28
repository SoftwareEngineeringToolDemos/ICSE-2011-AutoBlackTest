/*
 * Created on Dec 6, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.imports;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.plugin.api.BuddiImportPlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginMessage;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.MutableBudgetCategory;
import org.homeunix.thecave.buddi.plugin.api.model.MutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.MutableModelFactory;
import org.homeunix.thecave.buddi.plugin.api.model.MutableTransaction;

import ca.digitalcave.moss.common.DateUtil;
import ca.digitalcave.moss.swing.MossDocumentFrame;

public class ImportTestData extends BuddiImportPlugin {

	@Override
	public void importData(MutableDocument model, MossDocumentFrame callingFrame, File file) throws PluginException, PluginMessage {
		try {
			for (int i = 0; i < 5; i++){
				MutableAccount a = MutableModelFactory.createMutableAccount("ACME " + i, 10000, model.getMutableAccountTypes().get((int) (Math.random() * model.getMutableAccountTypes().size())));
				model.addAccount(a);				
			}
			
			for (int i = 0; i < 15; i++){
				MutableBudgetCategory bc = MutableModelFactory.createMutableBudgetCategory("ACME" + i, model.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH), (Math.random() > 0.5));
				model.addBudgetCategory(bc);
			}
			
			List<MutableBudgetCategory> budgetCategories = model.getMutableBudgetCategories();
			List<MutableAccount> accounts = model.getMutableAccounts();
			
			int MAX = 100000;
			for (int i = 0; i < MAX; i++){
				if (i % 100 == 0)
					Logger.getLogger(this.getClass().getName()).info("Creating transaction " + i + " of " + MAX);
				MutableAccount a = accounts.get((int) (Math.random() * accounts.size()));
				MutableBudgetCategory bc = budgetCategories.get((int) (Math.random() * budgetCategories.size()));
				MutableTransaction t = MutableModelFactory.createMutableTransaction(DateUtil.getDate(2006, (int) (Math.random() * 12), (int) (Math.random() * 28)), "Test Transaction " + i, (long) (Math.random() * 1000000), a, bc);
				model.addTransaction(t);
			}
		}
		catch (ModelException me){
			throw new PluginException(me);
		}
	}
	
	@Override
	public boolean isPromptForFile() {
		return false;
	}
	
	@Override
	public boolean isCreateNewFile() {
		return true;
	}
	
	public String getName() {
		return "Create Test Data";
	}
}
