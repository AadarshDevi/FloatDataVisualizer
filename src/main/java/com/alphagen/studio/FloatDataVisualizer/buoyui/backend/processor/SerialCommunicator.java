package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.processor;

import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.ConnectionConfig;
import com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data.FloatConfig;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortIOException;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
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
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

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

        System.out.println("[Serial] 1. Ready");

        // try to connect to serialport 5 times
        int attempts = 0;
        final int MAX_ATTEMPTS = 5;

//        do {
//            if (!serialPort.isOpen())
        serialPort.openPort();
//            attempts++;
//        } while (!serialPort.isOpen() || attempts > MAX_ATTEMPTS);

        if (!serialPort.isOpen()) {
            System.err.println("[Serial] 2. Unopenable - " + serialPort.getDescriptivePortName());
            errorCode.set(-2); // unable to connect to hardware
            return;
        }

        System.out.println("[Serial] 2. Open - " + serialPort.isOpen());

        // 1. open stream
        // 2. read through stream
        // 3. catch reading exceptions

        // 1. open stream
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(serialPort.getInputStream())
        )) {

            running.set(true);

            System.out.println("[SerialReader] Ready");
//            System.out.println("[SerialReader] 3. Ready - " + bufferedReader.ready());

            // 2. read stream
            while (running.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    String rawdata = bufferedReader.readLine();

                    System.out.println("[SerialRawData] " + rawdata);

                    if (rawdata == null && (!serialPort.isOpen() || (serialPort.bytesAvailable() == -1))) {
                        collectData.set(false);
                        throw new SerialPortIOException("[SerialException::External] Connection - " + serialPort.getDescriptivePortName());
                    }
                    rawdata = rawdata.trim();

                    if (rawdata.equals(startFlag)) {
                        collectData.set(true);
                    } else if (rawdata.equals(endFlag)) {
                        stop();
                    }

                    System.out.println("[SerialData] " + rawdata);

                    if (verbose) {
                        dataPointProcessor.getRawArray().put(rawdata);
                    }

                    if (collectData.get()) {
                        if (collectData.get()) {
                            dataPointProcessor.getRawArray().put(rawdata);
                        }
                    }

                } catch (InterruptedException e) {
                    System.err.println("[SerialException::Internal] Unusable");
                    stop();
                } catch (SerialPortTimeoutException _) {
                }
            }

            System.out.println("[Serial] B. Finished");

        } catch (SerialPortIOException _) {
            System.err.println("[SerialException::External] Disconnected");
            stop();
            try {
                dataPointProcessor.getRawArray().put("[SerialException] Disconnected"); // writing to terminal
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.err.println("[SerialException::External] Unopenable - " + serialPort.getDescriptivePortName());
            System.err.println("[SerialException::External] " + e.getMessage());
            e.printStackTrace();
            stop();
        }

        System.out.println("[Serial] A. Closed");
    }

    public void stop() {
        collectData.set(false);
        running.set(false);
    }

    public int getErrorCode() {
        return errorCode.get();
    }

    public boolean isConnected() {
        return serialPort.isOpen();
    }

    public boolean close() {
        return serialPort.closePort();
    }

    public boolean open() {
        return serialPort.openPort();
    }

    public boolean isRunning() {
        return running.get();
    }

}
