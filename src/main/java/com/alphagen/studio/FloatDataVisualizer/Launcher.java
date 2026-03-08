package com.alphagen.studio.FloatDataVisualizer;

import com.alphagen.studio.FloatDataVisualizer.buoyui.connections.ConnectionControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.connections.ConnectionUIConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.connections.ConnectionsController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.util.DeltaDrag;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Launcher extends Application {

//	static Thread dkt;
//	static Thread drt;

	public static void main(String[] args) {

//        DataConfigurator setting = DataConfigurator.getInstance();
//        setting.readSettings();
//
//        if (setting.isNewUser) {
//            new JFXPanel();
//            javafx.application.Platform.runLater(() -> {
//                new CopyStage("Config File Filepath",
//                        DataPathFactory.getFilePathFactory().getFilePath().getSettingsPath()
//                ).show();
//            });
//        }
//
//        DataKeeper dataKeeper = DataKeeper.getInstance();
//        dkt = new Thread(dataKeeper);
//        dkt.setName("DataKeeper");
//        dkt.start();
//
//        DataReceiver dataReceiver = DataReceiver.getInstance();
//        drt = Thread.ofVirtual()
//                .name("DataReceiver")
//                .start(dataReceiver);

		launch(args);
//		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		System.out.println("Starting FloatDataVisualizer");

		System.out.println("Loading ConnectionsUI");
		FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(ConnectionUIConstants.CONNECTIONS_PAGE));
		BorderPane buoyUI = fxmlLoader.load();
		ConnectionsController bmc = fxmlLoader.getController();
		ConnectionControllerManager.setConnectionsController(bmc);

		Scene scene = new Scene(buoyUI);
		scene.setFill(Color.TRANSPARENT);

		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setTitle("Float Data Visualizer");

		StageManager.setMainStage(stage);
		final DeltaDrag launcherDrag = new DeltaDrag();

		buoyUI.setOnMousePressed(event -> {
			launcherDrag.setDeltaX(event.getSceneX());
			launcherDrag.setDeltaY(event.getSceneY());
		});

		// mouse dragged: move the stage
		buoyUI.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - launcherDrag.getDeltaX());
			stage.setY(event.getScreenY() - launcherDrag.getDeltaY());
		});

		System.out.println("Opening App");
		stage.show();
	}

//	public static void killDataReceiver() {
//		drt.interrupt();
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}
//		if (drt.isAlive()) {
//			JOptionPane.showMessageDialog(null, "Unable to kill DataReceiver", "Launcher Exception", JOptionPane.ERROR_MESSAGE);
//			System.err.println("ERROR: Data Receiver is still running");
//		} else System.out.println("LOG: Closed > Data Receiver");
//	}
//
//	public static boolean isDataReceiverAlive() {
//		return drt.isAlive();
//	}
//
//	public static void killDataKeeper() {
//        DataKeeper.getInstance().stop();
//		dkt.interrupt();
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}
//		if (dkt.isAlive()) {
//			JOptionPane.showMessageDialog(null, "Unable to kill DataKeeper", "Launcher Exception", JOptionPane.ERROR_MESSAGE);
//			System.err.println("ERROR: Data Keeper is still running");
//		} else System.out.println("LOG: Closed > Data Keeper");
//	}
//
//	public static Thread getDataKeeperThread() {
//		return dkt;
//	}
//
//	public static Thread getDataReceiverThread() {
//		return drt;
//	}
//
//	// Platform Obj in Settings
//	public enum Platform {
//		WIN11,
//		MACOS,
//		LINUX
//	}
}