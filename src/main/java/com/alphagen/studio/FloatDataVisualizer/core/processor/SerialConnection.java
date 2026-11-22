package com.alphagen.studio.FloatDataVisualizer.core.processor;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class SerialConnection extends CommunicationChannel {
    private static final Logger logger = LogManager.getLogger(SerialConnection.class);
    private final SerialPort serialPort;
    private final Consumer<String> consumer;
    private final boolean collecting = false;
    private final ConnectionConfig connectionConfig;

    public SerialConnection(ConnectionConfig config, Consumer<String> consumer) {
        this.consumer = consumer;
        this.connectionConfig = config;
        serialPort = SerialPort.getCommPort(connectionConfig.port());
        serialPort.setBaudRate(config.baudRate());
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
    }

    @Override
    public void run() {

        if (serialPort == null) {
            logger.error("Unable to find SerialPort.");
            return;
        }

        serialPort.openPort();

        if (!serialPort.isOpen()) {
            logger.error("Unable to open serial port.");
            return;
        }

        collecting = false;

        try (BufferedReader serialReader = new BufferedReader(
                new InputStreamReader(serialPort.getInputStream()
                )
        )) {

            while (!Thread.currentThread().isInterrupted()) {

            }
        } catch (IOException e) {
            logger.error("Unable to open serial port.");
        }
    }
}
