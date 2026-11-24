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

public class ConnectionModule extends Button {

    public ConnectionModule(ConnectionConfig config) {

        Label nameLabel = new Label(config.name());
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setFont(FontManager.getFont(FontManager.CASCADIA_MON_MEDIUM, 18));
        nameLabel.setTextFill(Paint.valueOf("white"));
//        nameLabel.setPadding(new Insets(10));

        Label portlabel = new Label("Port: " + config.port());
        portlabel.setFont(FontManager.getFont(FontManager.CASCADIA_MON_MEDIUM, 14));
//        portlabel.setPadding(new Insets(10, 10, 10, 10));
        portlabel.setTextFill(Paint.valueOf("white"));
        portlabel.setMaxWidth(Double.MAX_VALUE);

        Label baudRateLabel = new Label("BaudRate: " + config.baudRate());
//        baudRateLabel.setPadding(new Insets(0, 10, 10, 10));
        baudRateLabel.setTextFill(Paint.valueOf("white"));
        baudRateLabel.setFont(FontManager.getFont(FontManager.CASCADIA_MON_MEDIUM, 14));


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
        infoBox.getChildren().addAll(portlabel, baudRateLabel);
        infoBox.setSpacing(6);
        infoBox.setPadding(new Insets(10, 0, 0, 0));
        module.setCenter(infoBox);

        setGraphic(module);
    }

    public void toLight() {
    }

    public void toDark() {
    }

    class ConnectionLabel extends Label {
        public ConnectionLabel(String text) {
            super(text);
            setFont(FontManager.getFont(FontManager.CASCADIA_MON_MEDIUM, 14));
            setTextFill(Paint.valueOf("white"));
            setMaxWidth(Double.MAX_VALUE);
        }

        public void setFontSize(int fontSize) {
            Font font = new Font(getFont().getFamily(), fontSize);
            setFont(font);
        }
    }
}
