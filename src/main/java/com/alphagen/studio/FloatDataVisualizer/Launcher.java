package com.alphagen.studio.FloatDataVisualizer;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.Backend;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.ControllerManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.PageConstants;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.ConnectionsController;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


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

        System.out.println("Starting FloatDataVisualizer");
        launch(args);
//		Application.launch(Main.class, args);
    }

//    public static void killDataReceiver() {
//        drt.interrupt();
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        if (drt.isAlive()) {
//            JOptionPane.showMessageDialog(null, "Unable to kill DataReceiver", "Launcher Exception", JOptionPane.ERROR_MESSAGE);
//            System.err.println("ERROR: Data Receiver is still running");
//        } else System.out.println("LOG: Closed > Data Receiver");
//    }
//
//    public static boolean isDataReceiverAlive() {
//        return drt.isAlive();
//    }
//
//    public static void killDataKeeper() {

    /// /        DataKeeper.getInstance().stop();
//        dkt.interrupt();
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        if (dkt.isAlive()) {
//            JOptionPane.showMessageDialog(null, "Unable to kill DataKeeper", "Launcher Exception", JOptionPane.ERROR_MESSAGE);
//            System.err.println("ERROR: Data Keeper is still running");
//        } else System.out.println("LOG: Closed > Data Keeper");
//    }
//
//    public static Thread getDataKeeperThread() {
//        return dkt;
//    }
//
//    public static Thread getDataReceiverThread() {
//        return drt;
//    }
    @Override
    public void start(Stage stage) throws Exception {

        PlatformDetector pd = new PlatformDetector();
        pd.detectPlatform();
        System.exit(0);

        System.out.println("Initializing Backend");
        Backend backend = Backend.getBackend();

        System.out.println("Finding Folders");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");

        if (!backend.verifyRoot()) {
            alert.setTitle("Exception 1");
            alert.setContentText("Root folder is unable to be found or created.");
            alert.showAndWait();
            System.exit(-1);
        }

        if (!backend.verifyFolder("connections")) {
            alert.setTitle("Exception 2");
            alert.setContentText("Base folder is unable to be found or created.\nBase Folder: connections");
            alert.showAndWait();
            System.exit(-1);
        }

        if (!backend.verifyFolder("logs")) {
            alert.setTitle("Exception 3");
            alert.setContentText("Base folder is unable to be found or created.\nBase Folder: logs");
            alert.showAndWait();
            System.exit(-1);
        }

        if (!backend.verifyFile("float.settings")) {
            alert.setTitle("Exception 4");
            alert.setContentText("File is unable to be found or created.\nFile: float.settings");
            alert.showAndWait();
            System.exit(-1);
        }
        System.out.println("All startup folders and settings file ready.");

        System.out.println("Loading ConnectionsUI");

        FXMLLoader fxmlLoader = new FXMLLoader(PageConstants.CONNECTIONS_PAGE);
        BorderPane buoyUI = fxmlLoader.load();
        ConnectionsController bmc = fxmlLoader.getController();
        ControllerManager.setConnectionsController(bmc);

        Scene scene = new Scene(buoyUI);

        stage.setScene(scene);
        stage.setTitle("Float Data Visualizer");

        StageManager.setMainStage(stage);

        StageUtil.createInvisPane(stage, scene, buoyUI);

        System.out.println("Opening App");
        stage.show();
    }

//    // Platform Obj in Settings
//    public enum Platform {
//        WIN11,
//        MACOS,
//        LINUX
//    }
}