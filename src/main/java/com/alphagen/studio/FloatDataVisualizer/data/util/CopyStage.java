package com.alphagen.studio.FloatDataVisualizer.data.util;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CopyStage {

    private final Stage stage;

    public CopyStage(String title, String message) {
        System.out.println(message);

        stage = new Stage();

        final Clipboard clipboard = Clipboard.getSystemClipboard();

        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(750.0, 150.0);
        borderPane.setCenter(label);

        HBox hBox = new HBox();
        hBox.setPrefSize(500.0, 50.0);
        borderPane.setBottom(hBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10.0);

        Button copyButton = new Button("Copy");
        copyButton.setPrefSize(100.0, 20.0);
        copyButton.setOnAction(event -> {
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(message);
            clipboard.setContent(clipboardContent);
        });

        Button closeButton = new Button("Ok");
        closeButton.setPrefSize(100.0, 20.0);
        closeButton.setOnAction(event -> {
            stage.close();
        });

        hBox.getChildren().add(copyButton);
        hBox.getChildren().add(closeButton);

        stage.setScene(new Scene(borderPane));
        stage.setTitle(title);
    }

    public void show() {
        stage.show();
    }
}
