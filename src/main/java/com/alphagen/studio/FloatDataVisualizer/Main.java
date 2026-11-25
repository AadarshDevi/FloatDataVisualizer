package com.alphagen.studio.FloatDataVisualizer;


import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.Backend;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings.SettingsManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.util.StageUtil;
import com.alphagen.studio.FloatDataVisualizer.core.system.UserSystem;
import com.alphagen.studio.FloatDataVisualizer.floatui.aquasition.AcquisitionPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws Exception {

        final String FOLDER_CONNECTIONS = "connections";
        final String FILE_LOG = "float.log";
        final String FILE_SETTINGS = "float.settings";

        StageManager.setMainStage(stage);
        UserSystem userSystem = UserSystem.getInstance();
        userSystem.detectOS();
        PlatformDetector.detectPlatform(); // todo replace with user system

        logger.info("Initializing Backend");
        Backend backend = Backend.getBackend();

        logger.info("Finding Folders");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);

        if (!backend.verifyRoot()) {
            new AlertBox(
                    Alert.AlertType.INFORMATION,
                    "App Root Folder",
                    "Root folder is unable to be found or created.",
                    ButtonType.OK
            ).showAndWait();
            logger.info("Root folder \"FloatDataVisualizer\"is unable to be found or created.");
            Platform.exit();
        }

        if (!backend.verifyFolder(FOLDER_CONNECTIONS)) {
            new AlertBox(
                    Alert.AlertType.INFORMATION,
                    "Connections Folder Exception",
                    "Connection folder is unable to be found or created.",
                    ButtonType.OK
            ).showAndWait();
            logger.error("Connection folder is unable to be found or created.");
            Platform.exit();
        }

        if (!backend.verifyFile(FILE_LOG)) {
            new AlertBox(
                    Alert.AlertType.INFORMATION,
                    "Log File",
                    "Unable to locate or create log file.",
                    ButtonType.OK
            ).showAndWait();
            logger.error("Unable to locate or create log file.");
            Platform.exit();
        }

        if (!backend.verifyFile(FILE_SETTINGS)) {
            new AlertBox(
                    Alert.AlertType.INFORMATION,
                    "Settings File",
                    "Unable to locate or create settings file.",
                    ButtonType.OK
            ).showAndWait();
            logger.error("Unable to locate or create settings file.");
            Platform.exit();
        }
        logger.info("All startup folders and files are ready.");

        // todo: read settings file
        SettingsManager sm = SettingsManager.getInstance();

        AcquisitionPage acquisitionPage = new AcquisitionPage();
        acquisitionPage.loadConnections();
        Scene scene = new Scene(acquisitionPage);
        scene.setFill(Color.TRANSPARENT);

        StageUtil.customTitleBarDrag(stage, scene, acquisitionPage.getTitleBar());

        StageManager.setConnectionsScene();
//        Scene scene = StageManager.getConnectionsScene();
        stage.setScene(scene);
        stage.setTitle("Float Data Visualizer");
        stage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("buoyui/logos/ImageLogo.png"))));

        logger.info("Opening App");
        stage.show();
    }
}
