package com.alphagen.studio.FloatDataVisualizer.filepaths;

import com.alphagen.studio.FloatDataVisualizer.log.Exitter;

import java.io.File;
import java.io.IOException;

public class FilePath implements Exitter {

    public final String basepath;
    public final String screenshot_scatterplot;
    public final String screenshot_tableview;
    public final String settingspath;
    public final String logpath;

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
        }

        try {
            generateFolder("Data Table Screenshots", screenshot_tableview);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to generate Data Table Screenshots folder");
        }

        // logpath
        // log folder
        logpath = this.basepath + "/logs/";
        try {
            generateFolder("Logs", logpath);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to generate Log folder");
        }

    }

    public boolean writeSave(File settingsfile) {
        if (settingsfile.length() == 0) return true;


        return true;
    }

    private File generateFile(String filepath) throws IOException {
        File file = new File(filepath);
        System.out.println("LOG: " + file.getName() + " File exists");
        if (file.exists() && file.isFile()) return file;
        boolean success = file.createNewFile();
        System.out.println("Generated: " + file.getName() + " File > " + success);
        if (!success) throw new IOException();
        return file;
    }

    private boolean generateFolder(String folder, String folderpath) throws IOException {
        File directory = new File(folderpath);
        if (directory.exists() && directory.isDirectory()) return true;
        boolean directoryCreated = directory.mkdirs();
        System.out.println("Generated: " + folder + " Folders > " + directoryCreated);
        if (!directoryCreated) throw new IOException();
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


}
