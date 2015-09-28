/*
 * Created on Nov 27, 2008 by igx89
 */
package org.homeunix.thecave.buddi.plugin.builtin.cellrenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JList;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.Account;
import org.homeunix.thecave.buddi.model.Transaction;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.BuddiTransactionCellRendererPlugin;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.Formatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;

import ca.digitalcave.moss.common.OperatingSystemUtil;


/**
 * A modified version of wyatt's DefaultTransactionCellRenderer.
 * Tries to pack as much information into as little space as possible,
 * while still being very readable.
 * 
 * @author Matthew "IGx89" Lieder
 *
 */
public class ConciseTransactionCellRenderer extends BuddiTransactionCellRendererPlugin {
	public static final long serialVersionUID = 0;
	private Transaction transaction;

	private boolean showCleared = PrefsModel.getInstance().isShowCleared();
	private boolean showReconciled = PrefsModel.getInstance().isShowReconciled();
	protected boolean cheques = false;

	private int maxDescriptionLength = 50;

	private static final Color GREEN = new Color(0, 128, 0);

	public ConciseTransactionCellRenderer(){
		if (OperatingSystemUtil.isMac()){
			this.putClientProperty("Quaqua.Component.visualMargin", new Insets(0,0,0,0));
		}
		else {
			this.setOpaque(true);
		}

		//Make this big enough to have two lines of text.  Since the 
		// FontMetrics object is not yet initialized, we have to guess
		// the correct size, based on JLabel size.
		this.setPreferredSize(new Dimension(200, (int) new JLabel("<html>A<br>B</html>").getPreferredSize().getHeight()));
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof Transaction){
			transaction = (Transaction) value;
		}
		else {
			transaction = null;
		}

		maxDescriptionLength = list.getWidth() - 230;

		return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		final int height = this.getHeight();
		final int width = this.getWidth();

		// Antialias text
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

		if (transaction != null){
			final Color textColor = g.getColor();
			final int topRowYPos = height / 2 - 5;
			final int bottomRowYPos = height - 5;

			final Font f = g.getFont();
			final Font bold = new Font(f.getName(), Font.BOLD, f.getSize());
			final Font italic = new Font(f.getName(), Font.ITALIC, f.getSize());				

			final int amountXOffset = 180;

			//Get the font metrics for column alignment
			final FontMetrics fm = this.getGraphics().getFontMetrics();

			//Date
			g.drawString(TextFormatter.getDateFormat().format(transaction.getDate()), 10, topRowYPos);

			//Description
			g.setFont(bold);
			g.drawString(
					Formatter.getStringToLength(
							transaction.getDescription() + (cheques && transaction.getNumber() != null && transaction.getNumber().trim().length() > 0 ? "     #" + transaction.getNumber(): ""),
							maxDescriptionLength, fm), 
							150, 
							topRowYPos);
			g.setFont(f);

			//Memo
			g.drawString(
					Formatter.getStringToLength(
							transaction.getMemo() != null ? transaction.getMemo() : "",
							width - amountXOffset, fm
					), 
					150, 
					bottomRowYPos);

			//Cleared and Reconciled
			if (getAccount() != null){
				if (showCleared){
					g.setColor(GREEN);
					if (getAccount().equals(transaction.getTo()) && transaction.isClearedTo()
							|| getAccount().equals(transaction.getFrom()) && transaction.isClearedFrom())
						g.drawString(PrefsModel.getInstance().getTranslator().get(BuddiKeys.SHORT_CLEARED), 20, bottomRowYPos);
					g.setColor(textColor);
				}
				if (showReconciled){
					g.setColor(GREEN);
					if (getAccount().equals(transaction.getTo()) && transaction.isReconciledTo()
							|| getAccount().equals(transaction.getFrom()) && transaction.isReconciledFrom())
						g.drawString(PrefsModel.getInstance().getTranslator().get(BuddiKeys.SHORT_RECONCILED), 30, bottomRowYPos);
					g.setColor(textColor);
				}
			}

			//Amount renderer when account is null
			int xPos = 0, minXPos = Integer.MAX_VALUE;
			if (getAccount() == null){
				long value = transaction.getAmount();
				xPos = width - 20 - fm.stringWidth(TextFormatter.getFormattedCurrency(value, false, false));
				if (xPos < minXPos) minXPos = xPos;
				g.setColor(InternalFormatter.isRed(transaction) ? Color.RED : textColor);
				g.drawString(TextFormatter.getFormattedCurrency(value, false, false), xPos, bottomRowYPos);
				g.setColor(textColor);
			}
			else {
				//Amount
				xPos = width - amountXOffset;
				if (xPos < minXPos) minXPos = xPos;
				g.setColor(InternalFormatter.isRed(transaction, transaction.getTo().equals(getAccount())) ? Color.RED : textColor);
				g.drawString(TextFormatter.getFormattedCurrency(transaction.getAmount(), false, false), xPos, bottomRowYPos);
				g.setColor(textColor);

				//Balance - max 150px
				g.setFont(bold);
				final long balanceValue = transaction.getBalance(getAccount().getUid());

				xPos = width - 20 - fm.stringWidth(TextFormatter.getFormattedCurrency(balanceValue, false, getAccount().getAccountType().isCredit()));
				if (xPos < minXPos) minXPos = xPos;
				g.setColor(InternalFormatter.isRed(getAccount(), balanceValue) ? Color.RED : textColor);
				g.drawString(TextFormatter.getFormattedCurrency(balanceValue, false, getAccount().getAccountType().isCredit()), xPos, bottomRowYPos);
				g.setColor(textColor);
			}
			g.setFont(f);

			//To / From sources
			g.setFont(italic);
			final String categoryName = transaction.getFrom() instanceof Account ? transaction.getTo().getFullName() : transaction.getFrom().getFullName();
			xPos = width - 20 - fm.stringWidth(categoryName);
			if (xPos < minXPos) minXPos = xPos;
			g.drawString(categoryName, xPos, topRowYPos);

			if (transaction.isDeleted()){
				g.setFont(new Font(f.getName(), Font.BOLD, (int) (f.getSize() * 2.5)));
				g.setColor(new Color(0, 0, 0, 64));
				g.drawString(TextFormatter.getTranslation(BuddiKeys.VOID_TRANSACTION), maxDescriptionLength / 2, (int) (f.getSize() * 2.5 + 2));
			}
		}
	}

	@Override
	public String getName() {
		return TextFormatter.getTranslation(BuddiKeys.CONCISE_TRANSACTION_CELL_RENDERER);
	}
}