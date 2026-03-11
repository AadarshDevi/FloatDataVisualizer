package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ConnectionEditorController {


	@FXML public Label error_label_connection_name_blank;
	@FXML public Label error_label_name_invalid_characters;
	@FXML public Label error_label_baud_rate_invalid_characters;
	@FXML public Label error_label_baud_rate_blank;
	@FXML public Label error_label_port_blank;
	@FXML public Label error_label_data_format_blank;
	@FXML public Label error_label_start_flag_blank;
	@FXML public Label error_label_end_flag_blank;
	@FXML public Label error_label_measure_name_blank;
	@FXML public Label error_label_measure_unit_blank;
	@FXML public Label error_label_measure_unit_invalid_characters;
	@FXML TextField connectionName;
	@FXML TextField baudRate;
	@FXML ChoiceBox<String> connectionOptions;
	@FXML TextField dataFormat;
	@FXML TextField startFlagTextField;
	@FXML TextField endFlagTextField;
	@FXML TextField measurementName;
	@FXML TextField measurementUnit;

    public void populateFloatConfig(FloatConfig floatConfig) {
        if (floatConfig != null) {
            // todo: populate
        }
    }

    @FXML
    public void initialize() {
        resetErrorLabels();

        switch (PlatformDetector.getOSPLATFORM()) {
            case WIN11:
                connectionOptions.getItems().addAll(
                        Stream.of(SerialPort.getCommPorts())
                                .map(SerialPort::getSystemPortName)
                                .toList()
                );
                break;
            case MACOS:
                ArrayList<SerialPort> portsList = new ArrayList<>();
                SerialPort[] ports = SerialPort.getCommPorts();
                for (SerialPort port : ports)
                    if (port.getSystemPortName().contains("cu.usb"))
                        portsList.add(port);

                connectionOptions.getItems().addAll(String.valueOf(portsList));
                break;
            case LINUX:
                break;
        }
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


//		error_label_connection_name_blank.setManaged(false);
//		error_label_baud_rate_invalid_characters.setManaged(false);
//		error_label_baud_rate_blank.setManaged(false);
//		error_label_port_blank.setManaged(false);
//		error_label_data_format_blank.setManaged(false);
//
//		error_label_name_invalid_characters.setManaged(false);
//		error_label_start_flag_blank.setManaged(false);
//		error_label_end_flag_blank.setManaged(false);
//		error_label_measure_name_blank.setManaged(false);
//		error_label_measure_unit_blank.setManaged(false);
//		error_label_measure_unit_invalid_characters.setManaged(false);
    }

    @FXML
    public void confirmConnectionEditor() {

        boolean isValidConnectionName = isValidConnectionName();
        boolean isValidBaudRate = validBaudRate();
        boolean isValidPort = validPort();
//		System.out.println("Connection Config: " + isValidConnectionName + " " + isValidBaudRate + " " + isValidPort);

        boolean isValidTeamName = isValidTeamName();
        boolean isValidStartFlag = validStartFlag();
        boolean isValidEndFlag = validEndFlag();
//		System.out.println("Float Config: " + isValidTeamName + " " + isValidStartFlag + " " + isValidEndFlag);

        boolean isValidMeasurementName = validMeasurementName();
        boolean isValidMeasurementUnit = validMeasurementUnit();
//		System.out.println("Unit Config: " + isValidMeasurementName + " " + isValidMeasurementUnit);

		System.out.println();
		if (
				isValidConnectionName
						&& isValidBaudRate
						&& isValidPort
						&& isValidTeamName
						&& isValidStartFlag
						&& isValidEndFlag
						&& isValidMeasurementName
						&& isValidMeasurementUnit
		) {
			// get serial port from name
			// connectionOptions.getValue()
			ConnectionConfig connection = new ConnectionConfig(
					connectionName.getText(),
					Integer.parseInt(baudRate.getText()),
					SerialPort.getCommPorts()[0], // fixme: dynamic
					ConnectionType.SERIAL,
					new FloatConfig(
							dataFormat.getText().trim(),
							startFlagTextField.getText().trim(),
							endFlagTextField.getText().trim(),
							measurementName.getText().trim(),
							measurementUnit.getText().trim()
					)
			);
			ConnectionManager.setCurrentConnection(connection);
			System.out.println("Connection created: " + connection);
			Stage stage = StageManager.getConnectionCreatorStage();
			stage.close();
		}
	}

    private boolean validMeasurementUnit() {
        if (measurementUnit.getText().trim().isEmpty()) {
            error_label_measure_unit_blank.setVisible(true);
            measurementUnit.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        } else {
            error_label_measure_unit_blank.setVisible(false);
            measurementUnit.getStyleClass().remove(DynamicCSS.ERROR);
            return true;
        }
    }

    private boolean validMeasurementName() {
        if (measurementName.getText().trim().isEmpty()) {
            error_label_measure_name_blank.setVisible(true);
            measurementName.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        } else {
            error_label_measure_name_blank.setVisible(false);
            measurementName.getStyleClass().remove(DynamicCSS.ERROR);
            return true;
        }
    }

    private boolean validEndFlag() {
        if (endFlagTextField.getText().trim().isEmpty()) {
            error_label_end_flag_blank.setVisible(true);
//			error_label_end_flag_blank.setManaged(true);
            endFlagTextField.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        } else {
            error_label_end_flag_blank.setVisible(false);
//			error_label_end_flag_blank.setManaged(false);
            endFlagTextField.getStyleClass().remove(DynamicCSS.ERROR);
            return true;
        }

    }

    private boolean validStartFlag() {
        if (startFlagTextField.getText().trim().isEmpty()) {
            error_label_start_flag_blank.setVisible(true);
//			error_label_start_flag_blank.setManaged(true);
            startFlagTextField.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        } else {
            error_label_start_flag_blank.setVisible(false);
//			error_label_start_flag_blank.setManaged(false);
            startFlagTextField.getStyleClass().remove(DynamicCSS.ERROR);
            return true;
        }
    }

    private boolean validPort() {

        String value;

        try {
            value = connectionOptions.getValue();
        } catch (NullPointerException _) {
            System.out.println("Connection Option Value has give Null Pointer Exception");
            return false;
        }

        if (value == null || value.isEmpty()) {
            error_label_port_blank.setVisible(true);
//			error_label_port_blank.setManaged(true);
            connectionOptions.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        } else {
            error_label_port_blank.setVisible(false);
//			error_label_port_blank.setManaged(false);
            connectionOptions.getStyleClass().remove(DynamicCSS.ERROR);
            return true;
        }

    }

    private boolean isValidConnectionName() {
        String value = connectionName.getText().trim();
        if (value.isEmpty()) {
            error_label_connection_name_blank.setVisible(true);
//			error_label_connection_name_blank.setManaged(true);
            connectionName.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        } else {
            error_label_connection_name_blank.setVisible(false);
//			error_label_connection_name_blank.setManaged(true);
            connectionName.getStyleClass().remove(DynamicCSS.ERROR);
        }

        for (char c : value.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != ' ') {
                error_label_name_invalid_characters.setVisible(true);
//				error_label_name_invalid_characters.setManaged(true);
                connectionName.getStyleClass().add(DynamicCSS.ERROR);
                return false;
            }
        }

        error_label_name_invalid_characters.setVisible(false);
//		error_label_name_invalid_characters.setManaged(false);
        connectionName.getStyleClass().remove(DynamicCSS.ERROR);
        return true;
    }

    private boolean isValidTeamName() {
        if (dataFormat.getText().trim().isEmpty()) {
            error_label_data_format_blank.setVisible(true);
//			error_label_data_format_blank.setManaged(true);
            dataFormat.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        }

        error_label_data_format_blank.setVisible(false);
//			error_label_data_format_blank.setManaged(false);
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

    public boolean validBaudRate() {
        if (baudRate.getText().trim().isEmpty()) {
            error_label_baud_rate_blank.setVisible(true);
//			error_label_baud_rate_blank.setManaged(true);
            baudRate.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        }

        error_label_baud_rate_blank.setVisible(false);
//		error_label_baud_rate_blank.setManaged(false);
        baudRate.getStyleClass().remove(DynamicCSS.ERROR);

        int baudRateValue = -1;
        try {
            baudRateValue = Integer.parseInt(baudRate.getText());
            error_label_baud_rate_invalid_characters.setVisible(false);
//			error_label_baud_rate_invalid_characters.setManaged(false);
            baudRate.getStyleClass().remove(DynamicCSS.ERROR);
        } catch (NumberFormatException e) {
            error_label_baud_rate_invalid_characters.setVisible(true);
//			error_label_baud_rate_invalid_characters.setManaged(true);
            baudRate.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        }

        if (baudRateValue < 0) {
            baudRate.getStyleClass().add(DynamicCSS.ERROR);
            return false;
        }

        baudRate.getStyleClass().remove(DynamicCSS.ERROR);
        return true;
    }
}