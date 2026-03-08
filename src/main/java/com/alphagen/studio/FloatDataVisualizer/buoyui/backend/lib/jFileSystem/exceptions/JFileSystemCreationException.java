package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jFileSystem.exceptions;

public class JFileSystemCreationException extends Exception {
	public JFileSystemCreationException(String message) {
		super("Unable to create JFileSystem: " + message);
	}
}