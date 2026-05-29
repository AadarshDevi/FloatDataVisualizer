package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppMode;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.theme.Theme;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.theme.ThemeProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;

import java.io.*;
import java.util.Properties;

public class SettingsManager {

	private static SettingsManager sm;
	private final SettingsData settingsData;
	private Properties properties;

	private SettingsManager() {
		settingsData = readSettings();
	}

	// todo: read settings
	private SettingsData readSettings() {

		properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(FolderConstants.SETTINGS.toUri())));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ThemeProcessor.setTheme(Theme.valueOf(properties.getProperty("theme")));
		System.out.println("theme: " + ThemeProcessor.getTheme());
		return null;
	}

	public static SettingsManager getInstance() {
		if (sm == null) sm = new SettingsManager();
		return sm;
	}

	public AppMode getAppMode() {
		return settingsData.getAppMode();
	}

	public void setAppMode(AppMode newAppMode) {
		if (newAppMode == AppMode.DEV)
			settingsData.setAppMode(newAppMode);
	}

	// todo: write settings
	public void writeSettings() {
		try (PrintWriter pw = new PrintWriter(new File(FolderConstants.SETTINGS.toUri()))) {
			pw.println("theme=" + ThemeProcessor.getTheme());
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find settings file.");
		}
	}

}