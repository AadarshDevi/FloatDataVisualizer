package com.alphagen.studio.FloatDataVisualizer.filepaths;

import com.alphagen.studio.FloatDataVisualizer.log.Exitter;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FilePath implements Exitter {

    public final String basepath;
    public final String screenshot_scatterplot;
    public final String screenshot_tableview;
    public final String settingspath;
    public final String logpath;
    public final String csvpath;

    public FilePath(String basepath) {
        this.basepath = basepath;

        try {
            generateFolder("Base Folder", basepath);
        } catch (IOException e) {
            exit("Unable to generate base folder:\n" + basepath);
        }

        // settings filepath
        settingspath = this.basepath + "/settings.txt";
        // TODO: generateSaveFile(settingspath)
        try {
            generateFile(settingspath);
//            writeSave(settingsfile);
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

        csvpath = this.basepath + "/data/";
        try {
            generateFolder("CSV", logpath);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to generate Data(CSV) folder");
            JOptionPane.showMessageDialog(null, "Unable to generate Data(CSV) folder.", "FilePath Exception", JOptionPane.ERROR_MESSAGE);
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

    private File generateFile(String filepath) throws IOException {
        File file = new File(filepath);
        System.out.println("LOG: " + file.getName() + " File exists");
        if (file.exists() && file.isFile()) return file;
        boolean success = file.createNewFile();
        System.out.println("Generated: " + file.getName() + " File > " + success);
        if (!success) {
            JOptionPane.showMessageDialog(null, "Unable to generate file:\n" + filepath, "FilePath Exception", JOptionPane.ERROR_MESSAGE);
            throw new IOException();
        }
        return file;
    }

    private boolean generateFolder(String folder, String folderpath) throws IOException {
        File directory = new File(folderpath);
        if (directory.exists() && directory.isDirectory()) return true;
        boolean directoryCreated = directory.mkdirs();
        System.out.println("Generated: " + folder + " Folders > " + directoryCreated);
        if (!directoryCreated) {
            JOptionPane.showMessageDialog(null, "Unable to generate folder:\n" + folderpath, "FilePath Exception", JOptionPane.ERROR_MESSAGE);
            throw new IOException();
        }
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
}
