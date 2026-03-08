package com.alphagen.studio.FloatDataVisualizer.buoyui.connections;

import com.alphagen.studio.FloatDataVisualizer.buoyui.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.util.DynamicCSS;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ConnectionEditorController {

	@FXML public Label error_label_connection_name_blank;
	@FXML public Label error_label_baud_rate_invalid_characters;
	@FXML public Label error_label_baud_rate_blank;
	@FXML public Label error_label_port_blank;
	@FXML public Label error_label_data_format_blank;
	@FXML public Button confirmButton;
	@FXML public CheckBox rememberDataFormat;
	@FXML TextField connectionName;
	@FXML TextField baudRate;
	@FXML TextField dataFormat;
	@FXML ChoiceBox<String> connectionOptions;
	@FXML private Button closeButton;

	@FXML
	public void initialize() {
		resetErrorLabels();

		SerialPort[] serialPorts = SerialPort.getCommPorts();
		for (SerialPort serialPort : serialPorts) {
			String portName = serialPort.getSystemPortName();
			String portDescription = serialPort.getDescriptivePortName();
			int lastSpace = portDescription.lastIndexOf(' ');
			connectionOptions.getItems().add(portName + ": " + portDescription.substring(0, lastSpace));
		}
	}

	public void resetErrorLabels() {
		error_label_connection_name_blank.setVisible(false);
		error_label_baud_rate_invalid_characters.setVisible(false);
		error_label_baud_rate_blank.setVisible(false);
		error_label_port_blank.setVisible(false);
		error_label_data_format_blank.setVisible(false);
	}

	@FXML
	public void confirmConnectionEditor() {

		if (connectionName.getText().isEmpty()) {
			error_label_connection_name_blank.setVisible(true);
			connectionName.getStyleClass().add(DynamicCSS.ERROR);
		} else {
			error_label_connection_name_blank.setVisible(false);
			connectionName.getStyleClass().remove(DynamicCSS.ERROR);
		}

		if (baudRate.getText().trim().isEmpty()) {
			error_label_baud_rate_blank.setVisible(true);
			baudRate.getStyleClass().add(DynamicCSS.ERROR);
		} else {
			error_label_baud_rate_blank.setVisible(false);
			baudRate.getStyleClass().remove(DynamicCSS.ERROR);
		}

		String value = connectionOptions.getValue();
		if (value == null || value.isEmpty()) {
			error_label_port_blank.setVisible(true);
			connectionOptions.getStyleClass().add(DynamicCSS.ERROR);
		} else {
			error_label_port_blank.setVisible(false);
			connectionOptions.getStyleClass().remove(DynamicCSS.ERROR);
		}

//		rememberDataFormat.setSelected(true);// todo get from settings
		if (rememberDataFormat.isSelected()) {
			dataFormat.setText("PN12-MiramarWaterJets"); // todo: replace to fix team data
		}

		if (dataFormat.getText().trim().isEmpty()) {
			error_label_data_format_blank.setVisible(true);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
		} else {
			error_label_data_format_blank.setVisible(false);
			dataFormat.getStyleClass().remove(DynamicCSS.ERROR);
		}

		int baudRateValue = -1;

		try {
			baudRateValue = Integer.parseInt(baudRate.getText());
			error_label_baud_rate_invalid_characters.setVisible(false);
			baudRate.getStyleClass().remove(DynamicCSS.ERROR);

		} catch (NumberFormatException e) {
			error_label_baud_rate_invalid_characters.setVisible(true);
			baudRate.getStyleClass().add(DynamicCSS.ERROR);
			return;
		}

		if (baudRateValue < 0 || baudRateValue > Integer.MAX_VALUE) {
			baudRate.getStyleClass().add(DynamicCSS.ERROR);
		} else {
			baudRate.getStyleClass().remove(DynamicCSS.ERROR);
		}

		if (
				!connectionName.getText().trim().isEmpty()
						&& !baudRate.getText().isEmpty()
						&& value != null
						&& !dataFormat.getText().trim().isEmpty()
						&& baudRateValue > 0
		) {
			Connection connection = new Connection(connectionName.getText(), baudRateValue, connectionOptions.getValue().split(":")[0], ConnectionType.SERIAL);
			ConnectionManager.setCurrentConnection(connection);
			System.out.println("Connection created: " + connection);
			Stage stage = StageManager.getConnectionCreatorStage();
			stage.close();
		}
	}

	@FXML
	public void closeConnectionEditor() {
		System.out.println("Connection not created");
		ConnectionManager.setCurrentConnection(null);
		Stage stage = StageManager.getConnectionCreatorStage();
		stage.close();
	}
}