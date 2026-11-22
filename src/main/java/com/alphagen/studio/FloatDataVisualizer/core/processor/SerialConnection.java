package com.alphagen.studio.FloatDataVisualizer.core.processor;


import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class SerialConnection extends CommunicationChannel {
    private static final Logger logger = LogManager.getLogger(SerialConnection.class);
    private final SerialPort serialPort;
    private final Consumer<String> consumer;
    private volatile boolean running = false;
    private volatile boolean stopDataTransfer = false;
    private volatile boolean startDataTransfer = false;

    public SerialProcessor(ConnectionConfig config, Consumer<DataPacket> consumer) {
        this.consumer = consumer;
        serialPort = config.port();
        serialPort.setBaudRate(config.baudRate());
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (stopDataTransfer) {
                stopDataTransfer = false;
            } else if (startDataTransfer) {
                startDataTransfer = false;
            }
        }

    }

    public void stop() {
        running = false;
        collecting = false;
    }

    public void shutdown() {
        stop();
    }

    public void start() {
        running = true;
        collecting = false;
    }
}
