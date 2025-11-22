package com.alphagen.studio.FloatDataVisualizer.datareciever;

import com.alphagen.studio.FloatDataVisualizer.Launcher;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.data.Settings;
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
    private final Settings settings;

    public DataReceiver(DataKeeper dataKeeper) {

        settings = Settings.getInstance();

        System.out.println("DATA: Serial Connection >");

        this.dataKeeper = dataKeeper;
        SerialPort[] serialPorts = SerialPort.getCommPorts();


        SerialPort serialPort;
        try {
            serialPort = SerialPort.getCommPort(settings.getSerialCommPort());
        } catch (SerialPortInvalidPortException e) {
//            JOptionPane.showMessageDialog(null, "Unable to create SerialComm with port: " + settings.getSerialCommPort(), "DataReceiver Exception", JOptionPane.ERROR_MESSAGE);
            exit("Unable to create SerialComm with port: " + settings.getSerialCommPort());
            throw new RuntimeException(e);
        }

        boolean success = serialPort.openPort();
        serialPort.closePort();
        if (!success) {
            settings.showSerialCommPorts();
        }

        this.serialPort = serialPort;

        this.serialPort.setBaudRate(settings.getBaudRate());
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        boolean connectionEstablished = this.serialPort.openPort();
        System.out.println("\tLOG: Connection > " + connectionEstablished);
        if (!connectionEstablished) exit("COM Port Connection ERROR");

        System.out.println("\tLOG: COM Port Connection Established");
        System.out.println();
    }

    public static DataReceiver getInstance() {
        if (dr == null) dr = new DataReceiver(DataKeeper.getInstance());
        return dr;
    }

    @Override
    public void run() {

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

                if (dataline.equals(settings.getStartFlag())) {
                    System.out.println(dataline);
                    System.out.println();
                    startDataTransfer = true;
                } else if (dataline.equals(settings.getEndFlag())) {
                    System.out.println();
                    System.out.println(dataline);
                    endDataTransfer = true;
                    bufferedReader.close();
                    break;
                }

                if ((dataline != null) && startDataTransfer && !endDataTransfer && dataline.startsWith(settings.getTeamData())) {
                    System.out.println(dataline);
                    dataKeeper.writeData(dataline);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to \"readLine()\"", "DataReceiver Exception", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

        if(serialPort != null)
            serialPort.closePort();
        System.out.println("LOG: Float Data Recorder has stopped receiving data.");
    }

    public boolean isRunning() {
        return serialPort.isOpen();
    }
}