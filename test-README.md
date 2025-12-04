# Miramar Water Jets's Mate Float Competitions's Float Data Visualizer

Welcome, I am Aadarsh Devi and I am the programming that built this app for Mate Float. This app would be used to get data from the float through serial communication.

## Project Info
1. Name: Float Data Visualizer
2. Abbrevation: FDV
3. Version: 2.1.3.1
4. Language: Java/JavaFX
5. Build Tool: Maven
6. VCS: Git/GitHub

## Instructions

### Network and Connection

```
     (Team's Wireless Communication)                  (Serial Communication)
Float ----------------------------> Microcontroller --------------------------> FloatDataVisualizer
     (Wi-Fi, LoRa, Bluetooth, etc)
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
    2. None if the microcotnroller is connected.
3. DO NOT PRESS ANYTHING.
4. Copy the filepath for the project data folder.
5. Go to **File Explorer** and paste in the filepath.
6. Once there open settings.txt replace the correct values below.
    1. commPort > dont chage the text before COM#
    2. baudRate
    3. packetData > (Change only the bolded text) **TeamID**-**TeamName**,pkt-,time,unit2
    4. unit2_name and unit2_unit
    5. startDataTransfer and endDataTransfer
7. Now you have configureed you fdv app

### Use FDV




