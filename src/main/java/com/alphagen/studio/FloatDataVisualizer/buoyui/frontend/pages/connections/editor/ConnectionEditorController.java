package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.MeasurementConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.util.DynamicCSS;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ConnectionManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.util.stream.Stream;

public class ConnectionEditorController {

	@FXML public Label error_label_connection_name;
	@FXML public Label error_label_baud_rate;
	@FXML public Label error_label_port;
	@FXML public Label error_label_data_format;
	@FXML public Label error_label_start_flag;
	@FXML public Label error_label_end_flag;
	@FXML TextField connectionName;
	@FXML TextField baudRate;
	@FXML ChoiceBox<String> connectionOptions;
	@FXML TextField dataFormat;
	@FXML TextField startFlagTextField;
	@FXML TextField endFlagTextField;

	@FXML
	public void initialize() {
		resetErrorLabels();

		updatePorts(SerialPort.getCommPorts());
	}

	public void resetErrorLabels() {
		error_label_connection_name.setText("");
		error_label_baud_rate.setText("");
		error_label_port.setText("");
		error_label_data_format.setText("");

		error_label_start_flag.setText("");
		error_label_end_flag.setText("");
	}

	public void updatePorts(SerialPort[] ports) {

		connectionOptions.setValue(null);
		connectionOptions.getItems().removeAll(connectionOptions.getItems());

		switch (PlatformDetector.getOSPLATFORM()) {
			case WIN11:
				connectionOptions.getItems().addAll(
						Stream.of(ports)
								.map(SerialPort::getSystemPortName)
								.toList()
				);
				break;
			case MACOS:
				for (SerialPort port : ports)
					if (port.getSystemPortName().contains("cu.usb"))
						connectionOptions.getItems().add(port.getSystemPortName());
				break;
			case LINUX:
				// todo Linux port stuff
				break;
		}
	}

	// todo: update error labels to have 1 label for each field
	@FXML
	public void confirmConnectionEditor() {

		// todo: remove before release
		startFlagTextField.setText("--start-data");
		endFlagTextField.setText("--end-data");

		boolean isValidConnectionName = isValidConnectionName();
		boolean isValidBaudRate = validBaudRate();
		boolean isValidPort = validPort();

		// must always be before isValidMeasurementData() because isValidTeamName()
		// does null, empty and invalid args checks
		boolean isValidTeamName = isValidTeamName();
		boolean isValidMeasurementData = false;
		if (isValidTeamName)
			isValidMeasurementData = isValidMeasurementData();

		boolean isValidStartFlag = validStartFlag();
		boolean isValidEndFlag = validEndFlag();

		System.out.println();
		if (
				isValidConnectionName && isValidBaudRate && isValidPort
						&& isValidTeamName && isValidMeasurementData
						&& isValidStartFlag && isValidEndFlag
		) {
			ConnectionConfig connection = new ConnectionConfig(
					connectionName.getText(),
					Integer.parseInt(baudRate.getText()),
					SerialPort.getCommPort(connectionOptions.getValue()),
					ConnectionType.SERIAL,
					new FloatConfig(
							dataFormat.getText().trim(),
							startFlagTextField.getText().trim(),
							endFlagTextField.getText().trim()
					),
					getMeasurementConfigs()
			);
			ConnectionManager.setCurrentConnection(connection);
			System.out.println("Connection created: " + connection.connectionName());
			Stage stage = StageManager.getConnectionCreatorStage();
			stage.close();
		}
	}

