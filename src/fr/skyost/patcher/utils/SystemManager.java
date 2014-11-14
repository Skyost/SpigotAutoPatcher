package fr.skyost.patcher.utils;

import java.io.File;

public class SystemManager {

	private static final Platform platform = setup();
	
	private static final Platform setup() {
		final OS os;
		final String osName = System.getProperty("os.name").toLowerCase();
		if(osName.contains("win")) {
			os = OS.WINDOWS;
		}
		else if(osName.contains("mac")) {
			os = OS.MAC;
		}
		else {
			os = OS.LINUX;
		}
		final Arch arch;
		final String archName = System.getProperty("os.arch");
		if(archName.contains("64")) {
			arch = Arch.X64;
		}
		else {
			arch = Arch.X86;
		}
		return new Platform(os, arch);
	}

	public static final Platform getPlatform() {
		return platform;
	}

	public static final File getSkyostDirectory() {
		return new File(getUserDirectory() + File.separator + ".skyost" + File.separator);
	}

	public static final File getApplicationDirectory() {
		return new File(getSkyostDirectory() + File.separator + "SpigotAutoPatcher" + File.separator);
	}

	public static final File getUserDirectory() {
		final String dirName;
		switch(platform.getOS()) {
		case WINDOWS:
			dirName = System.getenv("APPDATA");
			break;
		case MAC:
			dirName = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support";
			break;
		default:
			dirName = System.getProperty("user.home");
			break;
		}
		return new File(dirName);
	}

	public enum OS {
		
		WINDOWS("Windows"),
		MAC("Mac OS X"),
		LINUX("Other (Linux ?)");

		private final String name;

		OS(final String name) {
			this.name = name;
		}

		public final String getName() {
			return name;
		}

	}

	public enum Arch {
		
		X86("x86"),
		X64("x64");

		private final String name;

		Arch(final String name) {
			this.name = name;
		}

		public final String getName() {
			return name;
		}
		
	}

	public static class Platform {

		private final OS os;
		private final Arch arch;

		public Platform(final OS os, final Arch arch) {
			this.os = os;
			this.arch = arch;
		}

		public final OS getOS() {
			return os;
		}

		public final Arch getArch() {
			return arch;
		}
		
	}
	
}
