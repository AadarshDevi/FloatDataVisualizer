package com.alphagen.studio.FloatDataVisualizer.log;

import javax.swing.*;

public interface Exitter {

    String NO_ERROR = "NO_ERROR";

    default void exit(String errorMessage) {
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

