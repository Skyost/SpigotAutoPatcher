package fr.skyost.patcher.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import javax.swing.JProgressBar;

import net.md_5.jbeat.Patcher;

public class Utils {

	public static final boolean patch(final File originalFile, final File patchFile, final File outputFile) {
		if(!originalFile.canRead()) {
			System.out.println("Specified original file " + originalFile + " does not exist or cannot be read !");
			return false;
		}
		if(!patchFile.canRead()) {
			System.out.println("Specified patch file " + patchFile + " does not exist or cannot be read !");
			return false;
		}
		System.out.println("***** Starting patching process, please wait.");
		System.out.println("\tInput md5 Checksum: " + getMD5(originalFile));
		System.out.println("\tPatch md5 Checksum: " + getMD5(patchFile));

		try {
			new Patcher(patchFile, originalFile, outputFile).patch();
		}
		catch(final Exception ex) {
			System.out.println("***** Exception occured whilst patching file!");
			outputFile.delete();
			return false;
		}
		System.out.println("***** Your file has been patched and verified! We hope you enjoy using Spigot!");
		System.out.println("\tOutput md5 Checksum: " + getMD5(outputFile));
		return true;
	}

	private static String getMD5(final File file) {
		try(FileInputStream inputStream = new FileInputStream(file)) {
			final MessageDigest digest = MessageDigest.getInstance("MD5");

			final byte[] bytesBuffer = new byte[1024];
			int bytesRead = -1;

			while((bytesRead = inputStream.read(bytesBuffer)) != -1) {
				digest.update(bytesBuffer, 0, bytesRead);
			}

			final byte[] hashedBytes = digest.digest();

			return convertByteArrayToHexString(hashedBytes);
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static String convertByteArrayToHexString(final byte[] arrayBytes) {
		final StringBuilder builder = new StringBuilder();
		for(final byte arrayByte : arrayBytes) {
			builder.append(Integer.toString((arrayByte & 0xff) + 0x100, 16).substring(1));
		}
		return builder.toString();
	}

	public static final boolean download(final String site, final File dest, final JProgressBar progressBar) {
		try {
			System.out.println("Downloading " + site + "...");
			final URLConnection connection = new URL(site).openConnection();
			connection.addRequestProperty("User-Agent", "SpigotObtainer");
			final long size = connection.getContentLengthLong();
			final BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			final FileOutputStream out = new FileOutputStream(dest);
			final byte data[] = new byte[1024];
			int count;
			double sumCount = 0.0;
			while((count = in.read(data, 0, 1024)) != -1) {
				out.write(data, 0, count);
				sumCount += count;
				if(size > 0) {
					final int percent = (int)(sumCount / size * 100.0);
					System.out.println(percent + "%");
					progressBar.setValue(percent);
				}
			}
			out.close();
			in.close();
			return true;
		}
		catch(final Exception ex) {
			if(dest.exists()) {
				dest.delete();
			}
			ex.printStackTrace();
		}
		return false;
	}
	
	public static final void cleanDir(final File dir) {
		for(final File file : dir.listFiles()) {
			if(file.isDirectory()) {
				cleanDir(dir);
				dir.delete();
			}
			else {
				file.delete();
			}
		}
	}
	
}
