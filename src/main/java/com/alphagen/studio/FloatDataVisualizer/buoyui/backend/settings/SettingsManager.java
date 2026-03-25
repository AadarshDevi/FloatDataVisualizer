package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppMode;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsManager {

	private static SettingsManager sm;
	private final SettingsData settingsData;

	private SettingsManager() {
		settingsData = readSettings();
	}

	// todo: read settings
	private SettingsData readSettings() {

		final Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(FolderConstants.SETTINGS.toUri())));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

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

	}

}