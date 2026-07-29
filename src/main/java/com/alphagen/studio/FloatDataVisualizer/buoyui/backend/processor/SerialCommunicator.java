package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.fazecast.jSerialComm.SerialPort;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SerialCommunicator implements Runnable {

    private final SerialPort serialPort;
    private final DataPointProcessor dataPointProcessor;

    private final String startFlag;
    private final String endFlag;
    private final String teamData;
    private final AtomicBoolean running;
    private final AtomicBoolean collectData;
    // for future to send cmd to float
    private final AtomicReference<String> command = new AtomicReference<>();
    private final AtomicInteger errorCode = new AtomicInteger(0);
    @Getter
    @Setter
    private boolean verbose;

    public SerialCommunicator(ConnectionConfig connectionConfig, DataPointProcessor dataPointProcessor, boolean verbose) {

        serialPort = connectionConfig.port();
        serialPort.setBaudRate(connectionConfig.baudRate());
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);

        FloatConfig floatConfig = connectionConfig.floatConfig();
        startFlag = floatConfig.startFlag();
        endFlag = floatConfig.endFlag();
        teamData = floatConfig.teamData();

        this.verbose = verbose;

        this.running = new AtomicBoolean(false);
        this.collectData = new AtomicBoolean(false);

        this.dataPointProcessor = dataPointProcessor;
        
//        serialPort.addDataListener(new SerialPortDataListener() {
//            @Override
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
//            }
//
//            @Override
//            public void serialEvent(SerialPortEvent event) {
//                if ((event.getEventType() & SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) > 0) {
//                    serialPort.closePort();
//                    errorCode.set(-1); // disconnected hardware
//                    running.set(false);
//                }
//            }
//        });
    }

    @Override
    public void run() {

        // try to connect to serialport 5 times
        int attempts = 0;
        final int MAX_ATTEMPTS = 5;

        do {
            if (!serialPort.isOpen())
                serialPort.openPort();
            attempts++;
        } while (!serialPort.isOpen() || attempts > MAX_ATTEMPTS);

        if (!serialPort.isOpen()) {
            errorCode.set(-2); // unable to connect to hardware
            return;
        }

        // 1. open stream
        // 2. read through stream
        // 3. catch reading exceptions

        // 1. open stream
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(serialPort.getInputStream())
        )) {

            // 2. read stream
            while (running.get() && !Thread.currentThread().isInterrupted()) {
                String rawdata = bufferedReader.readLine();

                if (rawdata == null && (!serialPort.isOpen() || (serialPort.bytesAvailable() == -1))) {
                    System.err.println("Unable to get data");
                }
                rawdata = rawdata.trim();

                // catch reading exceptions
                try {

                    if (rawdata.equals(startFlag)) {
                        collectData.set(true);
                    } else if (rawdata.equals(endFlag)) {
                        running.set(false);
                        collectData.set(false);
                    }

                    if (verbose) {
                        dataPointProcessor.getRawArray().put(rawdata);
                    } else if (collectData.get() && rawdata.startsWith(teamData)) {
                        dataPointProcessor.getRawArray().put(rawdata);
                    }

                } catch (InterruptedException e) {
                    System.err.println("Unable to add data: " + rawdata);
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to open serialport in " + serialPort.getDescriptivePortName());
        }
    }

    public void stop() {
        collectData.set(false);
        running.set(false);
    }

    public int getErrorCode() {
        return errorCode.get();
    }

}
