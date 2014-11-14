package fr.skyost.patcher;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.UIManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.skyost.patcher.dialogs.WaitingDialog;
import fr.skyost.patcher.utils.SystemManager;
import fr.skyost.patcher.utils.Utils;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SpigotAutoPatcher extends JFrame {

	public static final String APP_NAME = "Spigot Auto Patcher";
	public static final String APP_VERSION = "0.1";
	public static final String APP_AUTHOR = "Skyost (http://www.skyost.eu)";

	private static final long serialVersionUID = 1L;
	private static String latestVersion;

	public SpigotAutoPatcher() {
		this.setTitle(APP_NAME + " v" + APP_VERSION + " - By " + APP_AUTHOR);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(SpigotAutoPatcher.class.getResource("/fr/skyost/patcher/res/spigot.png")));
		this.setSize(422, 274);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel lblSpigot = new JLabel(new ImageIcon(SpigotAutoPatcher.class.getResource("/fr/skyost/patcher/res/spigot.png")));
		lblSpigot.setHorizontalAlignment(SwingConstants.CENTER);
		final JLabel lblSpigotUrl = new JLabel("Spigot #1649 :");
		final JTextField txtfldSpigotUrl = new JTextField();
		txtfldSpigotUrl.setText("http://spigotmc.info/spigot1649.jar");
		txtfldSpigotUrl.setColumns(10);
		final JLabel lblVersion = new JLabel("Patch's URL :");
		final JTextField txtfldVersion = new JTextField(latestVersion == null ? "" : "http://www.spigotmc.org/spigot-updates/" + latestVersion);
		txtfldVersion.setColumns(10);
		final JButton btnDownloadAndPatch = new JButton("Download and patch !");
		btnDownloadAndPatch.setFont(btnDownloadAndPatch.getFont().deriveFont(Font.BOLD));
		btnDownloadAndPatch.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				new Thread() {

					@Override
					public final void run() {
						SpigotAutoPatcher.this.setVisible(false);
						final File appDir = SystemManager.getApplicationDirectory();
						if(!appDir.exists()) {
							appDir.mkdirs();
						}
						Utils.cleanDir(appDir);
						final File patchFile = new File(appDir, latestVersion);
						if(!downloadFile("Downloading the patch file...", txtfldVersion.getText(), patchFile)) {
							JOptionPane.showMessageDialog(SpigotAutoPatcher.this, "Cannot download the patch file.", "Error !", JOptionPane.ERROR_MESSAGE);
							SpigotAutoPatcher.this.setVisible(true);
							Utils.cleanDir(appDir);
							return;
						}
						final File originalFile = new File(appDir, "Spigot-1649.jar");
						if(!downloadFile("Downloading Spigot #1649...", txtfldSpigotUrl.getText(), originalFile)) {
							JOptionPane.showMessageDialog(SpigotAutoPatcher.this, "Cannot download Spigot #1649.", "Error !", JOptionPane.ERROR_MESSAGE);
							SpigotAutoPatcher.this.setVisible(true);
							Utils.cleanDir(appDir);
							return;
						}
						final JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileFilter(new FileNameExtensionFilter("Java Archive (*.jar)", "jar"));
						if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
							final String outputFilePath = fileChooser.getSelectedFile().getPath();
							final File outputFile = new File(outputFilePath.endsWith(".jar") ? outputFilePath : outputFilePath + ".jar");
							if(outputFile.exists()) {
								JOptionPane.showMessageDialog(SpigotAutoPatcher.this, "A file with the same name already exists !", "Error !", JOptionPane.ERROR_MESSAGE);
								return;
							}
							if(!Utils.patch(originalFile, patchFile, outputFile)) {
								JOptionPane.showMessageDialog(SpigotAutoPatcher.this, "Cannot patch Spigot.", "Error !", JOptionPane.ERROR_MESSAGE);
								SpigotAutoPatcher.this.setVisible(true);
								return;
							}
							if(Desktop.isDesktopSupported()) {
								try {
									Desktop.getDesktop().open(outputFile.getParentFile());
								}
								catch(final Exception ex) {
									ex.printStackTrace();
								}
							}
							SpigotAutoPatcher.this.dispose();
						}
						else {
							SpigotAutoPatcher.this.setVisible(true);
						}
					}

				}.start();

			}
		});
		final Container contentPane = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(contentPane);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblSpigot, GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE).addComponent(btnDownloadAndPatch, GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE).addGroup(Alignment.LEADING, groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblSpigotUrl).addComponent(lblVersion)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(txtfldVersion, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE).addComponent(txtfldSpigotUrl, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)))).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblSpigot).addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblSpigotUrl).addComponent(txtfldSpigotUrl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(txtfldVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblVersion)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnDownloadAndPatch).addContainerGap()));
		contentPane.setLayout(groupLayout);
	}

	public static final void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			final WaitingDialog dialog = new WaitingDialog("Getting the latest version infos...");
			dialog.setVisible(true);
			new Thread() {

				@Override
				public final void run() {
					try {
						System.out.println("Getting the latest version...");
						final Connection connection = (HttpConnection)Jsoup.connect("http://www.spigotmc.org/spigot-updates/");
						connection.data("query", "Java");
						connection.userAgent("SpigotObtainer");
						connection.timeout(10000);
						final Elements files = connection.get().getElementsByAttribute("href");
						dialog.setPercent(50);
						final Element latest = files.get(files.size() - 2);
						latestVersion = latest.ownText();
						dialog.setPercent(100);
						System.out.println("Latest version : " + latestVersion);
						dialog.dispose();
					}
					catch(final Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, ex.getClass().getName(), "Error !", JOptionPane.ERROR_MESSAGE);
					}
					dialog.dispose();
					new SpigotAutoPatcher().setVisible(true);
				}
			}.start();
		}
		catch(final Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getClass().getName(), "Error !", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	private final boolean downloadFile(final String dialogContent, final String url, final File dest) {
		final WaitingDialog dialog = new WaitingDialog(dialogContent);
		dialog.setVisible(true);
		final boolean result = Utils.download(url, dest, dialog.getProgressBar());
		dialog.dispose();
		return result;
	}
}
