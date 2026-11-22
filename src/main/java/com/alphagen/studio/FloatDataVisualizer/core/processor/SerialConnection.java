package com.alphagen.studio.FloatDataVisualizer.core.processor;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class SerialConnection extends CommunicationConnection {
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
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 1000);
    }

    @Override
    public void run() {

        // fixme: uncomment code when having serial
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

        // Reset Float Reciever
        serialPort.clearDTR();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        serialPort.setDTR();

        logger.info("Started SerialConnection");

        try (BufferedReader serialReader = new BufferedReader(
                new InputStreamReader(serialPort.getInputStream()
                )
        )) {

            while (!Thread.currentThread().isInterrupted()) {

                String rawLine = "";
                try {
                    rawLine = serialReader.readLine();
                } catch (SerialPortTimeoutException _) {
                    continue;
                }

                if (rawLine == null || rawLine.isBlank()) {
                    if (!serialPort.isOpen() || serialPort.bytesAvailable() == -1) {
                        logger.error("Disconnected from SerialPort.");
                        collectData = false;
                        break;
                    }
                    continue;
                }

                rawLine = rawLine.trim();

                if (rawLine.equalsIgnoreCase(connectionConfig.startFlag())) {
                    logger.info("Data collection started.");
                    collectData = true;
                } else if (rawLine.equalsIgnoreCase(connectionConfig.endFlag())) {
                    logger.info("Data collection completed.");
                    collectData = false;
                    break;
                } else if (collectData) {
                    consumer.accept(rawLine);
                }

            }
        } catch (IOException e) {
            logger.error("Unable to open serial port.");
        } finally {
            collectData = false;
            serialPort.closePort();
            logger.info("Closed SerialConnection.");
        }
    }

    public synchronized boolean write(String cmd) {

        // serialport dne
        if (serialPort == null) {
            logger.error("Unable to find SerialPort.");
            return false;
        }

        // serial port open for write
        if (!serialPort.isOpen()) {
            logger.error("SerialPort is not open.");
            return false;
        }

        // convert string to byte[]
        byte[] dataAsBytes = (cmd + "\n").getBytes(StandardCharsets.UTF_8);

        // the no of bytes that were written to serial
        int bytesWritten = serialPort.writeBytes(dataAsBytes, dataAsBytes.length);

        if (bytesWritten != dataAsBytes.length) {
            logger.error("Unable to write command \"" + cmd + "\" to SerialPort.");
            return false;
        }

        return true;
    }
}
