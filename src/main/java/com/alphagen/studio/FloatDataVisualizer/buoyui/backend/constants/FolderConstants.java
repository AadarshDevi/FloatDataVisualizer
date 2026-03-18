package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.Backend;

import java.nio.file.Path;

public class FolderConstants {
	public static final String FLOAT_CONNECTION_FILE_EXTENSION = ".float.connection";
	private static final Path PROJECTROOT = Backend.getPROJECTROOT();
	public static final Path LOGS = PROJECTROOT.resolve("logs");
	public static final Path CONNECTIONS = PROJECTROOT.resolve("connections");
}