package com.alphagen.studio.FloatDataVisualizer.floatui.aquasition;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.floatui.FontManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import javafx.scene.control.Button;


public class ConnectionModule extends Button {
    private static final Logger logger = LogManager.getLogger(ConnectionModule.class);

    public ConnectionModule(ConnectionConfig config) {
        VBox module = new VBox();
        module.setPrefSize(506, 90);
        Label connectionName = new Label(config.name());
        module.getChildren().add(connectionName);
        setGraphic(module);
        this.setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#000011"),
                new CornerRadii(15),
                Insets.EMPTY
        )));

        setOnAction(event -> {
            // todo make button do stuff
        });

    }
}
