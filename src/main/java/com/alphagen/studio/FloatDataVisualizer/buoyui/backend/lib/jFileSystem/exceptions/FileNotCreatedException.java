package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jFileSystem.exceptions;

public class FileNotCreatedException extends Exception {
	public FileNotCreatedException(String message) {
		super("Unable to create file: " + message);
	}
}