package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.AppData;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.constants.FolderConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor.ConnectionProcessor;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ConnectionManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.DataCardManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.datacard.DataCardController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor.ConnectionEditorController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import com.alphagen.studio.FloatDataVisualizer.buoyui.lib.fxml.NodePackage;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConnectionsController {

	@FXML public TilePane connections;

	@Setter
	@Getter
	private ConnectionConfig currentConnectionConfig;
	@Setter
	@Getter
	private FloatConfig currentFloatConfig;

	@FXML
	public void initialize() {}

	@FXML
	public void test() {
		System.out.println("Created Connection");
	}

	@FXML
	public void createConnection() {

		System.out.println("Opening ConnectionEditorUI");
		BorderPane connectionCreatorPane = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_EDITOR_PAGE);
			connectionCreatorPane = fxmlLoader.load();
			ConnectionEditorController cec = fxmlLoader.getController();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Stage editor = StageUtil.getConnectionEditor(connectionCreatorPane);
		editor.showAndWait();

		if (ConnectionManager.getCurrentConnection() == null) {
			return;
		}
		System.out.println("Creating Connection");

		// better datacard manager/processor
		Button dataCard = DataCardManager.createDataCard(ConnectionManager.getCurrentConnection());

		connections.getChildren().add(dataCard);
//		connections.getChildren().add(0, dcnp.node());

		boolean success = ConnectionProcessor.writeConnection(currentConnectionConfig);
		if (!success) {
			// fixme replace with error alert
			System.err.println("Connection Writing Failed");
			System.exit(-1);
		}

		ConnectionProcessor.readAllConnections();
	}

	public void deleteConnection(Button deleteButton) {
		connections.getChildren().remove(deleteButton);
	}

	@FXML
	public void quitApp() {
		System.out.println("App Quit");
		Platform.exit();
	}

	@FXML
	public void deleteAll() {
		// todo add conformation alert before delete all
		System.out.println("Deleting All Connections");
		connections.getChildren().removeAll(connections.getChildren());

		File connFolder = new File(FolderConstants.CONNECTIONS.toString());
		File[] files = connFolder.listFiles();
		System.out.println("Deleted All Connections: " + files.length);

		for (File file : files) {
			file.delete();
		}


	}

	public void setConnectionConfigs(ArrayList<ConnectionConfig> connectionsList) {
		System.out.println("Adding ConnectionConfigs");
		int count = connectionsList.size();
		for (ConnectionConfig connectionConfig : connectionsList) {

			NodePackage<DataCardController> np = DataCardManager.create(connectionConfig);
			Button dataCard = (Button) np.node();
			DataCardController dcc = np.controller();
			Button dataCard = DataCardManager.createDataCard(connectionConfig);
			DataCardController dcc = (DataCardController) dataCard.getProperties().get("dcc");
			connections.getChildren().add(dataCard);

			SerialPort sp = SerialPort.getCommPort(dcc.getConnectionConfig().port().getSystemPortName());
			if (!sp.openPort()) {
//				System.out.println("Unable to open Port ");
				dcc.disable();
			}
		}
		System.out.println("Added Connections: " + count);
	}

	@FXML
	public void refreshConnections() {
		ObservableList<Node> dataCards = connections.getChildren();
		for (Node node : dataCards) {
			Button dataCard = (Button) node;
			DataCardController dcc = (DataCardController) dataCard.getProperties().get("dcc");

			SerialPort sp = SerialPort.getCommPort(dcc.getConnectionConfig().port().getSystemPortName());
			if (!sp.openPort()) {
				dcc.disable();
			} else {
				dcc.enable();
				sp.closePort();
			}
		}
	}

	@FXML
	public void exportAllConnections() {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("Export All Connections");
		File folder = dc.showDialog(StageManager.getMainStage());
		System.out.println(folder.getAbsolutePath());
		Path exportRoot = Paths.get(folder.getAbsolutePath()).resolve("FloatDataVisualizer " + AppData.RELEASE_VERSION + " Connections");
		System.out.println("Export Path: " + exportRoot);
		try {
			Files.createDirectories(exportRoot);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		File[] files = FolderConstants.CONNECTIONS.toFile().listFiles();
		for (File file : files) {
			try (
					PrintWriter newFile = new PrintWriter(file);
					BufferedReader oldFile = new BufferedReader(new FileReader(file))
			) {
//				if (file.exists()) {
//					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//					alert.setTitle("Export Connection");
//					alert.setHeaderText(null);
//					alert.setContentText("File Exists. Do you want to overwrite it?");
//					Optional<ButtonType> buttonType = alert.showAndWait();
//					if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
//						Files.delete(exportRoot.resolve(file.getName()));
//						Files.copy(file.toPath(), exportRoot.resolve(file.getName()));
//					}
//				} else {
//					Files.copy(file.toPath(), exportRoot.resolve(file.getName()));
//				}
//
			} catch (IOException e) {
//				System.err.println("Unable to copy file " + file.getAbsolutePath());
//				e.printStackTrace();
			}
		}
	}

	@FXML
	public void importConnection() {

	}
}