package com.alphagen.studio.FloatDataVisualizer;


import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.Backend;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.app.PlatformDetector;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.settings.SettingsManager;
import com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.managers.StageManager;
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
            alert.setTitle("Exception 1");
            alert.setContentText("Root folder is unable to be found or created.");
            alert.showAndWait();
            logger.error("Exception 1: Root folder is unable to be found or created.");
            System.exit(-1);
        }

        if (!backend.verifyFolder(FOLDER_CONNECTIONS)) {
            alert.setTitle("Exception 2");
            alert.setContentText("Base folder is unable to be found or created.\nBase Folder: connections");
            alert.showAndWait();
            logger.error("Exception 2: Base folder is unable to be found or created: {}", FOLDER_CONNECTIONS);
            System.exit(-1);
        }

        if (!backend.verifyFile(FILE_LOG)) {
            alert.setTitle("Exception 3");
            alert.setContentText("Base folder is unable to be found or created.\nBase Folder: logs");
            alert.showAndWait();
            logger.error("Exception 3: File is unable to be found or created: {}", FILE_LOG);
            System.exit(-1);
        }

        if (!backend.verifyFile(FILE_SETTINGS)) {
            alert.setTitle("Exception 4");
            alert.setContentText("");
            alert.showAndWait();
            logger.error("Exception 4: File is unable to be found or created: {}", FILE_SETTINGS);
            System.exit(-1);
        }
        logger.info("All startup folders and files are ready.");

        // todo: read settings file
        SettingsManager sm = SettingsManager.getInstance();

        Scene scene = new Scene(new AcquisitionPage());
        scene.setFill(Color.TRANSPARENT);

        StageManager.setConnectionsScene();
//        Scene scene = StageManager.getConnectionsScene();
        stage.setScene(scene);
        stage.setTitle("Float Data Visualizer");
        stage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("buoyui/logos/ImageLogo.png"))));

        logger.info("Opening App");
        stage.show();
    }
}
