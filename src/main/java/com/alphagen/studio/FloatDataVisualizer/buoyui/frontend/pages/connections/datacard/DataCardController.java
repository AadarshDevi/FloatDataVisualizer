package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard;

import com.alphagen.studio.FloatDataVisualizer.buoyui.Controller;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.ConnectionProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.Connections;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor.ConnectionEditorController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.GrapherController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class DataCardController extends Controller {
	@FXML public Button dataCard;
	@FXML public Label baudRate;
	@FXML public Label port;
	@FXML public Label name;
	@FXML public Button serial;
	@FXML public Button wireless;
	@FXML public ContextMenu connectionOptions;
	@Getter private ConnectionConfig connectionConfig;
	@FXML private Tooltip tool_tip_connection_name;

	@Getter private boolean isDisabled;

	@Setter
	@Getter
	private boolean isWorking;

	@FXML
	public void initialize() {
		if (dataCard.getContextMenu() == null) {
			System.out.println("No ContextMenu Present");
			dataCard.setContextMenu(connectionOptions);
		}
		isWorking = true;
		isDisabled = false;
	}

	public void deleteConnection() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Connections");
		alert.setHeaderText(null);
		alert.setContentText("Do you want to delete the connection?");
		alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent() && result.get() == ButtonType.CANCEL) {
			return;
		}

		System.out.println("Deleting Connection");

		Path connectionFile = FolderConstants.CONNECTIONS.resolve(connectionConfig.connectionName() + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION);
		try {
			Files.delete(connectionFile);
		} catch (IOException e) {
			System.err.println("Unable to delete connection: " + connectionConfig.connectionName());
			System.err.println(e.getMessage());
			return;
		}

		ControllerManager.getConnectionsController().deleteConnection(dataCard);
		System.out.println("Deleted Connection: " + connectionConfig.connectionName());

		alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connection Deleted");
		alert.setHeaderText(null);
		alert.setContentText("Connection was successfully deleted");
	}

	public void setConnection(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
		ControllerManager.getConnectionsController().setCurrentConnectionConfig(this.connectionConfig);
		setBaudRate(connectionConfig.baudRate());
		setConnectionName(connectionConfig.connectionName());
		setPort(connectionConfig.port());
		setConnectionType(connectionConfig.portType());
		invalidConnection();

		tool_tip_connection_name.setText(connectionConfig.connectionName());
	}

	private void setBaudRate(int baudRateNum) {
		baudRate.setText(Integer.toString(baudRateNum));
	}

	private void setConnectionName(String connectionName) {
		name.setText(connectionName);
	}

	private void setPort(SerialPort portName) {
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

	public void invalidConnection() {
		if (connectionConfig == null || !connectionConfig.port().openPort()) {
			isWorking = false;
			isDisabled = true;
			dataCard.setOpacity(0.4);
		} else {
			isWorking = true;
			isDisabled = false;
			connectionConfig.port().closePort();
			dataCard.setOpacity(1);
		}
	}

	@FXML
	public void viewMeasurements() {
		FXMLLoader measurementViewerLoader = new FXMLLoader(DataCardController.class.getResource("MeasurementsViewer_v1.fxml"));

		BorderPane measurementViewer;
		try {
			measurementViewer = measurementViewerLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		MeasurementViewerController mvc = measurementViewerLoader.getController();
		mvc.setData(connectionConfig);

		{
			Scene scene = new Scene(measurementViewer);
			Stage stage = new Stage();
			stage.setScene(scene);
			scene.setFill(Color.TRANSPARENT);

			StageUtil.customTitleBarDrag(stage, scene, measurementViewer);
			StageUtil.setStageInit(stage);

			stage.initOwner(StageManager.getMainStage());
			StageManager.setConnectionCreatorStage(stage);

			stage.showAndWait();
		}
	}

	public void disable() {
		isDisabled = true;
		dataCard.setOpacity(0.4);
	}

	public void enable() {
		isDisabled = false;
		dataCard.setOpacity(1);
	}

	public void serialGraph() {

		if (isDisabled && !isWorking) {
			System.err.println("Connection Disabled: " + connectionConfig.connectionName());
			return;
		}
		System.out.println(" >>> Connection Data Card > " + connectionConfig);
		Connections.setCurrentConnection(this.connectionConfig);
		System.out.println("\nSerial Graph");
		Stage stage = StageManager.getMainStage();
		GrapherController gc = StageManager.setGraphingScene();
		ControllerManager.setGrapherController(gc);
//		gc.setup();
		stage.setScene(StageManager.getGraphingScene());
	}

	@FXML
	public void renameConnection() {

		// get validConnectionName method to use and verify
		ConnectionEditorController cec;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_EDITOR_PAGE);
			fxmlLoader.load();
			cec = fxmlLoader.getController();
			ControllerManager.setConnectionEditorController(cec);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String name = null;
		// get new connection name
		boolean success = false;
		do {

			// load custom
			TextInputDialog textInputDialog = new TextInputDialog(connectionConfig.connectionName());
			textInputDialog.setTitle("Rename Connection");
			textInputDialog.setHeaderText(null);
			textInputDialog.setContentText("Enter new connection name");
			textInputDialog.setWidth(500.0);
			textInputDialog.setHeight(175.0);
			Optional<String> objName = textInputDialog.showAndWait();

			if (objName.isEmpty()) {
				System.err.println("Obj is empty");
				break;
			}

			name = objName.get();
			System.out.println(" >>> New Connection Name > " + name);

			if (!cec.isValidConnectionName(name)) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Connection Exception");
				alert.setHeaderText(null);
				alert.setContentText("Connection with the name given already exists. Please change the name or cancel renaming.");
				alert.showAndWait();
			} else {
				success = true;
			}
		} while (!success);

		if (name == null || name.isBlank()) return;

		success = ConnectionProcessor.renameConnection(connectionConfig, new ConnectionConfig(
			name,
			connectionConfig.baudRate(),
			connectionConfig.port(),
			connectionConfig.portType(),
			connectionConfig.floatConfig(),
			connectionConfig.measurementConfigs()
		));

		if (!success) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Connection Exception");
			alert.setHeaderText(null);
			alert.setContentText("Unable to rename connection");
			alert.showAndWait();
		}

		ConnectionsController cc = ControllerManager.getConnectionsController();
		cc.repopulateConnections();
	}

	@FXML
	public void editConnection() {

	}
}

// todo: menus for rename and edit