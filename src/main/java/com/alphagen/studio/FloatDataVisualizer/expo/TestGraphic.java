package com.alphagen.studio.FloatDataVisualizer.expo;


import com.alphagen.studio.FloatDataVisualizer.core.data.*;
import com.alphagen.studio.FloatDataVisualizer.core.processor.SerialConnection;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class TestGraphic extends Scene {
    private static final Logger logger = LogManager.getLogger(TestGraphic.class);
    private RunnableManager reader = new RunnableManager(null);

    public TestGraphic() {

        super(new Pane());

        Button startButton = new Button("Start");
        startButton.setPrefSize(60, 20);

        Button stopButton = new Button("Stop");
        stopButton.setPrefSize(60, 20);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label("-");
        label.setPadding(new Insets(0, 0, 0, 5));
        AtomicReference<String> string = new AtomicReference<>();

        ConnectionConfig config = ConnectionConfig.builder()
                .name("Float Data Visualizer Windows 11 Serial Connection Test")
                .os(OperatingSystem.WINDOWS)
                .version(new Version(3, 0, 0))
                .type(ConnectionType.SERIAL)
                .port(SerialPort.getCommPorts()[0].getSystemPortName())
                .baudRate(115200)
                .startFlag("--start-data-transfer")
                .endFlag("--end-data-transfer")
                .teamInfo(
                        TeamInfo.builder()
                                .teamNum("PN12")
                                .teamName("MiramarWaterJets")
                                .build()
                )
                .sensor(new SensorUnit("Time", "s"))
                .sensor(new SensorUnit("Depth", "m"))
                .sensor(new SensorUnit("Pressure", "kPa"))
                .build();

        SerialConnection serialConnection = new SerialConnection(
                config,
                data -> {

                    // @test
//                    string.set(String.format("Profile + Packet: %s", data));
                    try {
                        int[] profileInfo = Arrays.stream(data.split(","))
                                .map(String::trim)
                                .mapToInt(Integer::parseInt)
                                .toArray();
                        boolean isRunning = reader.isRunning();
                        logger.warn(isRunning);
                        // @test-end
                        Platform.runLater(() -> {
                                    label.setText("Profile: " + profileInfo[0] + "\tPacket: " + profileInfo[1]);
                                    textArea.appendText("Profile: " + profileInfo[0] + "\tPacket: " + profileInfo[1] + "\n");
//                                logger.info(string.get());
                                }
                        );
                    } catch (Exception e) {
                        textArea.appendText(data);
                        if (data.equalsIgnoreCase(config.endFlag())) {
                            Platform.runLater(() -> {
                                        logger.info("Data extraction completed.");
                                        stopButton.fire();
                                    }
                            );
                        }
                    }
                }
        );
        reader = new RunnableManager(serialConnection);

        stopButton.setVisible(false);

        startButton.setOnAction(event -> {
            startButton.setVisible(false);
            startButton.setManaged(false);
            stopButton.setVisible(true);
            stopButton.setManaged(true);
            reader.start();
        });

        stopButton.setOnAction(event -> {
            stopButton.setVisible(false);
            stopButton.setManaged(false);
            startButton.setVisible(true);
            startButton.setManaged(true);
            reader.stop();
        });

        VBox vbox = new VBox(10);
        vbox.setPrefSize(500, 200);
        vbox.getChildren().add(startButton);
        vbox.getChildren().add(stopButton);
        vbox.getChildren().addFirst(label);

        setRoot(vbox);

        logger.info("TestGraphic has been created.");

    }

    public void stop() {
        reader.shutdown();
    }
}
