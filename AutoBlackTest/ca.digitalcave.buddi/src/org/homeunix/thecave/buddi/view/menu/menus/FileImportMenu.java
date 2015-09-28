/*
 * Created on Aug 7, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.menu.menus;

import java.util.List;

import org.homeunix.thecave.buddi.i18n.keys.MenuKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiImportPlugin;
import org.homeunix.thecave.buddi.view.menu.items.PluginMenuEntry;

import ca.digitalcave.moss.swing.MossDocumentFrame;
import ca.digitalcave.moss.swing.MossFrame;
import ca.digitalcave.moss.swing.MossMenu;

public class FileImportMenu extends MossMenu {
	public static final long serialVersionUID = 0;
	
	public FileImportMenu(MossFrame frame) {
		super(frame, PrefsModel.getInstance().getTranslator().get(MenuKeys.MENU_FILE_IMPORT));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateMenus() {
		this.removeAll();
		
		for (BuddiImportPlugin plugin : (List<BuddiImportPlugin>) BuddiPluginFactory.getPlugins(BuddiImportPlugin.class)) {
			if (getFrame() instanceof MossDocumentFrame)
				this.add(new PluginMenuEntry((MossDocumentFrame) getFrame(), plugin));
		}
		
		this.setEnabled(getFrame() instanceof MossDocumentFrame && this.getComponentCount() > 0);
		
		super.updateMenus();
	}
}
