package com.alphagen.studio.FloatDataVisualizer.buoyui.connections;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

public class DataCardController {
	@FXML public Button dataCard;
	@FXML public Label baudRate;
	@FXML public Label port;
	@FXML public Label name;
	@FXML public Button serial;
	@FXML public Button wireless;
	@FXML public ContextMenu connectionOptions;
	@FXML public MenuItem deleteDataCard;
	@Getter private ConnectionType connectionType;

	@Setter
	@Getter
	private boolean isDisabled;

	@Setter
	@Getter
	private boolean isWorking;

	@FXML
	public void initialize() {
		if (dataCard.getContextMenu() == null) {
			System.out.println("No ContextMenu Present");
			dataCard.setContextMenu(connectionOptions);
		}
		System.out.println("Data 1-1: " + dataCard.hashCode());
	}

	@FXML
	public void handleContextMenu(MouseEvent event) {
		if (!isDisabled && event.getButton() == MouseButton.SECONDARY)
			connectionOptions.show(dataCard, event.getSceneX(), event.getSceneY());
		else if (isDisabled) event.consume();
	}

	public void setDataCard(String connectionName, int baudRateNum, String portName, ConnectionType connectionType) {
		setConnectionName(connectionName);
		setBaudRate(baudRateNum);
		setPort(portName);
		setConnectionType(connectionType);
	}

	public void setConnectionName(String connectionName) {
		invalidConnectionData();
		name.setText(connectionName);
	}

	public void setBaudRate(int baudRateNum) {
		baudRate.setText(Integer.toString(baudRateNum));
	}

	public void setPort(String portName) {
		invalidConnectionData();
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
				break;
			case WIRELESS:
				System.out.println("Finding Wireless");
				serial.setVisible(false);
				wireless.setVisible(true);
				break;
			default:
				System.out.println("Invalid Connection");
		}
	}

	public void invalidConnectionData() {
		System.out.println("Name: " + name);
		System.out.println("Port: " + port);
		if (name == null || port == null || port.getText().isEmpty() || name.getText().isEmpty()) {
			isWorking = false;
			isDisabled = true;
			dataCard.setOpacity(0.4);
		}
	}

	public void deleteConnection() {
		System.out.println("Deleting Connection");
		ConnectionControllerManager.getConnectionsController().deleteConnection(dataCard);
	}
}