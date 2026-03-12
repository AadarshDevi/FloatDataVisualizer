package com.alphagen.studio.FloatDataVisualizer.buoyui.backend;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Backend {

	public static Backend backend;
	@Getter private static Path PROJECTROOT;

	private Backend() throws IOException {
		PROJECTROOT = PlatformDetector.getPROJECTROOT();
		Files.createDirectories(PROJECTROOT);
	}

	public static Backend getBackend() throws IOException {
		if (backend == null) backend = new Backend();
		return backend;
	}

	public boolean verifyFolder(String folder) throws IOException {
		if (!Files.isDirectory(PROJECTROOT.resolve(folder)))
			Files.createDirectories(PROJECTROOT.resolve(folder));
		return true;
	}

	public boolean verifyFile(String file) throws IOException {
		if (!Files.isRegularFile(PROJECTROOT.resolve(file)))
			Files.createFile(PROJECTROOT.resolve(file));
		return true;
	}

	public boolean verifyRoot() {
		return Files.isDirectory(PROJECTROOT);
	}

}