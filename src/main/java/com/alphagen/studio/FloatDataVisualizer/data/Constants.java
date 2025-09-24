package com.alphagen.studio.FloatDataVisualizer.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {

    // DataReceiver
    public static int BAUD_RATE;
    public static String SERIAL_COMM_PORT;
    public static String TEAM_DATA;
    public static String START_DATA_TRANSFER;
    public static String END_DATA_TRANSFER;

    // DataPlotter
    public static String DATA_GROUP_NAME;

    public static void readSettings() {

        System.out.println("DATA: Settings >");


        // Windows Build
//        String user = System.getProperty("user.name");
//        String filepath = "C:/Users/" + user + "/OneDrive/Desktop/settings.txt";

        try {
            Properties properties = new Properties();
            InputStream inputStream = Constants.class.getResourceAsStream("/com/alphagen/studio/FloatDataVisualizer/settings.txt");

            if (inputStream == null) {
                System.err.println("\tERROR: settings.txt resource not found");
                System.exit(0);
            }

            properties.load(inputStream);

            // Baud Rate
            BAUD_RATE = Integer.parseInt(properties.getProperty("baudRate"));

            int[] baudRates = {
                    300, 600, 750, 1200,
                    2400, 4800, 9600, 19200,
                    31250, 38400, 57600, 74880,
                    115200, 230400, 250000, 460800,
                    50000, 921600, 1000000, 2000000
            };

            boolean baudRateExists = false;
            for (int listedBaudRate : baudRates) {
                if (listedBaudRate == BAUD_RATE) {
                    baudRateExists = true;
                    break;
                }
            }

            if (!baudRateExists) {
                System.err.println("\tERROR: Baud Rate does not exist > " + BAUD_RATE);
                System.exit(0);
            }

            // SETV/SETX > Set Variable
            System.out.println("\tSETV: Baud Rate > " + BAUD_RATE);

            // Serial Comm Port
            SERIAL_COMM_PORT = properties.getProperty("commPort");
            System.out.println("\tSETV: Comm Port > " + SERIAL_COMM_PORT);

            // Packet Data
            TEAM_DATA = properties.getProperty("packetData");
            System.out.println("\tSETV: Comm Port > " + TEAM_DATA);

            START_DATA_TRANSFER = properties.getProperty("startDataTransfer");
            if (START_DATA_TRANSFER == null || START_DATA_TRANSFER.isEmpty()) {
                System.err.println("\tERROR: Start Data Transfer Flag in null");
            } else System.out.println("\tSETV: Start Data Transfer Flag > " + START_DATA_TRANSFER);

            END_DATA_TRANSFER = properties.getProperty("endDataTransfer");
            if (END_DATA_TRANSFER == null || END_DATA_TRANSFER.isEmpty()) {
                System.err.println("\tERROR: Start Data Transfer Flag in null");
            } else System.out.println("\tSETV: END Data Transfer Flag > " + END_DATA_TRANSFER);

            DATA_GROUP_NAME = properties.getProperty("dataGroupName");
            System.out.println("\tSETV: Data Group Name > " + DATA_GROUP_NAME);

            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
