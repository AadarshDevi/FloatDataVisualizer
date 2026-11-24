package com.alphagen.studio.FloatDataVisualizer.floatui.aquasition;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionType;
import com.alphagen.studio.FloatDataVisualizer.floatui.FontManager;
import com.alphagen.studio.FloatDataVisualizer.floatui.component.SVG;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class ConnectionModule extends Button {

    private final ConnectionLabel nameLabel;
    private final ConnectionLabel portLabel;
    private final ConnectionLabel baudRateLabel;
    private final SVG connectionSVG;

    private final CornerRadii radii = new CornerRadii(15);
    private final Insets insets = Insets.EMPTY;

    public ConnectionModule(ConnectionConfig config) {

        nameLabel = new ConnectionLabel(config.name());
        nameLabel.setFontSize(18);

        ConnectionLabel portLabel = new ConnectionLabel("Port: " + config.port());
        ConnectionLabel baudRateLabel = new ConnectionLabel("BaudRate: " + config.baudRate());

        ConnectionType type = config.type();
        SVG connectionSVG = switch (type) {
            case SERIAL -> new SVG(SVG.Library.SERIAL_SVG_PATH);
            case WIFI -> new SVG(SVG.Library.WIFI_SVG_PATH);
            case BLUETOOTH -> new SVG(SVG.Library.BLUETOOTH_SVG_PATH);
            case null, default -> new SVG(SVG.Library.UNKNOWN_SVG_PATH);
        };
        connectionSVG.setFill(Paint.valueOf("white"));

        VBox svgPane = new VBox();
        svgPane.getChildren().add(connectionSVG);
        svgPane.setAlignment(Pos.BOTTOM_CENTER);

        setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#151430"), // another: 000011, 15141c, 151430
                new CornerRadii(15),
                Insets.EMPTY
        )));

        setOnAction(event -> {
            // todo make button do stuff
        });

        BorderPane module = new BorderPane();
        module.setPrefSize(506, 90);
        module.setPadding(new Insets(10));

        module.setTop(nameLabel);
        module.setRight(svgPane);

        VBox infoBox = new VBox();
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.getChildren().addAll(portLabel, baudRateLabel);
        infoBox.setSpacing(6);
        infoBox.setPadding(new Insets(10, 0, 0, 0));
        module.setCenter(infoBox);

        setGraphic(module);
    }

    public ConnectionModule(com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig config) {
        this(ConnectionConfig.toNew(config));
    }

    // todo implement toLight
    public void toLight() {
        nameLabel.toLight();
        portLabel.toLight();
        baudRateLabel.toLight();
        connectionSVG.setFill(Paint.valueOf("black"));
        setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#f4f4f4"), // another: 000011, 15141c, 151430
                radii,
                insets
        )));
    }

    // todo implement toDark
    public void toDark() {
        nameLabel.toDark();
        portLabel.toDark();
        baudRateLabel.toDark();
        connectionSVG.setFill(Paint.valueOf("white"));
        setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#151430"), // another: 000011, 15141c, 151430
                radii,
                insets
        )));
    }

    static class ConnectionLabel extends Label {
        public ConnectionLabel(String text) {
            super(text);
            setFont(FontManager.getFont(FontManager.Library.CASCADIA_MON_MEDIUM, 14));
            setMaxWidth(Double.MAX_VALUE);
        }

        public void setFontSize(int fontSize) {
            Font font = new Font(getFont().getFamily(), fontSize);
            setFont(font);
        }

        public void toLight() {
            setTextFill(Paint.valueOf("black"));
        }

        public void toDark() {
            setTextFill(Paint.valueOf("white"));
        }
    }
}
