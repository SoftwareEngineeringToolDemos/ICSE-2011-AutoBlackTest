package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;

import org.homeunix.thecave.buddi.plugin.api.BuddiPanelPlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossCheckboxMenuItem;

/**
 *
 * @author mpeccorini
 */
public class SingletonPanelMenuEntry extends MossCheckboxMenuItem {

	public static final long serialVersionUID = 0;
	private final BuddiPanelPlugin plugin;

	public SingletonPanelMenuEntry(MainFrame parentFrame, BuddiPanelPlugin plugin) {
		super(parentFrame, TextFormatter.getTranslation(plugin.getTabLabelKey()));
		this.plugin = plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (isSelected()){
			plugin.setParentFrame((MainFrame)getFrame());
			plugin.updateContent();
			((MainFrame)getFrame()).addPanel(TextFormatter.getTranslation(plugin.getTabLabelKey()), plugin);
		} else {
			plugin.close();
		}
	}

	public void panelPluginClosed(BuddiPanelPlugin plugin) {
		setSelected(false);
	}
}