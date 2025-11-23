package com.alphagen.studio.FloatDataVisualizer;

import com.alphagen.studio.FloatDataVisualizer.data.DataConfigurator;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.datareciever.DataReceiver;
import javafx.application.Application;

import javax.swing.*;


public class Launcher {

    static Thread dkt;
    static Thread drt;

    public static void main(String[] args) {

        DataConfigurator setting = DataConfigurator.getInstance();
        setting.readSettings();

        DataKeeper dataKeeper = DataKeeper.getInstance();
        dkt = new Thread(dataKeeper);
        dkt.setName("DataKeeper");
        dkt.start();

        DataReceiver dataReceiver = DataReceiver.getInstance();
        drt = new Thread(dataReceiver);
        drt.setName("DataReceiver");
        drt.start();

        Application.launch(Main.class, args);
    }

    public static void killDataReceiver() {
        drt.interrupt();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (drt.isAlive()) {
            JOptionPane.showMessageDialog(null, "Unable to kill DataReceiver", "Launcher Exception", JOptionPane.ERROR_MESSAGE);
            System.err.println("ERROR: Data Receiver is still running");
        } else System.out.println("LOG: Closed > Data Receiver");
    }

    public static boolean isDataReceiverAlive() {
        return drt.isAlive();
    }

    public static void killDataKeeper() {
//        DataKeeper.getInstance().stop();
        dkt.interrupt();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (dkt.isAlive()) {
            JOptionPane.showMessageDialog(null, "Unable to kill DataKeeper", "Launcher Exception", JOptionPane.ERROR_MESSAGE);
            System.err.println("ERROR: Data Keeper is still running");
        } else System.out.println("LOG: Closed > Data Keeper");
    }

    public static Thread getDataKeeperThread() {
        return dkt;
    }

    public static Thread getDataReceiverThread() {
        return drt;
    }

    // Platform Obj in Settings
    public enum Platform {
        WIN11,
        MACOS,
        LINUX
    }
}
