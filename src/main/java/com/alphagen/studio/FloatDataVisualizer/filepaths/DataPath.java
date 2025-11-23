package com.alphagen.studio.FloatDataVisualizer.filepaths;

import com.alphagen.studio.FloatDataVisualizer.log.Exitter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class DataPath implements Exitter {

    public final String basepath;
    public final String screenshot_scatterplot;
    public final String screenshot_tableview;
    public final String settingspath;
    public final String logpath;
    public final String csvpath;

    public DataPath(String basepath) {
        this.basepath = basepath;

        System.out.println("DATA: DataPath >");

        try {
            generateFolder("Base Folder", basepath);
        } catch (IOException e) {
            exit("Unable to generate base folder:\n" + basepath);
        }

        // settings filepath
        settingspath = this.basepath + "/settings.txt";
        try {
            generateFile("Settings", settingspath);
        } catch (IOException e) {
            exit("Unable to generate settings.txt and cannot find filepath:\n" + settingspath);
        }

        // scatterchart filepath
        // table filepath
        screenshot_scatterplot = this.basepath + "/screenshot/scatterchart/";
        screenshot_tableview = this.basepath + "/screenshot/table/";

        // screenshot folder
        // scatterchart folder
        // table folder
        try {
            generateFolder("ScatterChart Screenshots", screenshot_scatterplot);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to generate ScatterChart Screenshots folder");
            JOptionPane.showMessageDialog(null, "Unable to generate ScatterChart Screenshots folder.", "FilePath Exception", JOptionPane.ERROR_MESSAGE);
        }

        try {
            generateFolder("Data Table Screenshots", screenshot_tableview);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to generate Data Table Screenshots folder");
            JOptionPane.showMessageDialog(null, "Unable to generate Data Table Screenshots folder.", "FilePath Exception", JOptionPane.ERROR_MESSAGE);
        }

        // logpath
        // log folder
        logpath = this.basepath + "/logs/";
        try {
            generateFolder("Logs", logpath);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to generate Log folder");
            JOptionPane.showMessageDialog(null, "Unable to generate Log folder.", "FilePath Exception", JOptionPane.ERROR_MESSAGE);
        }

        // data/csv folder
        csvpath = this.basepath + "/data/";
        try {
            generateFolder("CSV/Data", csvpath);
        } catch (IOException e) {
            exit("Unable to generate Data(CSV) folder.");
        }
    }

    public boolean writeSave(File settingsfile) {
        if (settingsfile.length() == 0) return true;

//        var let = """
//        # Visualizer Data
//        commPort=COM3
//        baudRate=115200
//        packetData=PN12-MiramarWaterJets,pkt-,time,unit2
//        time_unit=s
//        unit2_name=depth
//        unit2_unit=m/s
//        startDataTransfer=--start-data-transfer
//        endDataTransfer=--end-data-transfer
//        dataGroupName=Profile
//
//        # Project Data
//        projectVersion=2.1.2.0
//        releaseVersion=1.2.0
//        platform=win11
//        """;
//
//        try(PrintWriter printWriter = new PrintWriter(new FileWriter(settingsfile))) {
//
//        }

        return true;
    }

    private File generateFile(String filename, String filepath) throws IOException {
        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            System.out.println("\tSETF: Retrieved File > " + filename);
            return file;
        }
        boolean success = file.createNewFile();
//        System.out.println("Generated: " + file.getName() + " File > " + success);
        if (!success) {
            JOptionPane.showMessageDialog(null, "Unable to generate file:\n" + filepath, "FilePath Exception", JOptionPane.ERROR_MESSAGE);
            System.err.println("\tEXCF: Unable tp Generated File > " + filename);
            throw new IOException();
        }
        System.out.println("\tSETF: Generated File > " + filename);
        return file;
    }

    private boolean generateFolder(String foldername, String folderpath) throws IOException {
        File directory = new File(folderpath);
        if (directory.exists() && directory.isDirectory()) {
            System.out.println("\tSETF: Folder > " + foldername);
            return true;
        }
        boolean directoryCreated = directory.mkdirs();
        if (!directoryCreated) {
            JOptionPane.showMessageDialog(null, "Unable to generate folder:\n" + folderpath, "FilePath Exception", JOptionPane.ERROR_MESSAGE);
            System.out.println("\tEXCF: Unable to Generate Folder > " + foldername);
            throw new IOException();
        }
        System.out.println("\tSETF: Folder > " + foldername);
        return directoryCreated;
    }

    public String getSettingsPath() {
        return settingspath;
    }

    public String getScatterPath() {
        return screenshot_scatterplot;
    }

    public String getTablePath() {
        return screenshot_tableview;
    }

    public String getCSVPath() {
        return csvpath;
    }

    public void generateCSVFolder() {
    }
}
