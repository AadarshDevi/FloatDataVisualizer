# Float Data Visualizer 2.0.0

This is the Float Data Visualizer. It is used to communicate with the floats' data receiver
via Serial Communication.

## Table of Contents

1. Getting Started
2. User Manual
3. Downloads
4. Technical Documentation
5. Author
6. [Feature List](#features-list)

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
2. MacOS x86_64 (Intel): Late 2013 Macbook Pro
3. MacOS ARM (M-Series): Macbook M2
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

1. IntelliJ Idea
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

# Features List

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

## Connection Editor

1. [x] Write Connection
2. [ ] Reset Connection Data in Editor
3. [x] Autofill data based on last connection
4. [ ] Autofill data: float-info, start/end transfer flags

## Preferences

1. [ ] Theme Changing: Light/Dark
2. [ ] Default Team Start Data Transfer Flag
3. [ ] Default Team End Data Transfer Flag
4. [ ] Default Team Info
5. [ ] Default Team Info Data

## DataViewer

1. [ ] Change Data View: Graph/Table
    1. [ ] Via Buttons: GraphView Button & TableView Button
2. [ ] Zoom In/Out Graph --> Bigger and Smaller Graphs
    1. [ ] Ctrl + Mouse Scroll Up --> Zoom In
    2. [ ] Ctrl + Mouse Scroll Down --> Zoom Out
    3. [ ] Mouse Scroll Up --> Scroll Up
    4. [ ] Mouse Scroll Down --> Scroll Down
    5. [ ] Shift + Mouse Scroll Up --> Scroll Left
    6. [ ] Shift + Mouse Scroll Down --> Scroll Right
3. [ ] Import CSV (.float.data)
4. [ ] Export CSV (.float.data)
5. [ ] Import Raw Data CSV (.raw.float.data)
6. [ ] Export Raw Data CSV (.raw.float.data)
7. [ ] Start Data Receiving
8. [ ] Stop Data Receiving
9. [ ] Tabs for multiple Data Recorded

## DataStorage

1. [ ] data stored in double[]
    1. [ ] double[].length = number of data recording + 1 (for time)
2. [ ] --item-template

## Documentation Viewer aka JavaFX Lib: MarkdownFX

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