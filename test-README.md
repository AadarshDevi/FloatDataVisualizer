# Miramar Water Jets's Mate Float Competition's Float Data Visualizer

Welcome, I am Aadarsh Devi from Miramar Water Jets.
<br><br>
<ins>_**NOTE:**_</ins> It is suggested to read the entire **README**.

## Project Info
1. Team: Miramar Water Jets
2. Author: Aadarsh Devi
3. App: Float Data Visualizer
4. Abbrevation: FDV
5. Release Version: 1.3.1
6. Dev Version: 1.3.1
7. Language: Java/JavaFX
8. Build Tool: Maven
9. VCS: Git/GitHub

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

### Exporting Data


### Exception Messages



## Future Updates (Not 100% Confirmed)

### 2.1.4.0



