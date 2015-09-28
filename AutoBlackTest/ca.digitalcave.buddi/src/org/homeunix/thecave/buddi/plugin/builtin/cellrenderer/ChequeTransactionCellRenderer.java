/*
 * Created on Nov 5, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.cellrenderer;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;

public class ChequeTransactionCellRenderer extends DefaultTransactionCellRenderer {
	public static final long serialVersionUID = 0;
	
	public ChequeTransactionCellRenderer() {
		cheques = true;
	}
	
	@Override
	public String getName() {
		return TextFormatter.getTranslation(BuddiKeys.DEFAULT_TRANSLATION_CELL_RENDERER_WITH_CHEQUES);
	}
}
