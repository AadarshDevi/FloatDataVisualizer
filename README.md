# Float Data Visualizer 2.0.0

The Float Data Visualizer is a Data Acquisition Application that gets data from your team's
profiling float and displays the data using scatter plots and a table.

## Getting Started

Please follow each step to get started in using the app. It goes in detail so you, the user can feel comfortable using
the app.

### Bug Report

If you find any issues, please use the [Issues Page](https://github.com/AadarshDevi/FloatDataVisualizer/issues) to
report bugs, glitches etc. Before reporting the bug, it is recommended to use the Console version of the app so that I
can use it to fix the app.

### Download

Check [Releases](https://github.com/AadarshDevi/FloatDataVisualizer/releases) for the app.

Once you have downloaded the executable, run it so the app can be downloaded. You do not
need to change any of the settings unless stated otherwise.

### How the App Works

The app is connected to a float receiver, a board like an Arduino, ESP32, Raspberry PI, etc. The
board sends data to the app using serial communication. The float receiver gets data from the
float wirelessly (LoRa, Wi-Fi, Bluetooth). Below is a simple flowchart of how the setup looks like.
It uses the setup our team had for the competition.

```
                        Serial Comms                    Wireless Comms
Float Data Visualizer ----------------> Float Receiver ----------------> Float
                       baudRate 115200                       LoRa
```

Open **_FloatDataVisualizer_**.

### Page: Connections

This is the page of app where you will see when the app is launched. It has the app name and
the text saying connections on the right. Right now there are no configs so it is empty.

![connections_page.png](Documentation/BuoyUI/readme_imgs/connections_page.png)

In this page, you can manage your various configurations instead of having change the settings everytime
you open the app. Users can quit/close the app by clicking the **_Quit_** button.

A connection is a file that stores config data like baud rate, port of the board, types of data,
etc. Now that we are in the connections page, let's create a connection to the float's data receiver.

### Connection: Creation

To create a connection click the **_button that says create_**. It should open a window titled
**_New Connection_**

![img_3.png](Documentation/BuoyUI/readme_imgs/img_3.png)

There is a list of fields that you **_have_** to enter to create a connection. The values are
in the table below. In the table, a **_float receiver_** means an Arduino, ESP32, or RPi boards used
to receive data from your float.

| Field           | Description                                                                                                  | Accepted Values               |
|:----------------|:-------------------------------------------------------------------------------------------------------------|:------------------------------|
| Connection Name | This will be the name of the config. it has to be unique.                                                    | A - Z, a - z, 0 - 9, "_", " " |
| Baud Rate       | This is the baud rate your float receiver have.                                                              | 0 - 9                         |
| Port            | The port your float receiver. It gets updated automatically when there are new ports added or ports removed. | Values on the dropdown        |

Now we got the simple data entered, next are the inputs that need information about the float.

#### Data Format

![img_4.png](Documentation/BuoyUI/readme_imgs/img_4.png)

When you created your float, you made your float send data in a specific format. Our float sent data
in the format below:

```
PN12-MiramarWaterJets,pkt-,time,depth,pressure
```

To convert the format of the float to the app, it is simple. We add the units the measurements are in,
time in sec (s), depth in meters (m), and pressure in pascals (Pa). You do not have to capitalize the
name of the units. time/Time will not affect the use of the app. With this method, the team info and packet
number placement is rigid.

```
PN12-MiramarWaterJets,pkt-,Time(s),Depth(m),Pressure(Pa)
```

The **_time, depth, pressure_** texts are placeholders for the actual data the float will send.

comparing both formats:

```
Float: PN12-MiramarWaterJets,pkt-,time,depth,pressure
  App: PN12-MiramarWaterJets,pkt-,Time(s),Depth(m),Pressure(Pa)
```

The team info, in our case `PN12-MiramarWaterJets`, is really important because if you receive another team's
data, it will not record the data. If you have a typo in the name, then the app will not record the data. The
team info is **_case-sensitive_**.

#### Flags

Flags are special text that is sent to the app. These texts do not show up on the data. We use this
to tell the app to start and stop receiving data from the float receiver.

Before the float sends data, it must send a flag/text called the **_Start Data Transfer Flag_**. It tells the app
to start recording the data when this text is received. This flag is entered in the **_Start Flag_**
input. Our team used:

```
--start-data-transfer
```

Just like the Start Data Transfer Flag, we have the **_End Data Transfer Flag_**. This flag tells the
app to stop recording data. This flag is entered in the **_End Flag_** input. Our team used:

```
--end-data-transfer
```

It is advised to have a unique name for both flags to avoid accidentally starting or stopping recording
data.

Once all the fields have been filled, click **_Confirm_**. This will create a new connection. Below is
the connection used for this tutorial (The info is different for each team):

```
Connection Name: Serial Connection 1
Baud rate: 115200
Port: COM4

Team Number and Name:  PN12-MiramarWaterJets,pkt-,Time(s),Depth(m),Pressure(Pa)
Start Flag: --start-data-transfer
End Flag: --end-data-transfer
```

and our float sends data using the format below:

```
Start Data Transfer Flag (Before Data Transmission): --start-data-transfer
Data Format: PN12-MiramarWaterJets,pkt-,time,depth,pressure
End Data Transfer Flag (After Data Transmission): --end-data-transfer
```

This is how our connection looks in the **_Connection Editor_**:

![img_7.png](Documentation/BuoyUI/readme_imgs/img_7.png)
![img_6.png](Documentation/BuoyUI/readme_imgs/img_6.png)

Now we have a connection created. It now appears in **_Connections_**.

![img_8.png](Documentation/BuoyUI/readme_imgs/img_8.png)

### Page: Grapher

To start collecting data from your receiver, click the connection you have created. It will take
you to a new page, the Grapher Page.

In the **_Grapher Page_**, there are a few tabs.

| Tab              | Description                                                                                                                                      |                            View                            |
|:-----------------|:-------------------------------------------------------------------------------------------------------------------------------------------------|:----------------------------------------------------------:|
| Config           | Shows the configuration of the connection. It is the information you entered to create the connection.                                           |  ![img_9.png](Documentation/BuoyUI/readme_imgs/img_9.png)  |
| Terminal         | This tabs shows all the raw data your app is receiving from the float.                                                                           | ![img_10.png](Documentation/BuoyUI/readme_imgs/img_10.png) |
| Table            | This your data in form of a table                                                                                                                | ![img_12.png](Documentation/BuoyUI/readme_imgs/img_12.png) |
| Tabs after Table | The tabs after the **_Table Tab_** are scatter plots for each of the data points beoing recorded. Depth, Pressure, etc, will have their own tab. | ![img_11.png](Documentation/BuoyUI/readme_imgs/img_11.png) |

#### Collect Data

To start collecting data from your float click **_Start Button_**. App will start recording the data it receives. You
will see the Table Tab and graphs being filled up by the data. For you, the depth and pressure tabs will be different
based on the data being recorded.

|   Tab    |                           Image                            |
|:--------:|:----------------------------------------------------------:|
| Terminal | ![img_13.png](Documentation/BuoyUI/readme_imgs/img_13.png) |
|  Table   | ![img_14.png](Documentation/BuoyUI/readme_imgs/img_14.png) |
|  Depth   | ![img_15.png](Documentation/BuoyUI/readme_imgs/img_15.png) |
| Pressure | ![img_16.png](Documentation/BuoyUI/readme_imgs/img_16.png) |

Now we have the data recorded. If the **_Stop Button_** is disabled, then the receiver has ended receiving data. If it
is not disabled, user can click it to stop receiving data.

#### Viewing Data: Graphs

There are a few features to view the data in the graphs and table. In the graphs tabs, there are a few shortcuts:

| Name                | Shortcut                  | Description            |
|:--------------------|:--------------------------|:-----------------------|
| Zoom In             | Ctrl + Alt + Scroll Up    | Graph size increases   |
| Zoom Out            | Ctrl + Alt + Scroll Down  | Graph size decreases   |
| Horizontal Zoom In  | Shift + Alt + Scroll Up   | Graph width increases  |
| Horizontal Zoom Out | Shift + Alt + Scroll Down | Graph width decreases  |
| Vertical Zoom In    | Ctrl + Scroll Down        | Graph height increases |
| Vertical Zoom Out   | Ctrl + Scroll Down        | Graph height decreases |

To reset the size of the graph, right-click the graph and click **_Reset Size_**. It will set the graph back to the
original size.

If you want to take a picture of the graph, you can right-click and click **_Screenshot_**. This will create a PNG
image of the graph. The zoom does affect the graph, bigger the graph, larger the screenshot. Before the screenshot
is created, it will ask you to enter the name of the image.

Asking for the name of the screenshot.

![img_17.png](Documentation/BuoyUI/readme_imgs/img_17.png)

The screenshot in my desktop.

![img_18.png](Documentation/BuoyUI/readme_imgs/img_18.png)

The graph screenshot.

![img_19.png](Documentation/BuoyUI/readme_imgs/img_19.png)

#### Viewing Data: Table

Just like the graph, there is a table, it has the data. In the **_Table Tab_**, if you right-click, you can export
the data in a csv in 2 formats: raw data or the parsed data. Raw data is the data your floats sends (minus the flags)
and parsed data is data in a csv without the packet and team info. it has the time and the measurements recorded by your
float.

![img_20.png](Documentation/BuoyUI/readme_imgs/img_20.png)

Data exported raw.

![img_21.png](Documentation/BuoyUI/readme_imgs/img_21.png)

Data exported as CSV and Parsed

![img_22.png](Documentation/BuoyUI/readme_imgs/img_22.png)

## Other Features

| Feature                   | Description                                                                                                                                                                     | Image                                                      |
|:--------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------|
| Connection Available      | If a connection's float receiver is not connected, the connection is disabled. If the board is connected later, the connection will become enabled.                             | ![img_23.png](Documentation/BuoyUI/readme_imgs/img_23.png) |
| Serial Port unavailable   | When creating a connection, if the port is empty (no boards connected), you can connect a board and it will appear on the list of ports.                                        |                                                            |
| Deleteing All Connections | Right click in the empty space in the **_Connections Page_** and click **_Delete All_**. It will ask you to confim if you want to delete all the connections.                   | ![img_24.png](Documentation/BuoyUI/readme_imgs/img_24.png) |
| View Measurements         | Did you forget what measurements the connection records? Connections Page > Connection > Right Click > Measurements. It will show what data the app is recording.               | ![img_25.png](Documentation/BuoyUI/readme_imgs/img_25.png) |
| Delete Connection         | To delete a connection (only 1 connection, not all) go to Connections Page > Connection > Right Click > Delete. It will ask you to confim if you want to delete the connection. | ![img_26.png](Documentation/BuoyUI/readme_imgs/img_26.png) |
| Autofill Config Info      | Use the Autofill button in **_Connection Editor_** to autofill the connection and float data fields. can only be used if there was already a previous connection created.       |                                                            |
| Reset Button              | Want to reset all the fields? use the Reset Button on the **_Connection Creator_**                                                                                              |                                                            |

# Other Docs (Dev Docs) - Outdated

## Releases

For users who want the **_terminal with the app_**, the can download [Console Bundled App](#console-app)

### App Build Status

| Status | Build In Progress | Will Not Build | Future Builds | Build Available | Maybe Have Build |
|--------|-------------------|----------------|---------------|-----------------|------------------|
| Icon   | ⏳                 | ❌              | ⏭️            | ✅               | ⭯                |

### Download Links

The actual (base) app is actively being worked on. Then Win11, Linux will be released and later on MacOS.

| Status |            Platform            | Architecture | Executable Type | App Type | Download Link |
|:------:|:------------------------------:|:------------:|:---------------:|:--------:|:-------------:|
|  ⏭️️   |            Windows             |    x86_64    |      .msi       |   App    |               |
|  ⏭️️   |            Windows             |    x86_64    |      .exe       |   App    |               |
| **⭯**  |            Windows             |     ARM      |      .msi       |   App    |               |
| **⭯**  |            Windows             |     ARM      |      .exe       |   App    |               |
|  ⏭️️   |             MacOS              |    x86_64    |      .dmg       |   App    |               |
|  ⏭️️   |             MacOS              |    x86_64    |      .pkg       |   App    |               |
| **⭯**  |             MacOS              |     ARM      |      .dmg       |   App    |               |
| **⭯**  |             MacOS              |     ARM      |      .pkg       |   App    |               |
| **⭯**  |             Linux              |    x86_64    |    .AppImage    |   App    |               |
| **⭯**  |             Linux              |     ARM      |    .AppImage    |   App    |               |
|  ⏭️️   |   Debian, Ubuntu, Linux Mint   |    x86_64    |      .deb       |   App    |               |
| **⭯**  |   Debian, Ubuntu, Linux Mint   |     ARM      |      .deb       |   App    |               |
|  ⏭️️   | Fedora, RHEL, CentOS, openSUSE |    x86_64    |      .rpm       |   App    |               |
| **⭯**  | Fedora, RHEL, CentOS, openSUSE |     ARM      |      .rpm       |   App    |               |

### Executable Creation Platform

1. Windows 11 x86_64: MSI Vector A16 HX A8W
2. MacOS x86_64 (Intel): Late 2013 MacBook Pro
3. MacOS ARM (M-Series): MacBook M2
4. Debian x86_64: MSI Vector A16 HX A8W w/ VirtualBox Debian Linux

### Console App

| Status |            Platform            | Architecture | Executable Type |    App Type    | Download Link |
|:------:|:------------------------------:|:------------:|:---------------:|:--------------:|:-------------:|
|  ⏭️️   |            Windows             |    x86_64    |      .msi       | ConsoleBundled |               |
|  ⏭️️   |            Windows             |    x86_64    |      .exe       | ConsoleBundled |               |
| **⭯**  |            Windows             |     ARM      |      .msi       | ConsoleBundled |               |
| **⭯**  |            Windows             |     ARM      |      .exe       | ConsoleBundled |               |
| **⭯**  |             Linux              |    x86_64    |    .AppImage    | ConsoleBundled |               |
| **⭯**  |             Linux              |     ARM      |    .AppImage    | ConsoleBundled |               |
| **⭯**  |   Debian, Ubuntu, Linux Mint   |    x86_64    |      .deb       | ConsoleBundled |               |
| **⭯**  |   Debian, Ubuntu, Linux Mint   |     ARM      |      .deb       | ConsoleBundled |               |
| **⭯**  | Fedora, RHEL, CentOS, openSUSE |    x86_64    |      .rpm       | ConsoleBundled |               |
| **⭯**  | Fedora, RHEL, CentOS, openSUSE |     ARM      |      .rpm       | ConsoleBundled |               |

## Technical Documentation

Technical Docs will be updated when there is a big change of the codebase or new features being added.

## User Manual

## Technical Docs - 2.0.0 Update Changelog

**This is the second rewrite of the app to make the ui more user-friendly.**

When v1.3.1 was built, it used the technical knowledge of the user to be able to navigate through
the app. After some serious self-reflection, I am working on updating the app to be more user-friendly.
Finding app files and changing the settings.txt file to have the correct settings can now be done in
the app.

### Features

#### Updated

1. Screenshots (ScatterPlot, Table), Exporting Data (Raw CSV, CSV): App will ask the user where to store the file.
2. ScatterPlot and the Table will have separate tabs instead of being on the same place.

#### New Additions

1. Users can now have multiple connections
2. Users can now export/import serial connections with the import/export feature.
3. User Manual: The app will have a dedicated **_offline_** user manual. It will show from the start of the app to the
   features of it.
4. Serial Connection can start/stop with a click of a button. Originally, the app had to be closed and reopened again.
5. Flags to start/end data communication will also be added when creating the connection. They can also be saved.
6. Team name and id will be added when creating connections. They can also be saved.

## Build Project (Prerequisites)

1. IntelliJ IDEA
2. Java 23.0.2
3. JavaFX 23.0.2
4. jSerialComm 2.11.2
5. Apache Maven
6. Git
7. OS Specific
    - Windows: WiX 3.14
    - MacOS: Apple Developer Account
    - Linux: ?

## Data Files

Root: C:\Users\{user}\AppData\Roaming\FloatDataVisualizer\2.0.0

1. float.settings
    1. theme
    2. dataFormat
2. connections
    1. {Connection Name}.float.connection
3. logs
    1. {datetime}.float.log

## Package: BuoyUI

- backend
    - Backend
    - data
        - ConnectionConfig
        - FloatConfig
        - ConnectionType
    - processor
        - ConnectionProcessor
        - FloatProcessor
    - util
        - DeltaDrag
        - DynamicCSS
    - constants
        - FolderConstants
- frontend
    - pages
        - connections
            - editor
            - constants
    - util
        - StageUtil
        - loading
            - LoadingScreen
    - managers
        - StageManager
        - ControllerManager
- [x] app
    - AppData
    - Platform
- [x] lib
    - jFileSystem
    - markdownfx

## Pages

1. Connections: ~~Create~~, Edit, ~~Delete~~, Duplicate, Delete All, Export, Import, Export All
    - create: if the connection name already exists, throw error label

# Todo List

## Codebase

Revamp codebase to separate them from front end and backend using an api class.

## Connections Page

1. [x] Create Connection
2. [x] Real Delete
3. [x] Real Delete All
4. [ ] Edit
5. [ ] Export
6. [ ] Export All
7. [ ] Import All
8. [ ] Duplicate
9. [x] Read Connection
10. [x] Able to store different types of data in MeasurementConfigs
11. [x] View different types of data float data visualizer will record
12. [ ] Add CSS for MeasurementViewer
13. Connection Page View:
    1. ConnectionPage.View.Tile
    2. ConnectionPage.View.Detail
14. rename connection name

## Connection Editor

1. [x] Write Connection
2. [x] Reset Connection Data in Editor
3. [x] Autofill data based on last connection
4. [x] Autofill data: float-info, start/end transfer flags
5. [x] error labels become label for each and the text is replaced (visible become text="")
6. Connection Editor Mode:
    1. ConnectionEditor.Mode.EDIT: edit connections
    2. ConnectionEditor.Mode.CREATE: create connections

## Connection

1. Connection Type:
    1. Connection.Type.Serial
    2. Connection.Type.Wireless
    3. Connection.Type.Server (need icon for it)

## Preferences

1. [ ] Theme Changing: Light/Dark
2. [x] Default Team Start Data Transfer Flag
3. [x] Default Team End Data Transfer Flag
4. [x] Default Team Info
5. [x] Default Team Info Data

## Grapher Page

1. ~~[ ]Change Data View: Graph/Table~~
    1. ~~[ ] Via Buttons: GraphView Button & TableView Button~~
2. [x] Zoom In/Out Graph --> Bigger and Smaller Graphs
    1. [x] Mouse Scroll Up/Down --> Scroll Up/Down
    2. [x] Shift + Mouse Scroll Up/Down --> Scroll Left/Right
    3. [x] Ctrl + Alt + Mouse Scroll Up/Down --> Zoom In/Out
    4. [x] Ctrl + Mouse Scroll Up/Down --> Vertical Zoom In/Out
    5. [x] Shift + Alt + Mouse Scroll Up/Down --> Horizontal Zoom In/Out
3. [ ] Import CSV (.float.data)
4. [x] Export CSV (.float.data)
5. [ ] Import Raw Data CSV (.raw.float.data)
6. [x] Export Raw Data CSV (.raw.float.data)
7. [x] Start Data Receiving
8. [x] Stop Data Receiving
9. [x] Tabs for multiple Data Recorded

## Data Point Processor

1. [x] data stored in double[]
    1. [x] double[].length = number of data recording + 1 (for time)
2. ~~[x] --item-template~~

## Markdown Viewer aka JavaFX Lib: MarkdownFX

A basic Markdown file reader. Takes in a **.md file** and **a vbox**. It must have basic parsing.

1. [ ] Hyperlink (Hyperlink)
2. [ ] Image (ImageView)
3. [ ] Heading 1 - 6 (Label)
4. [ ] Paragraph (Text) set width of vbox before
5. [ ] Unordered List
6. [ ] Ordered List
7. [ ] Simple Table // does not use h1-6
8. [ ] Bold
9. [ ] Italics

# Before Creating Executable

1. Make sure either Win11, MacOS or Linux is ready and the rest commented
2. change the project version in pom to correct version
3. change project version in AppData.java
4. new command:
      ```
      mvn clean install -DskipTests
      ```
