package com.alphagen.studio.FloatDataVisualizer.log;

public class LoggerFactory {
    private static Logger logger;
    public static Logger getLogger(Object obj) {
        if(logger==null)logger = new Logger(obj);
        return logger;
    }
}
