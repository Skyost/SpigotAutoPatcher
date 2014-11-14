package fr.skyost.patcher.dialogs;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;

import javax.swing.SwingConstants;

import fr.skyost.patcher.SpigotAutoPatcher;

import java.awt.Font;

public class WaitingDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JProgressBar progressBar = new JProgressBar();

	public WaitingDialog(final String label) {
		this.setTitle("Please wait...");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(SpigotAutoPatcher.class.getResource("/fr/skyost/patcher/res/spigot.png")));
		this.setSize(450, 90);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		final JLabel lblDownloading = new JLabel(label);
		lblDownloading.setFont(lblDownloading.getFont().deriveFont(Font.BOLD));
		lblDownloading.setHorizontalAlignment(SwingConstants.CENTER);
		final Container contentPane = this.getContentPane();
		contentPane.add(lblDownloading, BorderLayout.NORTH);
		progressBar.setStringPainted(true);
		contentPane.add(progressBar, BorderLayout.CENTER);
	}
	
	public final JProgressBar getProgressBar() {
		return progressBar;
	}
	
	public final void setPercent(final int i) {
		progressBar.setValue(i);
	}

}
