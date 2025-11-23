package com.alphagen.studio.FloatDataVisualizer.filepaths;

public class DataPathFactory {
    private static DataPathFactory dataPathFactory;
    private final DataPath dataPath;

    private DataPathFactory(String basepath) {
        dataPath = new DataPath(basepath);
    }

    public static void generate(String basepath) {
        if (dataPathFactory == null) dataPathFactory = new DataPathFactory(basepath);
    }

    public static DataPathFactory getFilePathFactory() {
        return dataPathFactory;
    }

    public DataPath getFilePath() {
        return dataPath;
    }
}
