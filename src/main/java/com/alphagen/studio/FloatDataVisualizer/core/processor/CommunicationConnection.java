package com.alphagen.studio.FloatDataVisualizer.core.processor;


public interface CommunicationConnection extends Runnable {
    boolean isOpen();

    boolean isConnected();

    boolean isPortAvailable();
}
