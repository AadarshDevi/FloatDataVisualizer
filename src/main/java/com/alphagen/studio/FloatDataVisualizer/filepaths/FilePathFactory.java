package com.alphagen.studio.FloatDataVisualizer.filepaths;

public class FilePathFactory {
    private static FilePathFactory filePathFactory;
    private final FilePath filePath;

    private FilePathFactory(String basepath) {
        filePath = new FilePath(basepath);
    }

    public static void generate(String basepath) {
        if (filePathFactory == null) filePathFactory = new FilePathFactory(basepath);
    }

    public static FilePathFactory getFilePathFactory() {
        return filePathFactory;
    }

    public FilePath getFilePath() {
        return filePath;
    }
}
