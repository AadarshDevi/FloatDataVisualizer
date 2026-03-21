package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard;

import com.alphagen.studio.FloatDataVisualizer.buoyui.Controller;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.grapher.GrapherController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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

	@FXML
	public void handleContextMenu(MouseEvent event) {
		if (!isDisabled && event.getButton() == MouseButton.SECONDARY)
			connectionOptions.show(dataCard, event.getSceneX(), event.getSceneY());
		else if (isDisabled) event.consume();
	}

	public void deleteConnection() {
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

		BorderPane measurementViewer = null;
		try {
			measurementViewer = measurementViewerLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		MeasurementViewerController mvc = measurementViewerLoader.getController();
		mvc.setData(connectionConfig);

		Stage stage = StageUtil.getConnectionEditor(measurementViewer);
		StageManager.setConnectionCreatorStage(stage);
		stage.showAndWait();
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

		System.out.println("Serial Graph");

		Stage main = StageManager.getMainStage();

		FXMLLoader grapherLoader = new FXMLLoader(PageConstants.GRAPHING_PAGE);
		BorderPane grapherUI = null;
		try {
			grapherUI = grapherLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		GrapherController gc = grapherLoader.getController();

		Scene scene = new Scene(grapherUI);
		scene.setRoot(grapherUI);
		StageManager.createInvisPane(scene, grapherUI);
		main.setScene(scene);
		main.show();
	}
}