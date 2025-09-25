package com.alphagen.studio.FloatDataVisualizer.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Constants {

    private static Constants constants;
    // Application
    private final String[] PLATFORMS = {"win10", "win11", "macOS", "linux"};
    @SuppressWarnings("unused")
    private final String INTERNAL_PROJECT_VERSION = "2.1.0";
    private final String RELEASE_PROJECT_VERSION = "1.1.0";
    // DataPlotter
    public String DATA_GROUP_NAME;
    // DataReceiver
    public int BAUD_RATE;
    public String SERIAL_COMM_PORT;
    public String START_DATA_TRANSFER;
    public String END_DATA_TRANSFER;
    // DataKeeper
    public String TEAM_DATA;
    public String PACKET_NAME;
    public int INDEX_TIME;
    public int INDEX_DEPTH;

    // SETV/SETX > Set Variable

    public Constants() {
    }

    public static Constants getInstance() {
        if (constants == null) constants = new Constants();
        return constants;
    }

    public void readSettings() {

        System.out.println("DATA: Settings >");

        try {
            Properties properties = new Properties();
            InputStream inputStream = Constants.class.getResourceAsStream("/com/alphagen/studio/FloatDataVisualizer/settings.txt");

            if (inputStream == null) {
                System.err.println("\tERROR: settings.txt resource not found");
                exit();
                System.exit(0);
            }

            properties.load(inputStream);

            // Project Version > For settings.txt
            String projectVersion = properties.getProperty("projectVersion");
            if (projectVersion == null) {
                System.err.println("ERROR: Project Version does not exist.");
                exit();
                System.exit(0);
            } else if (!projectVersion.equals(INTERNAL_PROJECT_VERSION)) {
                System.err.println("ERROR: Project Version does not match Application Version.");
                exit();
                System.exit(0);
            }
            System.out.println("\tSETV: Internal Version > " + projectVersion);
            Thread.sleep(50);

            // Release Version > For settings.txt
            String releaseVersion = properties.getProperty("releaseVersion");
            if (releaseVersion == null) {
                System.err.println("ERROR: Project Version does not exist.");
                exit();
                System.exit(0);
            } else if (!releaseVersion.equals(RELEASE_PROJECT_VERSION)) {
                System.err.println("ERROR: Release Version does not match Application Version.");
                exit();
                System.exit(0);
            }
            System.out.println("\tSETV: Release Version > " + releaseVersion);
            Thread.sleep(50);

            // Platform
            String platform = properties.getProperty("platform");
            if (platform == null) {
                System.err.println("ERROR: Platform is null.");
                exit();
                System.exit(0);
            }
            boolean validPlatform = false;
            for (String string : PLATFORMS) {
                if (string.equals(platform)) {
                    validPlatform = true;
                    break;
                }
            }
            if (!validPlatform) {
                System.err.println("ERROR: settings.txt is in the wrong platform");
                exit();
                System.exit(0);
            }
            System.out.println("\tSETV: Platform > " + platform);
            Thread.sleep(50);

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
                exit();
                System.exit(0);
            }
            System.out.println("\tSETV: Baud Rate > " + BAUD_RATE);
            Thread.sleep(50);

            // Serial Comm Port
            SERIAL_COMM_PORT = properties.getProperty("commPort");
            if (SERIAL_COMM_PORT == null || SERIAL_COMM_PORT.isBlank()) {
                System.err.println("ERROR: Serial Comm Port is null");
                exit();
                System.exit(0);
            } else System.out.println("\tSETV: Comm Port > " + SERIAL_COMM_PORT);
            Thread.sleep(50);

            // Packet Data
            String packetFormat = properties.getProperty("packetData");
            if (packetFormat == null || packetFormat.isBlank()) {
                System.err.println("\tERROR: Packet Data is null or Does not exist.");
                exit();
                System.exit(0);
            }
            if (!packetFormat.contains("time") || !packetFormat.contains("depth")) {
                System.err.println("\tERROR: \"time\" or \"depth\" not found in \"packetData\".");
                exit();
                System.exit(0);
            }
            String[] rawPacketData = packetFormat.split(",");
            TEAM_DATA = rawPacketData[0];
            PACKET_NAME = rawPacketData[1].substring(0, rawPacketData[1].indexOf("-"));
            if (rawPacketData[2].equals("time")) {
                INDEX_TIME = 2;
                INDEX_DEPTH = 3;
            } else {
                INDEX_DEPTH = 2;
                INDEX_TIME = 3;
            }
            Thread.sleep(50);

            // Start Data Transfer Flag
            START_DATA_TRANSFER = properties.getProperty("startDataTransfer");
            if (START_DATA_TRANSFER == null || START_DATA_TRANSFER.isBlank()) {
                System.err.println("\tERROR: Start Data Transfer Flag in null");
                exit();
                System.exit(0);
            } else System.out.println("\tSETV: Start Data Transfer Flag > " + START_DATA_TRANSFER);
            Thread.sleep(50);

            // End Data Transfer Flag
            END_DATA_TRANSFER = properties.getProperty("endDataTransfer");
            if (END_DATA_TRANSFER == null || END_DATA_TRANSFER.isBlank()) {
                System.err.println("\tERROR: Start Data Transfer Flag in null");
                exit();
                System.exit(0);
            } else System.out.println("\tSETV: END Data Transfer Flag > " + END_DATA_TRANSFER);
            Thread.sleep(50);

            // Data Group Name
            DATA_GROUP_NAME = properties.getProperty("dataGroupName");
            if (DATA_GROUP_NAME == null || DATA_GROUP_NAME.isBlank()) {
                System.err.println("\tERROR: Start Data Transfer Flag in null");
                exit();
                System.exit(0);
            } else System.out.println("\tSETV: Data Group Name > " + DATA_GROUP_NAME);
            Thread.sleep(50);

            System.out.println();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to parse Settings, Properties and InputStream");
        } catch (InterruptedException e) {
            System.err.println("ERROR: Thread Interrupted in Constants");
        }
    }

    public String getSerialCommPort() {
        return SERIAL_COMM_PORT;
    }

    public int getBaudRate() {
        return BAUD_RATE;
    }

    public String getStartFlag() {
        return START_DATA_TRANSFER;
    }

    public String getEndFlag() {
        return END_DATA_TRANSFER;
    }

    public String getTeamData() {
        return TEAM_DATA;
    }

    public String getDataGroupName() {
        return DATA_GROUP_NAME;
    }

    public String getReleaseVersion() {
        return RELEASE_PROJECT_VERSION;
    }

    public String getPacketName() {
        return PACKET_NAME;
    }

    public int getTimeIndex() {
        return INDEX_TIME;
    }

    public int getDepthIndex() {
        return INDEX_DEPTH;
    }

    public void exit() {
        try {
            Thread.sleep(10);
            System.out.print("\n[Press Enter to Exit] > ");
            new Scanner(System.in).nextLine();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