	private boolean isValidConnectionName() {
		String value = connectionName.getText().trim();
		System.out.println("Connection Name: \"" + value + "\", \"" + connectionName.getText() + "\"");

		if (value.isBlank()) {
			error_label_connection_name.setText(ExceptionMessage.CONNECTION_NAME_EMPTY);
			connectionName.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		for (char c : value.toCharArray()) {
			if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != ' ') {
				error_label_connection_name.setText(ExceptionMessage.CONNECTION_NAME_INVALID_CHARACTERS);
				connectionName.getStyleClass().add(DynamicCSS.ERROR);
				return false;
			}
		}

		File file = new File(FolderConstants.CONNECTIONS.resolve(value + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION).toString());
		if (file.exists()) {
			error_label_connection_name.setText(ExceptionMessage.CONNECTION_NAME_EXISTS);
			connectionName.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		error_label_connection_name.setText("");
		connectionName.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	public boolean validBaudRate() {
		if (baudRate.getText().trim().isBlank()) {
			error_label_baud_rate.setText(ExceptionMessage.CONNECTION_BAUD_RATE_EMPTY);
			baudRate.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		int baudRateValue;
		try {
			baudRateValue = Integer.parseInt(baudRate.getText().trim());
			error_label_baud_rate.setText("");
			baudRate.getStyleClass().remove(DynamicCSS.ERROR);
		} catch (NumberFormatException e) {
			error_label_baud_rate.setText(ExceptionMessage.CONNECTION_BAUD_RATE_INVALID_CHARACTERS);
			baudRate.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		if (baudRateValue < 0) {
			error_label_baud_rate.setText(ExceptionMessage.CONNECTION_BAUD_RATE_INVALID_RANGE);
			baudRate.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		error_label_baud_rate.setText("");
		baudRate.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	private boolean validPort() {

		String value;

		try {
			value = connectionOptions.getValue();
		} catch (NullPointerException _) {
			System.out.println("Connection Option Value has give Null Pointer Exception");
			return false;
		}

		if (value == null || value.isBlank()) {
			error_label_port.setText(ExceptionMessage.CONNECTION_PORT_EMPTY);
			connectionOptions.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		error_label_port.setText("");
		connectionOptions.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	private boolean isValidTeamName() {
		String floatFormat = dataFormat.getText().trim();
		System.out.println("Float Data: \"" + floatFormat + "\", \"" + dataFormat.getText() + "\"");

		// no text
		if (floatFormat.isBlank()) {
			System.err.println(" FD 1 >>> Empty");
			error_label_data_format.setText(ExceptionMessage.FLOAT_DATA_FORMAT_EMPTY_ARGUMENTS);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}
		System.out.println(" FD 1 >>> Not Empty");

		String[] floatFormatParts = floatFormat.split(",");

		// team name/num dne
		if (!floatFormatParts[0].contains("-")) {
			System.err.println(" FD 3 >>> Invalid Team Info");
			error_label_data_format.setText(ExceptionMessage.FLOAT_DATA_FORMAT_TEAM_DATA_ARGUMENTS_NOT_FOUND);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}
		System.out.println(" FD 3 >>> Valid Team Info");

		// team info length = 2 (PN12, MiramarWaterJets)
		String[] teamInfo = floatFormatParts[0].split("-");
		System.out.println(" FD 4 >>> Team Number Length: " + teamInfo[0].length());

		if (teamInfo.length != 2 && (teamInfo[0].isBlank() && teamInfo[1].isBlank())) {
			System.err.println(" FD 5 >>> Invalid Data Length: length < 4");
			error_label_data_format.setText(ExceptionMessage.FLOAT_DATA_FORMAT_TEAM_DATA_ARGUMENTS_MISSING + " or " + ExceptionMessage.FLOAT_DATA_FORMAT_TEAM_DATA_NAME_INVALID);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}
		System.out.println(" FD 5 >>> Valid Team Info");

		// team num wrong (correct length = 4/5) ex: PN12, EX92
		if (teamInfo[0].length() != 4) {
			System.err.println(" FD 6 >>> Invalid Team Number");
			error_label_data_format.setText(ExceptionMessage.FLOAT_DATA_FORMAT_TEAM_DATA_NUM_INVALID_LENGTH);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}
		System.out.println(" FD 6 >>> Valid Team Number");

		// team num wrong (correct length = 4/5) ex: PN12, EX92
		if (teamInfo.length < 2 || teamInfo[1].trim().isBlank()) {
			System.err.println(" FD 6.2 >>> Invalid Team Number");
			error_label_data_format.setText(ExceptionMessage.FLOAT_DATA_FORMAT_TEAM_DATA_NUM_INVALID_LENGTH);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}
		System.out.println(" FD 6.2 >>> Valid Team Number");

		if (floatFormatParts.length < 2 || !floatFormatParts[1].contains("pkt-")) {
			System.err.println(" FD 7 >>> Invalid Packet Format or Enter \"pkt-\"");
			error_label_data_format.setText(ExceptionMessage.FLOAT_DATA_FORMAT_PACKET_ID_NOT_FOUND);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}
		System.out.println(" FD 7 >>> Valid Packet Format");

		error_label_data_format.setText("");
		dataFormat.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	private boolean isValidMeasurementData() {
		String[] floatFormatParts = dataFormat.getText().trim().split(",");

		try {
			// first arg: contains time aka index 2 (0 - team info) (1 - pkt- info)
			if (!floatFormatParts[2].trim().toLowerCase().contains("time")) {
				error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_TIME_NOT_FOUND);
				dataFormat.getStyleClass().add(DynamicCSS.ERROR);
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException _) {
			error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_TIME_NOT_FOUND);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		for (int i = 2; i < floatFormatParts.length; i++) {

			String value = floatFormatParts[i];
			if (!value.contains("(") || !value.contains(")")) {
				error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_MEASUREMENT_UNIT_NOT_FOUND + value + "(unit)");
				dataFormat.getStyleClass().add(DynamicCSS.ERROR);
				return false;
			}

			if (!isValidMeasurementData(value.split("\\("))) {
				error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_TIME_INVALID + ": " + value);
				dataFormat.getStyleClass().add(DynamicCSS.ERROR);
				return false;
			}
		}

		// not enough args
		if (floatFormatParts.length < 4) {
			System.err.println(" FD 2 >>> Invalid Data Length: length < 4");
			error_label_data_format.setText(ExceptionMessage.FLOAT_DATA_FORMAT_MISSING_ARGUMENTS);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}
		System.out.println(" FD 2 >>> Data Length" + floatFormatParts.length);

		error_label_data_format.setText("");
		dataFormat.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	private boolean validStartFlag() {
		if (startFlagTextField.getText().trim().isBlank()) {
			error_label_start_flag.setText(ExceptionMessage.PROCESSOR_DATA_START_DATA_TRANSFER_FLAG_EMPTY);
			startFlagTextField.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		error_label_start_flag.setText("");
		startFlagTextField.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	private boolean validEndFlag() {
		String value = endFlagTextField.getText().trim();
		if (value.isBlank()) {
			error_label_end_flag.setText(ExceptionMessage.PROCESSOR_DATA_END_DATA_TRANSFER_FLAG_EMPTY);
			endFlagTextField.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}


		if (value.equals(startFlagTextField.getText().trim())) {
			error_label_end_flag.setText(ExceptionMessage.PROCESSOR_DATA_TRANSFER_FLAG_SAME);
			endFlagTextField.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}


		error_label_end_flag.setText("");
		endFlagTextField.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
	}

	private MeasurementConfig[] getMeasurementConfigs() {
		String[] floatData = dataFormat.getText().trim().split(",");
//		System.out.print("Connection: " + connectionName.getText().trim());
		System.out.println("Connection Measurement Count: " + (floatData.length - 2));
		MeasurementConfig[] measurementConfigs = new MeasurementConfig[floatData.length - 2];
		for (int i = 2; i < floatData.length; i++) {
			String measurement = floatData[i].trim();
//			System.out.print("Complete Measurement: " + measurement);
			String measurementName = measurement.substring(0, measurement.indexOf("("));
			String measurementUnit = measurement.substring(measurement.indexOf("(")).replace("(", "").replace(")", "");
			measurementConfigs[i - 2] = new MeasurementConfig(measurementName, measurementUnit);
//			System.out.printf(" > Measurement Breakdown: %-20s %s\n", measurementName, measurementUnit);
		}
		System.out.println();
		return measurementConfigs;
	}

	private boolean isValidMeasurementData(String[] measurementData) {
//		String[] measureData = null;
//		try {
//			measurementData[2].split("\\(");
//		} catch (ArrayIndexOutOfBoundsException _) {
//			error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_TIME_INVALID);
//			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
//			return false;
//		}

		if (measurementData.length != 2) {
			error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_TIME_INVALID);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		// measure name is empty
		if (measurementData[0] == null || measurementData[0].isBlank()) {
			error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_MEASUREMENT_NAME_NOT_FOUND);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		// measure unit is empty
		// 		1. m) is min length (2)
		//		2. ) is empty length (1)
		if (measurementData[1].length() < 2) {
			error_label_data_format.setText(ExceptionMessage.MEASUREMENT_DATA_FORMAT_MEASUREMENT_UNIT_NOT_FOUND);
			dataFormat.getStyleClass().add(DynamicCSS.ERROR);
			return false;
		}

		error_label_data_format.setText("");
		dataFormat.getStyleClass().remove(DynamicCSS.ERROR);
		return true;
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

		// todo: for the float data format
		switch (PlatformDetector.getOSPLATFORM()) {
			case WIN11:
				connectionOptions.setValue(SerialPort.getCommPorts()[0].getSystemPortName());
				break;
			case MACOS:
				SerialPort[] ports = SerialPort.getCommPorts();
				for (SerialPort port : ports) {
					if (port.getSystemPortName().contains("cu.usb")) {
						connectionOptions.setValue(port.getSystemPortName());
						break;
					}
				}
				break;
			case LINUX:
				// todo: use ubuntu to figure out the things
				break;
		}

		ConnectionsController cc = ControllerManager.getConnectionsController();

		if (cc.getCurrentConnectionConfig() != null) {
			ConnectionConfig ccg = cc.getCurrentConnectionConfig();
			connectionName.setText(ccg.connectionName());
			baudRate.setText(Integer.toString(ccg.baudRate()));
			connectionOptions.setValue(ccg.port().getSystemPortName());
		}

		if (cc.getCurrentFloatConfig() != null) {
			FloatConfig fcg = cc.getCurrentFloatConfig();
			dataFormat.setText(fcg.teamData());
			startFlagTextField.setText(fcg.startFlag());
			endFlagTextField.setText(fcg.endFlag());
		}
	}
}