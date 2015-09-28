/*
 * Created on May 17, 2006 by wyatt
 */
package org.homeunix.thecave.buddi.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.homeunix.thecave.buddi.Buddi;
import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.ButtonKeys;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.view.menu.bars.BuddiMenuBar;

import ca.digitalcave.moss.common.ClassLoaderFunctions;
import ca.digitalcave.moss.common.OperatingSystemUtil;
import ca.digitalcave.moss.swing.MossFrame;
import edu.stanford.ejalbert.BrowserLauncher;

public class AboutFrame extends MossFrame implements ActionListener {
	public static final long serialVersionUID = 0;

	private final JButton okButton;
	private final JButton donateButton;
	private final JLabel text;

	public AboutFrame() {
		super("AboutFrame");
		this.setIconImage(ClassLoaderFunctions.getImageFromClasspath("img/BuddiFrameIcon.gif"));

		okButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_OK));
		donateButton = new JButton(TextFormatter.getTranslation(ButtonKeys.BUTTON_DONATE));

		Dimension buttonSize = new Dimension(Math.max(100, donateButton.getPreferredSize().width), donateButton.getPreferredSize().height);

		okButton.setPreferredSize(buttonSize);
		donateButton.setPreferredSize(buttonSize);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(donateButton);
		buttonPanel.add(okButton);

		StringBuffer sbTitle = new StringBuffer();
		sbTitle.append(
		"<html><center><h1>")
		.append(TextFormatter.getTranslation(BuddiKeys.BUDDI))
		.append("</h1></center></html>");

		StringBuffer sbVersion = new StringBuffer();
		sbVersion.append(
		"<html><center><h5>")
		.append(TextFormatter.getTranslation(BuddiKeys.ABOUT_VERSION))
		.append(" ")
		.append(Buddi.getVersion())
		.append("</h5></center></html");

		JLabel version = new JLabel(sbVersion.toString());

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
		bottomPanel.add(version, BorderLayout.NORTH);

		JLabel title = new JLabel(sbTitle.toString());
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.add(title);

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (null == cl) {
			cl = MossFrame.class.getClassLoader();
		}


		title.setIcon(new ImageIcon(ClassLoaderFunctions.getImageFromClasspath("img/BuddiAboutLogo.png")));
		title.setVerticalTextPosition(JLabel.BOTTOM);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setHorizontalTextPosition(JLabel.CENTER);

		StringBuffer sbText = new StringBuffer();
		sbText.append(
		"<html><center>")
		.append(TextFormatter.getTranslation(BuddiKeys.ABOUT_TEXT))
		.append("<br><br>")
		.append(TextFormatter.getTranslation(BuddiKeys.ABOUT_COPYRIGHT))
		.append("<br>&lt;")
		.append(TextFormatter.getTranslation(BuddiKeys.ABOUT_EMAIL))
		.append("&gt;<br><a href='")
		.append(TextFormatter.getTranslation(Const.PROJECT_URL))
		.append("'>")
		.append(TextFormatter.getTranslation(Const.PROJECT_URL))
		.append("</a><br><br>");
		if (!TextFormatter.getTranslation(BuddiKeys.ABOUT_TRANSLATION).equals(BuddiKeys.ABOUT_TRANSLATION.toString())){
			sbText.append(TextFormatter.getTranslation(BuddiKeys.ABOUT_TRANSLATION))
			.append("<br><br>");
		}
		sbText.append(TextFormatter.getTranslation(BuddiKeys.ABOUT_GPL))
		.append("</center></html>");

		text = new JLabel(sbText.toString());

		JPanel inlayPanel = new JPanel(new BorderLayout());
		inlayPanel.add(text, BorderLayout.CENTER);
		inlayPanel.add(bottomPanel, BorderLayout.SOUTH);

		if (OperatingSystemUtil.isMac()){
			inlayPanel.setBorder(BorderFactory.createTitledBorder(""));
		}

		this.setLayout(new BorderLayout());
		this.getRootPane().setDefaultButton(okButton);
		this.setTitle(TextFormatter.getTranslation(BuddiKeys.ABOUT_BUDDI));
		this.add(titlePanel, BorderLayout.NORTH);
		this.add(inlayPanel, BorderLayout.CENTER);
	}

	public void init() {
		super.init();

		this.setJMenuBar(new BuddiMenuBar(this));
		
		okButton.addActionListener(this);
		donateButton.addActionListener(this);

		text.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				try{
					BrowserLauncher bl = new BrowserLauncher(null);
					bl.openURLinBrowser(Const.PROJECT_URL);
				}
				catch (Exception ex){
					Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unknown Exception", ex);
				}
				super.mouseClicked(e);
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(okButton)){
			AboutFrame.this.closeWindow();
		}
		else if (e.getSource().equals(donateButton)){		
			try{
				BrowserLauncher bl = new BrowserLauncher(null);
				bl.openURLinBrowser(Const.DONATE_URL);
			}
			catch (Exception ex){
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unknown Exception", ex);
			}
		}
	}

	@Override
	public void closeWindowWithoutPrompting() {
		PrefsModel.getInstance().putWindowLocation(BuddiKeys.ABOUT_BUDDI.toString(), this.getLocation());
		PrefsModel.getInstance().save();
		
		super.closeWindowWithoutPrompting();
	}
}
