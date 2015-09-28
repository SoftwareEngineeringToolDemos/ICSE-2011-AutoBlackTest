package org.homeunix.thecave.buddi.plugin.api;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.MainFrame;

import ca.digitalcave.moss.application.document.DocumentChangeEvent;
import ca.digitalcave.moss.application.document.DocumentChangeListener;
import ca.digitalcave.moss.application.plugin.MossPlugin;
import ca.digitalcave.moss.swing.MossPanel;

/**
 *
 * @author mpeccorini
 */
public abstract class BuddiPanelPlugin extends MossPanel implements MossPlugin {
	private static final long serialVersionUID = 0L;
	private MainFrame parentFrame;
	protected Document document;
	private DocumentChangeListener listener;
//	private final Vector<PanelPluginClosedListener> pluginClosedListeners = new Vector<PanelPluginClosedListener>();
	private CloseTabButton tabButton;

	public abstract String getTabLabelKey();

	public abstract boolean isSingleton();

	public abstract boolean isStartedAutomatically();

	public abstract boolean isPersisted();

	public abstract void initPanel();

	public abstract void updatePanel();

	@Override
	public void init() {
		super.init();
		tabButton = new CloseTabButton(this, TextFormatter.getTranslation(getTabLabelKey()));
		initPanel();
	}

	@Override
	public void updateContent() {
		super.updateContent();
		updatePanel();
	}

	public void close() {
		parentFrame.removePanel(this);
//		for (PanelPluginClosedListener pluginClosedListener : pluginClosedListeners) {
//			pluginClosedListener.panelPluginClosed(this);
//		}
	}
//
//	public void addPanelPluginClosedListener(PanelPluginClosedListener pluginClosedListener) {
//		pluginClosedListeners.add(pluginClosedListener);
//	}
//
//	public void removePluginClosedListener(PanelPluginClosedListener pluginClosedListener) {
//		pluginClosedListeners.remove(pluginClosedListener);
//	}

	public MainFrame getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
		listener = new DocumentChangeListener() {

			public void documentChange(DocumentChangeEvent arg0) {
				updatePanel();
			}
		};
		this.document = (Document) parentFrame.getDocument();
		parentFrame.getDocument().addDocumentChangeListener(listener);
		updateContent();
	}

	public boolean wasInUse() {
		// Implement how to determine if the plugin was in use the last time Buddi
		// was executed. In the mean time, assume no panel plugins were in use.
		return false;
	}

	public JPanel getTabComponent(){
		return tabButton;
	}

	class CloseIcon implements Icon {

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(Color.RED);
			g.drawLine(4, 4, getIconWidth() - 5, getIconHeight() - 5);
			g.drawLine(getIconWidth() - 5, 4, 4, getIconHeight() - 5);
		}

		public int getIconWidth() {
			return 13;
		}

		public int getIconHeight() {
			return 13;
		}
	}

	class CloseTabButton extends JPanel implements ActionListener {
		private static final long serialVersionUID = 0L;
		
		BuddiPanelPlugin plugin;

		private CloseTabButton(BuddiPanelPlugin plugin, String title) {
			this.plugin = plugin;
			setOpaque(false);
			add(new JLabel(title,JLabel.LEFT));
			Icon closeIcon = new CloseIcon();
			JButton btClose = new JButton(closeIcon);
			btClose.setPreferredSize(new Dimension(
					closeIcon.getIconWidth(), closeIcon.getIconHeight()));
			add(btClose);
			btClose.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
//			plugin.close();
		}
	}
}
