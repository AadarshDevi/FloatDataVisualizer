package com.alphagen.studio.FloatDataVisualizer.log;

import com.alphagen.studio.FloatDataVisualizer.Launcher;
import com.alphagen.studio.FloatDataVisualizer.data.DataKeeper;
import com.alphagen.studio.FloatDataVisualizer.data.DataPointRecord;

import javax.swing.*;
import java.util.ArrayList;

public interface Exitter {

    String NO_ERROR = "NO_ERROR";

    default void exit(String errorMessage) {

//        Launcher.killDataKeeper();
//        Launcher.killDataReceiver();

        try {
            Launcher.getDataKeeperThread().join();
            Launcher.getDataReceiverThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<DataPointRecord> dprs = DataKeeper.getInstance().getAllData();
        for (DataPointRecord dpr : dprs) {
            dpr = null;
        }
        System.gc();

        try {
            if (!errorMessage.equals(NO_ERROR)) {
                System.err.println("ERROR: " + errorMessage);
                Thread.sleep(10);
                JOptionPane.showMessageDialog(null, errorMessage, "Exitter Exception", JOptionPane.ERROR_MESSAGE);
            }
            Thread.sleep(10);
            System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

