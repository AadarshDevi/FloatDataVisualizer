package com.alphagen.studio.FloatDataVisualizer.floatui.aquasition;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AcquisitionPage extends BorderPane {
    private static final Logger logger = LogManager.getLogger(AcquisitionPage.class);

    public AcquisitionPage() {

        ModeButton acquisitionButton = new ModeButton("Acquisition");
        ModeButton analysisButton = new ModeButton("Analysis");
        ModeButton settingsButton = new ModeButton("Settings");
        ModeButton userManualButton = new ModeButton("User Manual");
        ModeButton quitButton = new ModeButton("Quit");

        setPrefSize(1080, 720);
        setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#d3d3d3"),
                new CornerRadii(15),
                Insets.EMPTY
        )));

        HBox buttonContainer = new HBox();
        buttonContainer.setPrefHeight(50);
        buttonContainer.setBackground(new Background(
                new BackgroundFill(Paint.valueOf("#d3d3d3"),
                        new CornerRadii(15), Insets.EMPTY
                )
        ));
        buttonContainer.setSpacing(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(acquisitionButton, analysisButton, settingsButton, quitButton);
        this.setBottom(buttonContainer);
    }

    public void placeConnections() {

    }

    class ModeButton extends Button {
        public ModeButton(String text) {
            super(text);
        }
    }

}
