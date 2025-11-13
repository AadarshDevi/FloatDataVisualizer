package com.alphagen.studio.FloatDataVisualizer;

import com.alphagen.studio.FloatDataVisualizer.data.Settings;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.data.DataPointRecord;
import com.alphagen.studio.FloatDataVisualizer.datarecorder.DataPlotter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/float_data_recorder.fxml"));

        BorderPane floatUI = fxmlLoader.load();
        DataPlotter dataPlotter = fxmlLoader.getController();

        if (dataPlotter == null) {
            JOptionPane.showMessageDialog(null, "DataPlotter is null.", "Main Exception", JOptionPane.ERROR_MESSAGE);
            System.err.println("ERROR: DataPlotter is Null");
            System.exit(0);
        }

        Scene scene = new Scene(floatUI);
        stage.setTitle("Miramar Water Jets Float Data Visualizer " + Settings.getInstance().getReleaseVersion());
        stage.setScene(scene);
        Image icon = new Image(Main.class.getResourceAsStream("float_data_recorder_2_png_icon_2.png"));
        stage.getIcons().add(icon);
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
                DataPointRecord dataPointRecord = DataKeeper.getInstance().getDataPointRecord();
                if (dataPointRecord != null) {
                    Platform.runLater(() -> dataPlotter.writeDataPoint(dataPointRecord));
                }
            }
        });
        dataReader.setName("DataReader");
        dataReader.setDaemon(true);
        dataReader.start();


    }
}
