package com.alphagen.studio.FloatDataVisualizer.core.system.threader;


import com.alphagen.studio.FloatDataVisualizer.core.processor.CommunicationConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionManager extends RunnableManager {
    private static final Logger logger = LogManager.getLogger(ConnectionManager.class);

    public ConnectionManager(CommunicationConnection connection) {
        super(connection);
    }

    public synchronized boolean isOpen() {
        return getRunnable().isOpen();
    }

    private CommunicationConnection getRunnable() {
        return (CommunicationConnection) runnable;
    }

    public synchronized boolean isConnected() {
        return getRunnable().isConnected();
    }

    public synchronized void restart() {
        stop();
        start();
    }

    public boolean isPortOpen() {
        return getRunnable().isPortAvailable();
    }

}
