package com.alphagen.studio.FloatDataVisualizer;

import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.data.Settings;
import com.alphagen.studio.FloatDataVisualizer.datareciever.DataReceiver;
import javafx.application.Application;


public class Launcher {

    static Thread dkt;
    static Thread drt;

    public static void main(String[] args) {

        Settings setting = Settings.getInstance();
        setting.readSettings();

        DataKeeper dataKeeper = DataKeeper.getInstance();
        dkt = new Thread(dataKeeper);
        dkt.start();

        DataReceiver dataReceiver = DataReceiver.getInstance();
        drt = new Thread(dataReceiver);
        drt.start();

        Application.launch(Main.class, args);
    }

    public static void killDataReceiver() {
        if (drt.isAlive()) System.err.println("ERROR: Data Receiver is still running");
        else System.out.println("LOG: Closed > Data Receiver");
    }

    public static boolean isDataReceiverAlive() {
        return drt.isAlive();
    }

    public static void killDataKeeper() {
        DataKeeper.getInstance().stop();
        if (dkt.isAlive()) System.err.println("ERROR: Data Keeper is still running");
        else System.out.println("LOG: Closed > Data Keeper");
    }

    public static enum Platform {
        WIN11, MACOS
    }
}
