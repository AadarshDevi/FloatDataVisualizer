package com.alphagen.studio.FloatDataVisualizer.data;

import com.alphagen.studio.FloatDataVisualizer.Launcher;
import com.alphagen.studio.FloatDataVisualizer.filepaths.DataPath;
import com.alphagen.studio.FloatDataVisualizer.filepaths.DataPathFactory;
import com.alphagen.studio.FloatDataVisualizer.log.Exitter;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class DataConfigurator implements Exitter {

    // Debugging
    public static final boolean DEBUG = true;

    // Other Vars
    public static final Launcher.Platform PLATFORM = Launcher.Platform.MACOS;
    private static DataConfigurator dataConfigurator;
    public final DataPath dataPath;

    // Application
    private final String[] PLATFORMS = {"win10", "win11", "macos", "linux"};
    private final String INTERNAL_PROJECT_VERSION = "2.1.1.9";
    private final String RELEASE_PROJECT_VERSION = "1.1.9";
    private final SerialPort[] serialPorts;

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
    public int INDEX_UNIT2;
    public String UNIT2_NAME;
    public String UNIT2_UNIT;
    public String TIME_UNIT;

    // DataWriter
//    public boolean WRITE_CSV;
//    public boolean WRITE_RAW;


    // SETV/SETX > Set Variable

    public DataConfigurator() {

        String user = System.getProperty("user.name");
        System.out.println("LOG: User > " + user);

        String basepath = switch (PLATFORM) {
            case WIN11 ->
                    "C:/Users/" + user + "/AppData/Local/miramarwaterjets/FloatDataVisualizer/" + RELEASE_PROJECT_VERSION;
            case MACOS ->
                    "/Users/" + user + "/Applications/miramarwaterjets/FloatDataVisualizer/" + RELEASE_PROJECT_VERSION;
            default -> null;
        };

        if (user == null) exit("Unable to get User's Platform/OS and Username.");
        if (basepath.isBlank()) exit("Datapath of settings.txt is null.");

        DataPathFactory.generate(basepath);
        dataPath = DataPathFactory.getFilePathFactory().getFilePath();

        File settingsFile = new File(dataPath.getSettingsPath());

        if ((settingsFile.exists() && settingsFile.isFile() && (settingsFile.length() == 0)) || !settingsFile.exists())
            generateSettingsFile();

        serialPorts = SerialPort.getCommPorts();
    }

    public static DataConfigurator getInstance() {
        if (dataConfigurator == null) dataConfigurator = new DataConfigurator();
        return dataConfigurator;
    }

    private void generateSettingsFile() {

        try (PrintWriter printWriter = new PrintWriter(dataPath.getSettingsPath())) {
            printWriter.println("# Visualizer Data");
            String portName = switch (PLATFORM) {
                case WIN11 -> "COM3";
                case MACOS -> "/dev/cu.usbmodem14101";
                case LINUX -> null;
            };
            printWriter.println("commPort=" + portName);
            printWriter.println("baudRate=115200");
            printWriter.println("packetData=PN12-MiramarWaterJets,pkt-,time,unit2");
            printWriter.println("time_unit=s");
            printWriter.println("unit2_name=depth");
            printWriter.println("unit2_unit=m");
            printWriter.println("startDataTransfer=--start-data-transfer");
            printWriter.println("endDataTransfer=--end-data-transfer");
            printWriter.println("dataGroupName=Profile");
            printWriter.println("");
            printWriter.println("# Project Data");
            printWriter.println("projectVersion=" + INTERNAL_PROJECT_VERSION);
            printWriter.println("releaseVersion=" + RELEASE_PROJECT_VERSION);
            printWriter.println("platform=" + PLATFORM.toString().toLowerCase());
//            printWriter.println("csv=" + false);
//            printWriter.println("raw=" + false);
        } catch (FileNotFoundException e) {
            exit("Unable to find settings.txt");
        }


    }

    public void readSettings() {

        System.out.println("DATA: DataConfigurator >");

        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(dataPath.getSettingsPath());  // Settings.class.getResourceAsStream(datapath);
//            InputStream inputStream = new FileInputStream(datapath);  // Settings.class.getResourceAsStream(datapath);

            if (inputStream == null) exit("settings.txt resource not found");

            properties.load(inputStream);

            // Project Version > For settings.txt
            String projectVersion = properties.getProperty("projectVersion");
//            System.out.println("SetX: " + projectVersion + ", ProjectVal: " + INTERNAL_PROJECT_VERSION);
            if (projectVersion == null) exit("Project Version does not exist");
            else if (!projectVersion.equals(INTERNAL_PROJECT_VERSION))
                exit("Project Version does not match Application Version.");
            System.out.println("\tSETV: Internal Version > " + projectVersion);
            Thread.sleep(50);

            // Release Version > For settings.txt
            String releaseVersion = properties.getProperty("releaseVersion");
            if (releaseVersion == null) exit("Release Version does not exist.");
            else if (!releaseVersion.equals(RELEASE_PROJECT_VERSION))
                exit("Release Version does not match Application Version");
            System.out.println("\tSETV: Release Version > " + releaseVersion);
            Thread.sleep(50);

            // Platform
            String platform = properties.getProperty("platform");
            if (platform == null) exit("Platform is null.");
            if (!PLATFORM.toString().toLowerCase().equalsIgnoreCase(platform))
                exit("Wrong App. Please use " + platform + " version of the app.");
            else System.out.println("\tSETV: App Platform > " + platform);

            boolean validPlatform = false;
            for (String string : PLATFORMS) {
                if (string.equals(platform)) {
                    validPlatform = true;
                    break;
                }
            }
            if (!validPlatform) exit("settings.txt is in the wrong platform");
            System.out.println("\tSETV: Platform > " + platform);
            Thread.sleep(50);

            // Baud Rate
            // FIXME: BaudRate must be a number
            //

//            else
            try {
                String baudRateString = properties.getProperty("baudRate");
                if (baudRateString == null || baudRateString.isBlank()) exit("Baud Rate is EMPTY.");
                BAUD_RATE = Integer.parseInt(baudRateString);
            } catch (NumberFormatException e) {
                exit("baudRate is not a number");
            }
            System.out.println("\tSETV: Baud Rate > " + BAUD_RATE);
            Thread.sleep(50);

            SERIAL_COMM_PORT = properties.getProperty("commPort");
            if (SERIAL_COMM_PORT == null || SERIAL_COMM_PORT.isBlank()) {
                // TODO: add joptionpane to show all comm ports
                showSerialCommPorts();
                exit("Serial Comm Port is null");
            }
            System.out.println("\tSETV: Comm Port > " + SERIAL_COMM_PORT);
            Thread.sleep(50);

            // Packet Data
            String packetFormat = properties.getProperty("packetData");
            if (packetFormat == null || packetFormat.isBlank()) {
                exit("Packet Data is null or Does not exist.");
            }
            if (!packetFormat.contains("time") || !packetFormat.contains("unit2")) {
                exit("\"time\" or \"unit2\" not found in \"packetData\".");
            }
            String[] rawPacketData = packetFormat.split(",");
            TEAM_DATA = rawPacketData[0];
            PACKET_NAME = rawPacketData[1].substring(0, rawPacketData[1].indexOf("-"));
            if (rawPacketData[2].equals("time")) {
                INDEX_TIME = 2;
//                INDEX_DEPTH = 3;
                INDEX_UNIT2 = 3;
            } else {
                INDEX_UNIT2 = 2;
//                INDEX_DEPTH = 2;
                INDEX_TIME = 3;
            }
            Thread.sleep(50);

            TIME_UNIT = properties.getProperty("time_unit");
            if (TIME_UNIT == null || TIME_UNIT.isBlank()) {
                exit("Time Unit is EMPTY");
            }
            System.out.println("\tSETV: Time Unit > " + TIME_UNIT);
            Thread.sleep(50);

            UNIT2_NAME = properties.getProperty("unit2_name");
            if (UNIT2_NAME == null || UNIT2_NAME.isBlank()) {
                exit("Unit 2 Name in null");
            }
            System.out.println("\tSETV: Unit 2 Name > " + UNIT2_NAME);
            Thread.sleep(50);

            UNIT2_UNIT = properties.getProperty("unit2_unit");
            if (UNIT2_UNIT == null || UNIT2_UNIT.isBlank()) {
                exit("Unit 2 Unit in null");
            }
            System.out.println("\tSETV: Unit 2 Unit > " + UNIT2_UNIT);
            Thread.sleep(50);

            // Start Data Transfer Flag
            START_DATA_TRANSFER = properties.getProperty("startDataTransfer");
            if (START_DATA_TRANSFER == null || START_DATA_TRANSFER.isBlank()) {
                exit("Start Data Transfer Flag in null");
            }
            System.out.println("\tSETV: Start Data Transfer Flag > " + START_DATA_TRANSFER);
            Thread.sleep(50);

            // End Data Transfer Flag
            END_DATA_TRANSFER = properties.getProperty("endDataTransfer");
            if (END_DATA_TRANSFER == null || END_DATA_TRANSFER.isBlank()) {
                exit("End Data Transfer Flag in null");
            }
            System.out.println("\tSETV: END Data Transfer Flag > " + END_DATA_TRANSFER);
            Thread.sleep(50);

            // Data Group Name
            DATA_GROUP_NAME = properties.getProperty("dataGroupName");
            if (DATA_GROUP_NAME == null || DATA_GROUP_NAME.isBlank()) {
                exit("Data Group Name in null");
            }
            System.out.println("\tSETV: Data Group Name > " + DATA_GROUP_NAME);
            Thread.sleep(50);

//            // Write CSV File
//            String stringCSVWrite = properties.getProperty("csv");
//            if (stringCSVWrite == null || stringCSVWrite.isBlank()) {
//                WRITE_CSV = false;
//            }
//            WRITE_CSV = Boolean.parseBoolean(stringCSVWrite);
//            System.out.println("\tSETV: Write CSV File > " + WRITE_CSV);
//            Thread.sleep(50);
//
//            // Write CSV File
//            String stringRAWWrite = properties.getProperty("raw");
//            if (stringRAWWrite == null || stringRAWWrite.isBlank()) {
//                WRITE_RAW = false;
//            }
//            WRITE_CSV = Boolean.parseBoolean(stringRAWWrite);
//            System.out.println("\tSETV: Write Raw Data in CSV > " + WRITE_CSV);
//            Thread.sleep(50);

            // Serial Comm Port
            if (serialPorts.length == 0) {
                exit("Serial Ports are empty.");
            }

            System.out.println();
        } catch (IOException e) {
            exit("Unable to parse Settings, Properties and InputStream");
        } catch (InterruptedException e) {
            exit("Thread Interrupted in Settings");
        } catch (SerialPortInvalidPortException e) {
            exit("Serial Comm Ports connected to something else.");
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

//    public int getDepthIndex() {
//        return INDEX_DEPTH;
//    }

    public int getUNIT2Index() {
        return INDEX_UNIT2;
    }

    public void showSerialCommPorts() {
        System.out.println();
        System.err.println("ERROR: Serial Port does not exist.");
        JOptionPane.showMessageDialog(null, "Serial Port does not exist.", "Settings Exception", JOptionPane.ERROR_MESSAGE);
        System.err.println("FIX: \"commPort\" in settings.txt");
        JOptionPane.showMessageDialog(null, "Fix \"commPort\" in settings.txt.", "Settings Info", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("List of Comm Ports:");
        for (int i = 0; i < serialPorts.length; i++) {
            System.out.printf("\t%2d : %s", i, serialPorts[i].getSystemPortName());
        }
    }
}
