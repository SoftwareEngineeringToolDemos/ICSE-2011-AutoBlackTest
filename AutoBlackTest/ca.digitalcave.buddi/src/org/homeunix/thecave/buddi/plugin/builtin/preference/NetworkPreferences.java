/*
 * Created on Aug 12, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.plugin.builtin.preference;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;

import ca.digitalcave.moss.swing.MossHintTextField;

public class NetworkPreferences extends BuddiPreferencePlugin {
	public static final long serialVersionUID = 0;
	
	private final JCheckBox useProxy;
	private final MossHintTextField proxy;
	private final MossHintTextField port;
	
	public NetworkPreferences() {
		useProxy = new JCheckBox(TextFormatter.getTranslation(BuddiKeys.USE_PROXY_SERVER));
		proxy = new MossHintTextField(TextFormatter.getTranslation(BuddiKeys.HINT_PROXY_SERVER_NAME));
		port = new MossHintTextField(TextFormatter.getTranslation(BuddiKeys.HINT_PORT));
		
		useProxy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				proxy.setEnabled(useProxy.isSelected());
				port.setEnabled(useProxy.isSelected());
			}
		});
	}
	
	public void load() {
		useProxy.setSelected(PrefsModel.getInstance().isShowProxySettings());
		proxy.setText(PrefsModel.getInstance().getProxyServer());
		port.setText(PrefsModel.getInstance().getPort() + "");
		
		proxy.setEnabled(useProxy.isSelected());
		port.setEnabled(useProxy.isSelected());
	}
	
	public boolean save() {
		PrefsModel.getInstance().setShowProxySettings(useProxy.isSelected());
		if (useProxy.isSelected()){
			PrefsModel.getInstance().setProxyServer(proxy.getText());
			try {
				PrefsModel.getInstance().setPort(Integer.parseInt(port.getText().replaceAll("\\D", "")));
			}
			catch (NumberFormatException nfe){
				Logger.getLogger(this.getClass().getName()).warning("Incorrect port number; setting to 80");
			}
		}
		else {
			PrefsModel.getInstance().setProxyServer("");
			PrefsModel.getInstance().setPort(0);
		}
		
		return false;
	}
	
	@Override
	public JPanel getPreferencesPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		JPanel useProxyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel proxyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		proxy.setPreferredSize(InternalFormatter.getComponentSize(proxy, 200));
		port.setPreferredSize(InternalFormatter.getComponentSize(port, 50));
		
		useProxyPanel.add(useProxy);
		proxyPanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.PROXY_SERVER)));
		proxyPanel.add(proxy);
		proxyPanel.add(new JLabel(":"));
		proxyPanel.add(port);
		
		p.add(useProxyPanel);
		p.add(proxyPanel);
		
		return p;
	}
	
	public String getName() {
		return BuddiKeys.NETWORK.toString();
	}
}
