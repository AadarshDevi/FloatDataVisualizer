package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformDetector {
	@Getter
	private static Platform OSPLATFORM;
	@Getter
	private static Path PROJECTROOT;

	public PlatformDetector() throws IOException {
		detectPlatform();
	}

	public void detectPlatform() throws IOException {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().contains("win")) {
			OSPLATFORM = Platform.WIN11;
			PROJECTROOT = Paths.get(System.getenv("APPDATA")).resolve("FloatDataVisualizer").resolve(AppData.RELEASE_VERSION);
		} else if (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("darwin")) {
			OSPLATFORM = Platform.MACOS;
		} else if (osName.toLowerCase().contains("linux")) {
			OSPLATFORM = Platform.LINUX;
			PROJECTROOT = Paths.get(System.getProperty("user.home"), "FloatDataVisualizer/2.0.0/");
		}
	}
}