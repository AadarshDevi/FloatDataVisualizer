package com.alphagen.studio.FloatDataVisualizer.floatui.alerts;


import com.alphagen.studio.FloatDataVisualizer.floatui.FontManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlertBox extends Alert {
    private static final Logger logger = LogManager.getLogger(AlertBox.class);

    public AlertBox(AlertType alertType, String title, String message, ButtonType... buttons) {
        super(alertType);
        this.initStyle(StageStyle.TRANSPARENT);
        this.setTitle(null);
        this.setHeaderText(null);
        this.setGraphic(null);
        this.setContentText(null);

        ButtonType[] buttonType = buttons.length == 0 ? new ButtonType[]{ButtonType.OK} : buttons;
        this.getButtonTypes().setAll(buttonType);

        Label label = new Label(title + "\n\n" + message);
        label.setFont(FontManager.getFont(FontManager.Library.CASCADIA_MON_MEDIUM, 14));
        label.setWrapText(true);

        DialogPane dialogPane = this.getDialogPane();
        dialogPane.setPrefWidth(500);
        dialogPane.setHeaderText(null);
        dialogPane.setGraphic(null);
        dialogPane.getScene().setFill(Color.TRANSPARENT);
        dialogPane.setBackground(new Background(new BackgroundFill(
                Paint.valueOf("white"),
                new CornerRadii(10),
                Insets.EMPTY
        )));
        dialogPane.setBorder(new Border(new BorderStroke(
                Paint.valueOf("#dbdbdb"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(3)
        )));
        dialogPane.setPadding(new Insets(10));
        dialogPane.setContent(label);

        // in the constructor, after `DialogPane dialogPane = this.getDialogPane();`
        dialogPane.setOnMousePressed(e -> {
            dragOffsetX = e.getScreenX() - getX();
            dragOffsetY = e.getScreenY() - getY();
        });

        dialogPane.setOnMouseDragged(e -> {
            setX(e.getScreenX() - dragOffsetX);
            setY(e.getScreenY() - dragOffsetY);
        });

        for (ButtonType type : buttonType) {
            Button button = (Button) dialogPane.lookupButton(type);
            if (button == null) continue;
            Color buttonColor = Color.WHITE;
            CornerRadii buttonCornerRadii = new CornerRadii(5);

            button.setMinWidth(80);
            button.setBackground(new Background(new BackgroundFill(
                    buttonColor,
                    buttonCornerRadii,
                    Insets.EMPTY
            )));
            button.setBorder(new Border(new BorderStroke(Paint.valueOf("#eaeaea"), BorderStrokeStyle.SOLID, buttonCornerRadii, new BorderWidths(2))));
            button.hoverProperty().addListener((obs, was, is) -> {
                button.setBackground(new Background(new BackgroundFill(
                        is ? buttonColor.deriveColor(0, 1, 0.9, 1) : buttonColor,
                        buttonCornerRadii,
                        Insets.EMPTY)));
            });
        }
    }
}
