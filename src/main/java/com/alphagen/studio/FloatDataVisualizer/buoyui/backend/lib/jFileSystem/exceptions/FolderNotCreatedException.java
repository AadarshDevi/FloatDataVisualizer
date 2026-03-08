package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jFileSystem.exceptions;

public class FolderNotCreatedException extends Exception {
	public FolderNotCreatedException(String message) {
		super("Unable to create folder: " + message);
	}
}