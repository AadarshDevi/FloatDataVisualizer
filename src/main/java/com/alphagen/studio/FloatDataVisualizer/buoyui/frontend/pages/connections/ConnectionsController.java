package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.ConnectionProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ConnectionManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.DataCardManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard.DataCardController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor.ConnectionEditorController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionsController {

	@Getter private final ExecutorService serialPortWatcher = Executors.newSingleThreadExecutor();
	@FXML public TilePane connections;
	@FXML public Button refresh_connections;
	@FXML public Button export_all_connections;
	@FXML public Button import_connection_s;
	@Setter
	@Getter
	private ConnectionConfig currentConnectionConfig;
	@Setter
	@Getter
	private FloatConfig currentFloatConfig;

	@FXML
	public void initialize() {
		refresh_connections.setVisible(false);
		refresh_connections.setManaged(false);
		export_all_connections.setVisible(false);
		export_all_connections.setManaged(false);
		import_connection_s.setVisible(false);
		import_connection_s.setManaged(false);
		startHardwareWatcher();
	}

	public void startHardwareWatcher() {
		serialPortWatcher.submit(() -> {
			int lastCount = -1;
			while (!Thread.currentThread().isInterrupted()) {
				int currentCount = SerialPort.getCommPorts().length;

				if (currentCount != lastCount) {
					lastCount = currentCount;

					// Update the UI safely
					Platform.runLater(() -> {
						// You could also refresh a ComboBox here
						System.out.println(" >>> Connection Refresher: Refreshed Connections");
						refreshConnections();

						if (ControllerManager.getConnectionEditorController() != null) {
							ControllerManager.getConnectionEditorController().updatePorts(SerialPort.getCommPorts());
						}

					});
				}

				try {
					// Don't spam the OS; check every 1-2 seconds
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					break;
				}
			}
		});
	}

	@FXML
	public void refreshConnections() {
		System.out.println("\nRefreshing Connections");
//		System.out.println("Enabled\t  Working");
		ObservableList<Node> dataCards = connections.getChildren();
		for (Node node : dataCards) {
			Button dataCard = (Button) node;
			DataCardController dcc = (DataCardController) dataCard.getProperties().get("dcc");
			dcc.invalidConnection();
//			System.out.println(!dcc.isDisabled() + "\t  " + dcc.isWorking());
		}
	}

//	@FXML
//	public void test() {
//		System.out.println("Created Connection");
//	}

	@FXML
	public void createConnection() {

		System.out.println("Opening ConnectionEditorUI");
		BorderPane connectionCreatorPane;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_EDITOR_PAGE);
			connectionCreatorPane = fxmlLoader.load();
			ConnectionEditorController cec = fxmlLoader.getController();
			ControllerManager.setConnectionEditorController(cec);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		{
			Scene scene = new Scene(connectionCreatorPane);
			Stage stage = new Stage();
			stage.setScene(scene);

			StageUtil.customTitleBarDrag(stage, scene, connectionCreatorPane);
			stage.initOwner(StageManager.getMainStage());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.TRANSPARENT);

			StageManager.setConnectionCreatorStage(stage);
			stage.showAndWait();
		}

		if (ConnectionManager.getCurrentConnection() == null) {
			return;
		}
		System.out.println("Creating Connection");

		Button dataCard = DataCardManager.createDataCard(ConnectionManager.getCurrentConnection());
		connections.getChildren().add(dataCard);

		Path filePath = FolderConstants.CONNECTIONS.resolve(currentConnectionConfig.connectionName() + FolderConstants.FLOAT_CONNECTION_FILE_EXTENSION);
		boolean success = ConnectionProcessor.writeConnection(filePath, currentConnectionConfig);
		if (!success) {
			System.err.println("Connection Writing Failed");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Connection Exception");
			alert.setHeaderText(null);
			alert.setContentText("Unable to create Connection");
			alert.showAndWait();
			System.exit(-1);
		}

		ConnectionProcessor.readAllConnections();
		ControllerManager.setConnectionEditorController(null);
	}

	public void deleteConnection(Button deleteButton) {
		connections.getChildren().remove(deleteButton);
	}

	@FXML
	public void quitApp() {
		serialPortWatcher.shutdownNow();
		System.out.println("App Quit");
		Platform.exit();
		System.exit(0);
	}

	@FXML
	public void deleteAll() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete All Connections");
		alert.setContentText("Do you want to delete all connections?");
		alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent() && result.get() == ButtonType.CANCEL) {
			return;
		}

		System.out.println("Deleting All Connections");
		connections.getChildren().removeAll(connections.getChildren());

		File connFolder = new File(FolderConstants.CONNECTIONS.toString());
		File[] files = connFolder.listFiles();
		System.out.println("Deleted All Connections: " + files.length);

		for (File file : files) {
			file.delete();
		}

		alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Delete All Connections");
		alert.setContentText("All connections have been deleted");

	}

	public void setConnectionConfigs(ArrayList<ConnectionConfig> connectionsList) {
		System.out.println("\nAdding ConnectionConfigs");
//		System.out.println("Enabled\t  Working");
		int count = connectionsList.size();
		for (ConnectionConfig connectionConfig : connectionsList) {

			Button dataCard = DataCardManager.createDataCard(connectionConfig);
			DataCardController dcc = (DataCardController) dataCard.getProperties().get("dcc");

			dcc.invalidConnection();
//			System.out.println(!dcc.isDisabled() + "\t  " + dcc.isWorking());

			connections.getChildren().add(dataCard);
		}
		System.out.println("Added Connections: " + count);
	}

	// fixme make sure this works later on :(
	@FXML
	public void exportAllConnections() {
//		DirectoryChooser dc = new DirectoryChooser();
//		dc.setTitle("Export All Connections");
//		File folder = dc.showDialog(StageManager.getMainStage());
//		System.out.println(folder.getAbsolutePath());
//		Path exportRoot = Paths.get(folder.getAbsolutePath()).resolve("FloatDataVisualizer " + AppData.RELEASE_VERSION + " Connections");
//		System.out.println("Export Path: " + exportRoot);
//		try {
//			Files.createDirectories(exportRoot);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//		File[] files = FolderConstants.CONNECTIONS.toFile().listFiles();
//		for (File file : files) {
//			try (
//					PrintWriter newFile = new PrintWriter(file);
//					BufferedReader oldFile = new BufferedReader(new FileReader(file))
//			) {
////				if (file.exists()) {
////					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
////					alert.setTitle("Export Connection");
////					alert.setHeaderText(null);
////					alert.setContentText("File Exists. Do you want to overwrite it?");
////					Optional<ButtonType> buttonType = alert.showAndWait();
////					if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
////						Files.delete(exportRoot.resolve(file.getName()));
////						Files.copy(file.toPath(), exportRoot.resolve(file.getName()));
////                    }
////                } else {
////					Files.copy(file.toPath(), exportRoot.resolve(file.getName()));
////                }
////
//			} catch (IOException e) {
////				System.err.println("Unable to copy file " + file.getAbsolutePath());
////				e.printStackTrace();
//			}
//		}
	}

	// fixme make sure this works later on :(
	@FXML
	public void importConnection() {

	}

	public void repopulateConnections() {
		connections.getChildren().removeAll(connections.getChildren());
		ConnectionProcessor.readAllConnections();
	}
}