package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard;

import com.alphagen.studio.FloatDataVisualizer.buoyui.Controller;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

public class DataCardController extends Controller {
	@FXML public Button dataCard;
	@FXML public Label baudRate;
	@FXML public Label port;
	@FXML public Label name;
	@FXML public Button serial;
	@FXML public Button wireless;
	@FXML public ContextMenu connectionOptions;
	@FXML public MenuItem deleteDataCard;
	@Getter private ConnectionConfig connectionConfig;

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

	public void setConnection(String connectionName, int baudRateNum, SerialPort portName, ConnectionType connectionType) {
		setConnectionName(connectionName);
		setBaudRate(baudRateNum);
		setPort(portName);
		setConnectionType(connectionType);
	}

	private void setConnectionName(String connectionName) {
		System.out.println("Connection Start?");
		System.out.println("--> " + connectionName);
		invalidConnectionData();
		name.setText(connectionName);
	}

	private void setBaudRate(int baudRateNum) {
		baudRate.setText(Integer.toString(baudRateNum));
	}

	private void setPort(SerialPort portName) {
		System.out.println("New Connection Set?");
		System.out.println("--> " + portName);
		invalidConnectionData();
		port.setText(portName.getDescriptivePortName());
	}

	private void setConnectionType(ConnectionType connectionType) {
		System.out.println("Finding Connection");
		switch (connectionType) {
			case SERIAL:
				serial.setVisible(true);
				wireless.setVisible(false);
				break;
			case WIRELESS:
				serial.setVisible(false);
				wireless.setVisible(true);
				break;
			default:
				System.out.println("Invalid Connection");
				return;
		}
		System.out.println("Connection Type: " + connectionType);
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
		ControllerManager.getConnectionsController().deleteConnection(dataCard);
	}

	public void setConnection(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
		System.out.println("Connection --> full config");
		ControllerManager.getConnectionsController().setCurrentConnectionConfig(this.connectionConfig);
		setBaudRate(connectionConfig.baudRate());
		setConnectionName(connectionConfig.connectionName());
		setPort(connectionConfig.port());
		setConnectionType(connectionConfig.portType());
	}
}