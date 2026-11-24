package com.alphagen.studio.FloatDataVisualizer.floatui.aquasition;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import javafx.scene.control.Button;


public class ConnectionModule extends Button {
    private static final Logger logger = LogManager.getLogger(ConnectionModule.class);

    public ConnectionModule(ConnectionConfig config) {
        VBox module = new VBox();
        module.setPrefSize(500, 300);
        Label connectionName = new Label(config.name());
        module.getChildren().add(connectionName);
        setGraphic(module);
        this.setBackground(new Background(new BackgroundFill(
                Paint.valueOf("000011"),
                new CornerRadii(5),
                new Insets(5)
        )));

        setOnAction(event -> {
            // todo make button do stuff
        });

    }
}
