package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppMode;
import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
class SettingsData {
	// file data
	private final String settingsVersion = "2.0.0";
	// autoscroll
	private final AtomicBoolean terminalAutoscroll = new AtomicBoolean(true);
	private final AtomicBoolean tableAutoscroll = new AtomicBoolean(true);
	// app data
	private AppMode appMode = AppMode.USER;
	// default autofill data
	private String default_teamInfo = "";
	private int default_baudRate = 115200;
	private String default_startDataTransferFlag = "--start-data-transfer";
	private String default_endDataTransferFlag = "--end-data-transfer";

	public boolean getAutoscrollTerminal() {
		return terminalAutoscroll.get();
	}

	public void setAutoscrollTerminal(boolean autoscroll) {
		terminalAutoscroll.set(autoscroll);
	}

	public boolean getAutoscrollTable() {
		return tableAutoscroll.get();
	}

	public void setAutoscrollTable(boolean autoscroll) {
		tableAutoscroll.set(autoscroll);
	}

}