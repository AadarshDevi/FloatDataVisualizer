package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.theme.Theme;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.theme.ThemeProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import lombok.Getter;

import java.io.*;
import java.util.Properties;

public class SettingsManager {

	private static SettingsManager sm;
	@Getter
	private final SettingsData settingsData;
	private Properties properties;

	private SettingsManager() {
		settingsData = new SettingsData();
		readSettings();
	}

	// todo: read settings
	private void readSettings() {

		properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(FolderConstants.SETTINGS.toUri())));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ThemeProcessor.setTheme(Theme.valueOf(properties.getProperty(SettingsField.theme)));
		settingsData.setAutoscrollTerminal(Boolean.parseBoolean(properties.getProperty(SettingsField.terminalAutoscroll)));
		settingsData.setAutoscrollTable(Boolean.parseBoolean(properties.getProperty(SettingsField.tableAutoScroll)));
	}

	public static SettingsManager getInstance() {
		if (sm == null) sm = new SettingsManager();
		return sm;
	}

	// todo: write settings
	public void writeSettings() {
		try (PrintWriter pw = new PrintWriter(new File(FolderConstants.SETTINGS.toUri()))) {
			pw.println(SettingsField.theme + "=" + ThemeProcessor.getTheme());
			pw.println(SettingsField.terminalAutoscroll + "=" + settingsData.getAutoscrollTerminal());
			pw.println(SettingsField.tableAutoScroll + "=" + settingsData.getAutoscrollTable());
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find settings file.");
		}
	}


	public boolean getAutoscrollTerminal() {
		return settingsData.getAutoscrollTerminal();
	}

	public void setAutoscrollTerminal(boolean autoscroll) {
		settingsData.setAutoscrollTerminal(autoscroll);
	}

	public boolean getAutoscrollTable() {
		return settingsData.getAutoscrollTable();
	}

	public void setAutoscrollTable(boolean autoscroll) {
		settingsData.setAutoscrollTable(autoscroll);
	}

}