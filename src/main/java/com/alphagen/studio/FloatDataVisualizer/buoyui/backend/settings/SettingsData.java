package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppMode;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.Theme;
import lombok.Data;

@Data
class SettingsData {
	// file data
	private final String settingsVersion = "2.0.0";
	// app data
	private AppMode appMode = AppMode.USER;
	private Theme theme = Theme.LIGHT;
	// default autofill data
	private String default_teamInfo = "";
	private int default_baudRate = 115200;
	private String default_startDataTransferFlag = "--start-data-transfer";
	private String default_endDataTransferFlag = "--end-data-transfer";

}