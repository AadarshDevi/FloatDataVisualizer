package com.alphagen.studio.FloatDataVisualizer.datareciever;

import com.alphagen.studio.FloatDataVisualizer.data.Constants;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataReceiver implements Runnable {

    private static DataReceiver dr;
    private final DataKeeper dataKeeper;
    private final SerialPort serialPort;

    public DataReceiver(DataKeeper dataKeeper) {

        System.out.println("DATA: Serial Connection >");

        this.dataKeeper = dataKeeper;
        SerialPort[] serialPorts = SerialPort.getCommPorts();

        if (serialPorts.length == 0) {
            System.err.println("ERROR: Serial Ports are empty.");
            System.exit(0);
        }

        if (Constants.SERIAL_COMM_PORT == null) {
            System.err.println("ERROR: Serial Comm Port is null");
            System.exit(0);
        }

        SerialPort serialPort = SerialPort.getCommPort(Constants.SERIAL_COMM_PORT);
        boolean success = serialPort.openPort();
        serialPort.closePort();
        if (!success) {
            System.out.println();
            System.err.println("ERROR: Serial Port does not exist.");
            System.err.println("FIX: \"commPort\" in settings.txt");
            System.out.println("List of Comm Ports:");
            for (int i = 0; i < serialPorts.length; i++) {
                System.out.printf("\t%2d : %s", i, serialPorts[i].getSystemPortName());
            }
            System.exit(0);
        }

        this.serialPort = serialPort;

        this.serialPort.setBaudRate(Constants.BAUD_RATE);
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        boolean connectionEstablished = this.serialPort.openPort();
        System.out.println("\tLOG: Connection > " + connectionEstablished);
        if (!connectionEstablished) {
            System.err.println("ERROR: COM Port Connection ERROR");
            System.exit(0);
        }

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

                if ((Constants.START_DATA_TRANSFER == null) || (Constants.END_DATA_TRANSFER == null)) {
                    System.err.println("ERROR: Data Transfer Flag(s) are null");
                    System.exit(0);
                }

                if (dataline.equals(Constants.START_DATA_TRANSFER)) {
                    System.out.println(dataline);
                    System.out.println();
                    startDataTransfer = true;
                } else if (dataline.equals(Constants.END_DATA_TRANSFER)) {
                    System.out.println();
                    System.out.println(dataline);
                    endDataTransfer = true;
                    bufferedReader.close();
                    break;
                }

                if ((dataline != null) && startDataTransfer && !endDataTransfer && dataline.startsWith(Constants.TEAM_DATA)) {
                    System.out.println(dataline);
                    dataKeeper.writeData(dataline);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        serialPort.closePort();
        System.out.println("LOG: Float Data Recorder has stopped receiving data.");
    }

    public boolean isRunning() {
        return serialPort.isOpen();
    }
}