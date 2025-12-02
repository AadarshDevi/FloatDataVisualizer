# 2025-2026 Mate Float's Data Visualizer

This repository is for the releases of the Float Data Recorder. To know the technology used, check out
_**Project Information**_ below.

# Dev Branch

# Table of Contents / Documentation

### 1. [Project Information](Documentation/ProjectInformation.md)

### 2. [Download](Documentation/Download.md)

### 3. [Future Updates](Documentation/FutureUpdates.md)

### 4. [Project Settings](Documentation/Settings.md)

### 5. [Transferring Data](Documentation/DataTransfer.md)

### 6. [Project Platforms](Documentation/Platforms.md)

### 7. [Old Program Flowchart](Documentation/flowchart/system_flowchart_1_outdated.png)

# ChangeLogs

1. Current App [ChangeLog 1.3.0](Documentation/ChangeLog/2_1_3_0.md)
2. Current Dev [ChangeLog 1.3.0](Documentation/ChangeLog/2_1_3_0.md)
3. Archived [ChangeLogs](Documentation/ChangeLog)

## v1.3.0 > Completed

## v1.4.0 > Settings and Connections Docs (Not Started)

1. Add libs to /lib
2. Change file names:
    1. settings.txt > app.settings
3. create file:
    1. connection1.app.config
4. auto detect platform
    1. removes the need for platform on config
5. rename "rawCSV_#.txt" to "raw_#.txt"
6. exporting files will use FileChooser for custom naming and placement.
7. Add file logging for exceptions
8. Merge Launcher.java and Main.java

### Settings and MenuItems

1. Settings (app.settings) (MenuItem under File)
    1. Filepath (MenuItem) aka Help
        1. Base Folder
        2. Screenshots
        3. CSV
        4. Raw CSV // different default path
        5. Logs
    2. Filenames
        1. Scatterplot > screenshot_scatterplot_#.png
        2. Table > screenshot_table_#.png
        3. Raw CSV > raw_#.csv
        4. CSV > csv_#.csv
    3. View
        1. Theme
        2. Width
        3. Height
        4. Table Visible (MenuItem)
        5. Scatterchart: Fit/Expand
    4. Images
        1. DPI (Slider)
        2. Scatterplot DPI (Slider)
        3. Table DPI (Slider)
2. Connection (app.config) (MenuItem under File)
    1. Port: (From OS)
    2. Baud Rate: 115200
    3. Packet Data: PN12-MiramarWaterJets,pkt-#,time,unit2
    4. Time
        1. Unit: s
        2. Conversion: 1 // unit conversion up to 2 decimal points
    5. Unit 2
        1. Name: Depth
        2. Unit: m
        3. Conversion: 1
    6. Flags
        1. Start Data Transfer
        2. End Data Transfer
    7. Group Name: Profile
3. Help > About
    1. Team: Miramar Water Jets
    2. Author: Aadarsh Devi
    3. Name: Float Data Visualizer
    4. Release Version:  v{internal version}
    5. Dev Version: v{release version}
    6. Platform: AutoDetect/ final PLATFORM
4. Data Receiver after App opens

### Packages and Classes

1. Backend
    1. Data
        1. Settings
            1. Settings
            2. SettingsReader // read settings
            3. SettingsWriter // write settings
        2. Connections
            1. Connection
                1. Port
                2. BaudRate
            2. ConnectionReader
            3. ConnectionWriter
            4. Exceptions
                1. ____NotFoundException
                2. ____NotFoundException
                3. ____NotFoundException
        3. DataPoint
    2. FXMLReader
        1. FXMLReader (getInstance)
        2. FXMLPackage
        3. Exceptions
            1. ____NotFoundException
            2. ____NotFoundException
    3. Util
        1. csv
            1. CSVWriter
            2. CSVWriterFactory
    4. DataKeeper
        1. DataKeeper
        2. DataKeeperManager
    5. DataReceiver
        1. DataReceiver

    6. FilePath
        1. FilePath
        2. FilePathFactory
    7. Logging
        1. Logger
            1. Logger
            2. LoggerFactory
        2. FileLogger
            1. FileLogger
            2. FileLoggerFactory
2. API
    1. Exitter
    2. Api
    3. ApiManager
3. Frontend
    1. UI
        1. CopyStage
    2. Controller
        1. DataPlotter
            1. DataPlotter
4. Launcher
    1. enum Platform: WIN10, WIN11, MacOSX_INTEL, MacOSX_ARM, LINUX
5. Main (Merged with Launcher)
    - run backend (main thread), api (api thread), frontend (javafx thread)
