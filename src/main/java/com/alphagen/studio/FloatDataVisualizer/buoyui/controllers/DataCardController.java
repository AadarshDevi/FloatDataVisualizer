package com.alphagen.studio.FloatDataVisualizer.buoyui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.SVGPath;

public class DataCardController {
	@FXML public Button dataCard;
	@FXML public Label baudRate;
	@FXML public Label port;
	@FXML public Label name;
	public ConnectionType connectionType;
	@FXML public SVGPath serial;
	@FXML public SVGPath wireless;
	private boolean isDisabled;

	public void setDataCard(String connectionName, int baudRateNum, String portName, ConnectionType connectionType) {
		setConnectionName(connectionName);
		setBaudRate(baudRateNum);
		setPort(portName);
		setConnectionType(connectionType);
	}


	public void setConnectionName(String connectionName) {
		name.setText(connectionName);
	}

	public void setBaudRate(int baudRateNum) {
		baudRate.setText(Integer.toString(baudRateNum));
	}

	public void setPort(String portName) {
		port.setText(portName);
	}

	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
		System.out.println("Finding Connection");
		switch (connectionType) {
			case SERIAL:
				System.out.println("Finding Serial");
				serial.setVisible(true);
				wireless.setVisible(false);
				System.out.println("Is Visible: " + serial.isVisible());
				break;
			case WIRELESS:
				System.out.println("Finding Wireless");
				serial.setVisible(false);
				wireless.setVisible(true);
				System.out.println("Is Visible: " + wireless.isVisible());
				break;
			default:
				System.out.println("Invalid Connection");
		}
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean disabled) {
		this.isDisabled = disabled;
	}
}