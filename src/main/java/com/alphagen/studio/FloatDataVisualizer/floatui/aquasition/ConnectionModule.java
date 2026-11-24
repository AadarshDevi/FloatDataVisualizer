package com.alphagen.studio.FloatDataVisualizer.floatui.aquasition;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.floatui.FontManager;
import com.alphagen.studio.FloatDataVisualizer.floatui.component.SVG;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import javafx.scene.control.Button;


public class ConnectionModule extends Button {
    private static final Logger logger = LogManager.getLogger(ConnectionModule.class);

    public ConnectionModule(ConnectionConfig config) {
        VBox module = new VBox();
        module.setPrefSize(506, 90);
        setGraphic(module);

        Label nameLabel = new Label(config.name());
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setFont(FontManager.getFont(FontManager.CASCADIA_MON_MEDIUM, 18));
        nameLabel.setTextFill(Paint.valueOf("white"));
        nameLabel.setPadding(new Insets(10));
        module.getChildren().add(nameLabel);

        HBox descriptiveBox = new HBox();
        module.getChildren().add(descriptiveBox);

        Label portlabel = new Label("Port: " + config.port());
        portlabel.setFont(FontManager.getFont(FontManager.CASCADIA_MON_MEDIUM, 14));
        portlabel.setPadding(new Insets(10, 10, 10, 10));
        portlabel.setTextFill(Paint.valueOf("white"));
        descriptiveBox.getChildren().add(portlabel);

        ConnectionType type = config.type();
        SVG connectionSVG = switch (type) {
            case SERIAL -> SVG.Library.SERIAL_SVG;
            case WIFI -> SVG.Library.WIFI_SVG;
            case BLUETOOTH -> SVG.Library.BLUETOOTH_SVG;
            case null, default -> SVG.Library.UNKNOWN;
        };
        descriptiveBox.getChildren().add(connectionSVG);

        setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#151430"), // another: 000011, 15141c, 151430
                new CornerRadii(15),
                Insets.EMPTY
        )));

        setOnAction(event -> {
            // todo make button do stuff
        });

    }
}
