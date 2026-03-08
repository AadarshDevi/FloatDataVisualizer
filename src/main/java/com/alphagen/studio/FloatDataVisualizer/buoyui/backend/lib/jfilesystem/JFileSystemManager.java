package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jfilesystem;

import java.util.HashMap;

public class JFileSystemManager {
	public static HashMap<String, JFileSystem> fileSystems = new HashMap<String, JFileSystem>();

	public static JFileSystem getJFileSystem(String name) {
		JFileSystem fileSystem = fileSystems.get(name);
		return fileSystem;
	}

	public static void addJFileSystem(String name, JFileSystem fileSystem) {
		fileSystems.put(name, fileSystem);
	}

	public static void removeJFileSystem(String name) {
		fileSystems.remove(name);
	}

	public static JFileSystem takeJFileSystem(String name) {
		JFileSystem fileSystem = fileSystems.remove(name);
		return fileSystem;
	}
}