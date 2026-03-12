package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard;

import com.alphagen.studio.FloatDataVisualizer.buoyui.Controller;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataCardController extends Controller {
	@FXML public Button dataCard;
	@FXML public Label baudRate;
	@FXML public Label port;
	@FXML public Label name;
	@FXML public Button serial;
	@FXML public Button wireless;
	@FXML public ContextMenu connectionOptions;
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
		invalidConnectionData();
		name.setText(connectionName);
	}

	private void setBaudRate(int baudRateNum) {
		baudRate.setText(Integer.toString(baudRateNum));
	}

	private void setPort(SerialPort portName) {
		invalidConnectionData();
		port.setText(portName.getSystemPortName());
	}

	private void setConnectionType(ConnectionType connectionType) {
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
		}
	}

	public void invalidConnectionData() {
		if (name == null || port == null || port.getText().isEmpty() || name.getText().isEmpty()) {
			isWorking = false;
			isDisabled = true;
			dataCard.setOpacity(0.4);
		}
	}

	public void deleteConnection() {
		System.out.println("Deleting Connection");

		Path connectionFile = FolderConstants.CONNECTIONS.resolve(connectionConfig.connectionName() + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION);
		try {
			Files.delete(connectionFile);
		} catch (IOException e) {
			System.err.println("Unable to delete connection: " + connectionConfig.connectionName());
			e.printStackTrace();
			return;
		}

		ControllerManager.getConnectionsController().deleteConnection(dataCard);
		System.out.println("Deleted Connection: " + connectionConfig.connectionName());
	}

	public void setConnection(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
		ControllerManager.getConnectionsController().setCurrentConnectionConfig(this.connectionConfig);
		setBaudRate(connectionConfig.baudRate());
		setConnectionName(connectionConfig.connectionName());
		setPort(connectionConfig.port());
		setConnectionType(connectionConfig.portType());
	}
}