package com.alphagen.studio.FloatDataVisualizer.core.processor;


import com.alphagen.studio.FloatDataVisualizer.core.data.ConnectionConfig;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class SerialConnection implements CommunicationConnection {
    private static final Logger logger = LogManager.getLogger(SerialConnection.class);
    private final SerialPort serialPort;
    private final Consumer<String> consumer;
    private final ConnectionConfig connectionConfig;
    private volatile boolean disconnected = false;
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

        disconnected = false;

        // fixme: uncomment code when having serial
        if (serialPort == null) {
            logger.error("Unable to find SerialPort.");
            disconnected = true;
            return;
        }

        serialPort.openPort();

        if (!serialPort.isOpen()) {
            logger.error("Unable to open serial port.");
            disconnected = true;
            return;
        }

        collectData = false;

        // Reset Float Reciever
        serialPort.clearDTR();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            disconnected = true;
            serialPort.closePort();
            return;
        }
        serialPort.setDTR();

        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
                    disconnected = true;
                }
            }
        });

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
                        disconnected = true;
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
            disconnected = true;
        } finally {
            collectData = false;
            serialPort.removeDataListener();
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

    @Override
    public boolean isOpen() {
        return (serialPort != null) && serialPort.isOpen();
    }

    @Override
    public boolean isConnected() {
        return !disconnected;
    }

    public boolean isPortAvailable() {
        if (connectionConfig == null || connectionConfig.port() == null) {
            return false;
        }
        for (SerialPort port : SerialPort.getCommPorts()) {
            if (connectionConfig.port().equalsIgnoreCase(port.getSystemPortName())) {
                return true;
            }
        }
        return false;
    }
}
