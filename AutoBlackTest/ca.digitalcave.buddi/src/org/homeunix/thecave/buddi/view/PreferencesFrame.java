/*
 * Created on May 6, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.BuddiPluginFactory;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.menu.bars.BuddiMenuBar;

import ca.digitalcave.moss.common.ClassLoaderFunctions;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.ApplicationModel;
import ca.digitalcave.moss.swing.MossFrame;

public class PreferencesFrame extends MossFrame implements ActionListener {
	public static final long serialVersionUID = 0;
	
	private final JTabbedPane tabs;
	private final List<BuddiPreferencePlugin> preferencePanels;
	
	private final JButton okButton;
	private final JButton cancelButton;
	
	@SuppressWarnings("unchecked")
	public PreferencesFrame() {
		super("Preferences");
		this.setIconImage(ClassLoaderFunctions.getImageFromClasspath("img/BuddiFrameIcon.gif"));
		tabs = new JTabbedPane();
		
		preferencePanels = (List<BuddiPreferencePlugin>) BuddiPluginFactory.getPlugins(BuddiPreferencePlugin.class);		
		okButton = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_OK));
		cancelButton = new JButton(PrefsModel.getInstance().getTranslator().get(ButtonKeys.BUTTON_CANCEL));
	}
	
	@Override
	public void init() {
		super.init();

		//Load each plugin into the Preferences panel
		for (BuddiPreferencePlugin preferencePlugin : preferencePanels) {
			JPanel panel;
			if (preferencePlugin.isUseWrapper())
				panel = getWrapperPanel(preferencePlugin.getPreferencesPanel());
			else
				panel = preferencePlugin.getPreferencesPanel();
			tabs.addTab(PrefsModel.getInstance().getTranslator().get(preferencePlugin.getName()), panel);
			
		}
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		if (OperatingSystemUtil.isMac()){
			buttonPanel.add(cancelButton);
			buttonPanel.add(okButton);
		}
		else {
			buttonPanel.add(okButton);			
			buttonPanel.add(cancelButton);
		}

		okButton.setPreferredSize(InternalFormatter.getButtonSize(okButton));
		cancelButton.setPreferredSize(InternalFormatter.getButtonSize(cancelButton));
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		for (BuddiPreferencePlugin panel : preferencePanels) {
			try {
				panel.load();
			}
			catch (PluginException pe){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Plugin Exception", pe);
			}
		}
		
		this.setJMenuBar(new BuddiMenuBar(this));
		this.setTitle(TextFormatter.getTranslation(BuddiKeys.PREFERENCES));
		this.getRootPane().setDefaultButton(okButton);
		this.setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	
	@Override
	public void closeWindowWithoutPrompting() {
		PrefsModel.getInstance().putWindowLocation(BuddiKeys.PREFERENCES.toString(), this.getLocation());
		PrefsModel.getInstance().save();

		MainFrame.updateAllContent();
		
		super.closeWindowWithoutPrompting();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(okButton)){
			boolean restart = false;
			for (BuddiPreferencePlugin panel : preferencePanels) {
				try {
					if (panel.save())
						restart = true;
				}
				catch (PluginException pe){
					Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Plugin Exception", pe);
				}
			}
			
			PrefsModel.getInstance().save();

			if (restart){
				String[] options = new String[1];
				options[0] = TextFormatter.getTranslation(ButtonKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(
						null, 
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_PREFERENCES_CHANGED_RESTART_NEEDED),
						TextFormatter.getTranslation(BuddiKeys.MESSAGE_PREFERENCES_CHANGED_RESTART_NEEDED_TITLE),
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						options,
						options[0]
				);
			}
			
			for (MossFrame frame : ApplicationModel.getInstance().getOpenFrames()) {
				if (frame instanceof MainFrame){
					MainFrame main = (MainFrame) frame;
					main.fireStructureChanged();
					main.updateContent();
					main.updateMenus();
				}
			}
			
			this.closeWindow();
		}
		else if (e.getSource().equals(cancelButton)){
			this.closeWindow();
		}
	}
	
	private JPanel getWrapperPanel(JPanel panel){
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.add(panel, BorderLayout.NORTH);
		return wrapper;
	}
}
