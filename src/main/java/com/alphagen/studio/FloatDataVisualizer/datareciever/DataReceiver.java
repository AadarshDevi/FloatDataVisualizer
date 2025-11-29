package com.alphagen.studio.FloatDataVisualizer.datareciever;

import com.alphagen.studio.FloatDataVisualizer.data.DataConfigurator;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.log.Exitter;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataReceiver implements Runnable, Exitter {

    private static DataReceiver dr;
    private final DataKeeper dataKeeper;
    private final SerialPort serialPort;
    private final DataConfigurator dataConfigurator;

    public DataReceiver(DataKeeper dataKeeper) {

        dataConfigurator = DataConfigurator.getInstance();

        System.out.println("DATA: Serial Connection >");

        this.dataKeeper = dataKeeper;

        SerialPort serialPort = null;
        try {
            for (SerialPort sp : SerialPort.getCommPorts()) {
                if (sp.getSystemPortPath().equals(dataConfigurator.getSerialCommPort())) {
                    serialPort = SerialPort.getCommPort(dataConfigurator.getSerialCommPort());
                    break;
                }
            }

            if (serialPort == null) {
                exit("WARNING: Do NOT close the app if you are using it for the first time.\nSerialComm Port does not exist: " + dataConfigurator.getSerialCommPort());
            }

        } catch (SerialPortInvalidPortException e) {
            exit("Unable to create SerialComm with port: " + dataConfigurator.getSerialCommPort());
            throw new RuntimeException(e);
        }

        boolean success = serialPort.openPort();
        serialPort.closePort();
        if (!success) {
            dataConfigurator.showSerialCommPorts();
        }

        this.serialPort = serialPort;

        this.serialPort.setBaudRate(dataConfigurator.getBaudRate());
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        boolean connectionEstablished = this.serialPort.openPort();
        System.out.println("\tCOMB: Connection > " + connectionEstablished);
        if (!connectionEstablished) exit("COM Port Connection ERROR");

        System.out.println("\tCOMM: Port Connection Established");
        System.out.println();
    }

    public static DataReceiver getInstance() {
        if (dr == null) dr = new DataReceiver(DataKeeper.getInstance());
        return dr;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try (
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(
                                    serialPort.getInputStreamWithSuppressedTimeoutExceptions()
                            )
                    )
            ) {

                String dataline;
                boolean startDataTransfer = false;
                boolean endDataTransfer = false;

                while ((dataline = bufferedReader.readLine()) != null) {

                    if (dataline.equals(dataConfigurator.getStartFlag())) {
                        System.out.println(dataline);
                        System.out.println();
                        startDataTransfer = true;
                    } else if (dataline.equals(dataConfigurator.getEndFlag())) {
                        System.out.println();
                        System.out.println(dataline);
                        endDataTransfer = true;
                        bufferedReader.close();
                        break;
                    }

                    if ((dataline != null) && startDataTransfer && !endDataTransfer && dataline.startsWith(dataConfigurator.getTeamData())) {
                        System.out.println(dataline);
                        dataKeeper.writeData(dataline);
                    }
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, " (Ignore)\nUnable to \"readLine()\"", "DataReceiver Exception", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (serialPort != null)
            serialPort.closePort();
        System.out.println("LOG: Float Data Recorder has stopped receiving data.");
    }

    public boolean isRunning() {
        return serialPort.isOpen();
    }
}