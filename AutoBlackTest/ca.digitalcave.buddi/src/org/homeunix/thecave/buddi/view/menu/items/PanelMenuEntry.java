package org.homeunix.thecave.buddi.view.menu.items;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.homeunix.thecave.buddi.plugin.api.BuddiPanelPlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.swing.MossMenuItem;

/**
 *
 * @author mpeccorini
 */
public class PanelMenuEntry extends MossMenuItem {

	public static final long serialVersionUID = 0;
	private final BuddiPanelPlugin plugin;

	public PanelMenuEntry(MainFrame parentFrame, BuddiPanelPlugin plugin) {
		super(parentFrame, TextFormatter.getTranslation(plugin.getTabLabelKey()));
		this.plugin = plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			BuddiPanelPlugin newPlugin = plugin.getClass().newInstance();
			newPlugin.setParentFrame((MainFrame)getFrame());
			((MainFrame)getFrame()).addPanel(TextFormatter.getTranslation(newPlugin.getTabLabelKey()), newPlugin);
		} catch (InstantiationException ex) {
			Logger.getLogger(PanelMenuEntry.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(PanelMenuEntry.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
