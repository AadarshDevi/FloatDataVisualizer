package com.alphagen.studio.FloatDataVisualizer.log;

import javax.swing.*;

public interface Exitter {

    default void exit(String errorMessage) {
        try {
            System.err.println("ERROR: " + errorMessage);
            Thread.sleep(10);
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);

            // TODO: Create Custom Popup

            Thread.sleep(10);
            System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}