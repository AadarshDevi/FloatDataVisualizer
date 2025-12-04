# Miramar Water Jets's Mate Float Data Visualizer

Welcome, I am Aadarsh Devi from Miramar Water Jets.
<br><br>
<ins>_**NOTE:**_</ins> It is suggested to read the entire **README**.
<br>
<ins>_**NOTE:**_</ins> Use Outline for quick navigation.

## Project Info
1. Team: Miramar Water Jets
2. Author: Aadarsh Devi
3. App: Float Data Visualizer
4. Release Version: 1.3.1
5. Dev Version: 1.3.1
6. Language: Java/JavaFX
7. Build Tool: Maven
8. VCS: Git/GitHub

## Pre-Information
1. FloatDataVisualizer might sometimes be abbrevated to FDV
2. ScatterChart and ScatterPlot refer to the same chart.

## Instructions

### Network and Connection

```
        (Team's Wireless Communication)                       (Serial Communication)
Float -----------------------------------> Microcontroller --------------------------> FloatDataVisualizer App
         (Wi-Fi, LoRa, Bluetooth, etc)                         (Done through App)
```

### Download

1. Go to [Releases](https://github.com/AadarshDevi/FloatDataVisualizer/releases)
2. Download the Latest Release
3. Click FloatDataVisualizer version-version.msi
4. If you get the **Windows Defender Blue Box**, click run anyway.
5. You will get:
    1. App in the location the user wants
    2. Shortcut in Desktop
    3. Maybe app pinned or placed in Start Menu

### How to Configure APP
1. Click FloatDataVisualizer with the correct version.
2. There will be an filepath message after either of the below messages
    1. Exception if there is no serial ports available or serial port is not correct.
3. **DO NOT PRESS ANYTHING.**
4. Copy the filepath for the project data folder.
5. Go to **File Explorer** and paste in the filepath.
6. Once there open settings.txt replace the correct values below.
    1. commPort > dont chage the text before **COM**
    2. baudRate
    3. packetData > (Change only the bolded text) **TeamID**-**TeamName**,pkt-,time,unit2
    4. unit2_name > The name of the measurement. Ex: Depth, Pressure
    5. unit2_unit > the unit of the measurement. Ex: m, Pa
    6. startDataTransfer > it can be any text with will not appear in the data that will be transfered.
    7. endDataTransfer > it can be any text with will not appear in the data that will be transfered.
7. Now you have configureed you fdv app, you can run it again.

### Using App
1. Open App **after** a microcontroller is connected to the Device (Desktop/Laptop).
2. The app should start receiving data after the startDataTransfer flag is sent.
    1. Check Console
3. Once startDataTransfer flag is sent, data will be printed on the console and on the ScatterPlot.
4. The data received will be on Console, ScatterPlot, Table.
5. [NOTE] If there are any **ERROR**s, check [Exception Messages](#exception-messages) below.
6. [NOTE] If the format of the data of the float and app are not the same, there will be no output.
7. Data will be continued to be read and put on the chart and table till the endDataTransfer flag is recieved.

### Viewing Data

There are a few features to view the data.
1. ScatterPlot: The datapoints on the plot when hovered will show time and unit2.
2. Menubar has a few helpful tools: View > ScatterChart
3. There are 4 options.
   1. Fit View: All points on the chart are visible.
   2. Full View: ScatterChart will have the width it had when it was reading all the data.
   3. Increase/Decrease Width: Increases or decreases the width of the chart.

### Exporting Data
The app can export data as Screenshots, CSV, and Raw.

#### Screenshots
1. Go to **_Export > Screenshot_** and select which screenshot you want.
2. The app will create a screenshot of the scatterchart or table based on the option chosen.
3. To access the screenshots, go to **_Help > Filepaths > Screenshots_**.
4. Copy the path and paste it into File Explorer
5. In this folder there will be 2 folders: **_ScatterChart_** and **Table**
6. Open the folder which you chose for the screenshot.
7. Your screenshots will be there.

#### Data CSVs
1. Go to **_Export > Data_** and select which type of data you want to export.
   1. Raw > Data that was sent by the float.
   2. CSV > Only the decimal values, time and unit2
2. Once the data is exported as a csv, go to **_Help > Filepaths > CSVs_**.
3. Copy the path and paste it into File Explorer.
4. The CSVs are there to be used. Both Raw and CSV will be in this folder.

### Exception Messages

There are many exceptions to pin point the problems when running the app. They are mostly ordered in chronological order. These are almost all the exception messages. If I have missed any, please raise an [issue](https://github.com/AadarshDevi/FloatDataVisualizer/issues).

1. Unable to get User's Platform/OS and Username
2. Unable to generate base folder
3. Unable to generate settings.txt and cannot find filepath
4. Unable to generate ScatterChart Screenshots folder
5. Unable to generate Data Table Screenshots folder
6. Unable to generate Log folder
7. Unable to generate Data(CSV) folder
8. Datapath of settings.txt is null
9. settings.txt resource not found
10. Project Version does not exist
11. Project Version does not match Application Version
12. Release Version does not exist
13. Project Version does not match Application Version
14. Platform is null
15. Wrong App. Please use {{platform}} version of the app
16. settings.txt is in the wrong platform
17. Baud Rate is EMPTY
18. baudRate is not a number
19. Serial Comm Port is null
20. Packet Data is null or Does not exist
21. "time" or "unit2" not found in "packetData"
22. Time Unit is EMPTY
23. Unit 2 Name in null
24. Unit 2 Unit in null
25. Start Data Transfer Flag in null
26. End Data Transfer Flag in null
27. Data Group Name in null
28. Unable to parse Settings, Properties and InputStream
29. Thread Interrupted in Settings
30. Serial Comm Ports connected to something else
31. Serial Ports are empty > This means that there are no serial ports connected to the device. Check using **Device Manager > Ports (Com & LPT)**
32. DataPlotter is null
33. Unable to "put" Float DataPoint
34. SerialComm Port does not exist
35. Unable to create SerialComm with port
36. COM Port Connection ERROR
37. Input error in Snapshot ScatterChart MenuItem
38. Input error in Snapshot TableView MenuItem
39. Unable to write DataPointRecord

## Future Updates (Not 100% Confirmed)

### 2.1.4.0

##### This will be Windows only from now on.

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

#### Settings and MenuItems

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

#### Packages and Classes

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

## Build Project (Prerequisites)
1. IntelliJ Idea
2. Java 23.0.2
3. JavaFX 23.02
4. jSerialComm 2.11.2
5. Apache Maven
6. Git

