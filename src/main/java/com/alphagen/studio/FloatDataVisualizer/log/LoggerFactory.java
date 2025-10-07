package com.alphagen.studio.FloatDataVisualizer.log;

public class LoggerFactory {
    public static Logger getLogger(Object obj) {
        return new Logger(obj);
    }
}
