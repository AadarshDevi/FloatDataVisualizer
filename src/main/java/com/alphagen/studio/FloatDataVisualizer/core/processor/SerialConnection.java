package com.alphagen.studio.FloatDataVisualizer.core.processor;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class SerialConnection extends CommunicationChannel {
    private static final Logger logger = LogManager.getLogger(SerialConnection.class);
    private final SerialPort serialPort;
    private final Consumer<String> consumer;
    private final ConnectionConfig connectionConfig;
    private volatile boolean collectData = false;

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

        collectData = false;

        try (BufferedReader serialReader = new BufferedReader(
                new InputStreamReader(serialPort.getInputStream()
                )
        )) {
            while (!Thread.currentThread().isInterrupted()) {

                String rawLine = "";
                try {
                    rawLine = serialReader.readLine();
                } catch (SerialPortTimeoutException e) {
                    logger.error("SerialPort has been timed out.");
                }

                if (rawLine == null || rawLine.isBlank()) {
                    if (!serialPort.isOpen() || serialPort.bytesAvailable() == -1) {
                        logger.error("Disconnected from SerialPort.");
                        collectData = false;
                        break;
                    }
                    continue;
                }

                trimmedLine = rawLine.trim();

                if (trimmedLine.equalsIgnoreCase(connectionConfig.startFlag())) {
                    collectData = true;
                } else if (trimmedLine.equalsIgnoreCase(connectionConfig.endFlag())) {
                    collectData = false;
                    break;
                }

                if (collectData) {
                    consumer.accept(trimmedLine);
                }

            }
        } catch (IOException e) {
            logger.error("Unable to open serial port.");
        } finally {
            collectData = false;
            serialPort.closePort();
        }
    }
}
