package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.util.DynamicCSS;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ConnectionManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.stream.Stream;

public class ConnectionEditorController {

	// error groups

	// name
	@FXML TextField connectionName;
	@FXML public Label error_label_connection_name_blank;
	@FXML public Label error_label_name_invalid_characters;
	private boolean validConnectionName = true;

	// baud rate
	@FXML TextField baudRate;
	@FXML public Label error_label_baud_rate_invalid_characters;
	@FXML public Label error_label_baud_rate_blank;

	// port
	@FXML ChoiceBox<String> connectionOptions;
	@FXML public Label error_label_port_blank;

	// data format
	@FXML TextField dataFormat;
	@FXML public Label error_label_data_format_blank;
	private boolean validTeamName = true;

	// flags
	@FXML TextField startFlagTextField;
	@FXML public Label error_label_start_flag_blank;
	@FXML TextField endFlagTextField;
	@FXML public Label error_label_end_flag_blank;

	// measure
	@FXML TextField measurementName;
	@FXML public Label error_label_measure_name_blank;
	@FXML TextField measurementUnit;
	@FXML public Label error_label_measure_unit_blank;
	@FXML public Label error_label_measure_unit_invalid_characters;

	// operation buttons
	@FXML public Button confirmButton;
	@FXML private Button closeButton;

	public void populateFloatConfig(FloatConfig floatConfig) {
		if (floatConfig != null) {
			// todo: populate
		}
	}

	@FXML
	public void initialize() {
		resetErrorLabels();

		connectionOptions.getItems().addAll(
				Stream.of(SerialPort.getCommPorts())
						.map(SerialPort::getDescriptivePortName)
						.toList()
		);
	}

	public void resetErrorLabels() {
		error_label_connection_name_blank.setVisible(false);
		error_label_baud_rate_invalid_characters.setVisible(false);
		error_label_baud_rate_blank.setVisible(false);
		error_label_port_blank.setVisible(false);
		error_label_data_format_blank.setVisible(false);

		error_label_name_invalid_characters.setVisible(false);
		error_label_start_flag_blank.setVisible(false);
		error_label_end_flag_blank.setVisible(false);
		error_label_measure_name_blank.setVisible(false);
		error_label_measure_unit_blank.setVisible(false);
		error_label_measure_unit_invalid_characters.setVisible(false);
	}

	@FXML
	public void confirmConnectionEditor() {

		if (!isValidConnectionName())
			validConnectionName = false;

		if (baudRate.getText().trim().isEmpty()) {
			error_label_baud_rate_blank.setVisible(true);
			baudRate.getStyleClass().add(DynamicCSS.ERROR);
		} else {
			error_label_baud_rate_blank.setVisible(false);
			baudRate.getStyleClass().remove(DynamicCSS.ERROR);
		}

		String value = null;

		try {
			value = connectionOptions.getValue();
		} catch (NullPointerException _) {
			System.out.println("Connection Option Value has give Null Pointer Exception");
		}

		if (value == null || value.isEmpty()) {
			error_label_port_blank.setVisible(true);
			connectionOptions.getStyleClass().add(DynamicCSS.ERROR);
		} else {
			error_label_port_blank.setVisible(false);
			connectionOptions.getStyleClass().remove(DynamicCSS.ERROR);
		}

		if (!isValidTeamName())
			validTeamName = false;

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
				validConnectionName
						&& !baudRate.getText().isEmpty()
						&& value != null
						&& validTeamName
						&& baudRateValue > 0
		) {
			// get serial port from name
			// connectionOptions.getValue()
			ConnectionConfig connection = new ConnectionConfig(
					connectionName.getText(),
					baudRateValue,
					SerialPort.getCommPorts()[0], // fixme: dynamic
					ConnectionType.SERIAL,
					new FloatConfig(null, null, null, null, null)
			);
			ConnectionManager.setCurrentConnection(connection);
			System.out.println("Connection created: " + connection);
			Stage stage = StageManager.getConnectionCreatorStage();
			stage.close();
		}
	}

	private boolean isValidConnectionName() {
		String value = connectionName.getText().trim();
		if (value.isEmpty()) {
			error_label_connection_name_blank.setVisible(true);
			connectionName.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		} else {
			error_label_connection_name_blank.setVisible(false);
			connectionName.getStyleClass().remove(DynamicCSS.ERROR);
		}

		// fixme: use correct
		for (char c : value.toCharArray()) {
			if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != ' ') {
				error_label_name_invalid_characters.setVisible(true);
				connectionName.getStyleClass().add(DynamicCSS.ERROR);
				return false;
			}
		}

		error_label_name_invalid_characters.setVisible(false);
		connectionName.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	private boolean isValidTeamName() {
		if (dataFormat.getText().trim().isEmpty()) {
			error_label_data_format_blank.setVisible(true);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		} else {
			error_label_data_format_blank.setVisible(false);
			dataFormat.getStyleClass().remove(DynamicCSS.ERROR);
			return true;
		}
	}

	@FXML
	public void closeConnectionEditor() {
		System.out.println("Connection not created");
		ConnectionManager.setCurrentConnection(null);
		Stage stage = StageManager.getConnectionCreatorStage();
		stage.close();
	}

	@FXML
	public void autoFill() {

		connectionName.setText("test1");
		baudRate.setText("1000");
		connectionOptions.setValue(SerialPort.getCommPorts()[0].getDescriptivePortName());
		dataFormat.setText("PN12-MiramarWaterJets");
		startFlagTextField.setText("--start-data-transfer");
		endFlagTextField.setText("--start-data-transfer");
		measurementName.setText("depth");
		measurementUnit.setText("m");

		ConnectionsController cc = ControllerManager.getConnectionsController();
		if (cc.getCurrentConnectionConfig() != null) {
			ConnectionConfig ccg = cc.getCurrentConnectionConfig();
			connectionName.setText(ccg.connectionName());
			baudRate.setText(Integer.toString(ccg.baudRate()));
			connectionOptions.setValue(ccg.port().getDescriptivePortName());
		}

		if (cc.getCurrentFloatConfig() != null) {
			FloatConfig fcg = cc.getCurrentFloatConfig();
			dataFormat.setText(fcg.teamData());
			startFlagTextField.setText(fcg.startFlag());
			endFlagTextField.setText(fcg.endFlag());
			measurementName.setText(fcg.measurement());
			measurementUnit.setText(fcg.measurementUnit());
		}


	}
}