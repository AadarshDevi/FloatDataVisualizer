package com.alphagen.studio.float_data_recorder_2;

import com.alphagen.studio.float_data_recorder_2.data.DataKeeper;
import com.alphagen.studio.float_data_recorder_2.data.DataPoint;
import com.alphagen.studio.float_data_recorder_2.datarecorder.DataPlotter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/float_data_recorder.fxml"));

        BorderPane floatUI = fxmlLoader.load();
        DataPlotter dataPlotter = fxmlLoader.getController();

        if (dataPlotter == null) {
            System.err.println("ERROR: DataPlotter is Null");
            System.exit(0);
        }

        Scene scene = new Scene(floatUI);
        stage.setTitle("Float Data Plotter");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            if (!Launcher.isDataReceiverAlive()) {
                Launcher.killDataReceiver();
                Launcher.killDataKeeper();
            }
            System.out.println("LOG: Closing DataRecorder");
            System.exit(0);
        });
        stage.show();


        Thread dataReader = new Thread(() -> {
            boolean running = true;
            while (running) {
                DataPoint dataPoint = DataKeeper.getInstance().getDataPoint();
                if (dataPoint != null) {
                    Platform.runLater(() -> dataPlotter.writeDataPoint(dataPoint));
                }
            }
        });
        dataReader.setDaemon(true);
        dataReader.start();


    }
}
