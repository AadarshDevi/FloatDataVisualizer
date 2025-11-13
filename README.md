# 2025-2026 Mate Float's Data Visualizer
This repository is for the releases of the Float Data Recorder. To know the technology used, check out _**Project Information**_ below.

# Table of Contents / Documentation

### 1. [Project Information](Documentation/ProjectInformation.md)
### 2. [Download](Documentation/Download.md)
### 3. [Future Updates](Documentation/FutureUpdates.md)
### 4. [Project Settings](Documentation/Settings.md)
### 5. [Transferring Data](Documentation/DataTransfer.md)
### 6. [Project Platforms](Documentation/Platforms.md)
### 7. [Old Program Flowchart](Documentation/flowchart/system_flowchart_1_outdated.png)

# 
1. Current Add ChangeLog [ChangeLog](Documentation/ChangeLog/2_1_1.md)
2. Current Version Dev [ChangeLog](Documentation/ChangeLog/2_2_0.md)
3. Archived [ChangeLogs](Documentation/ChangeLog)

## TODO
1. Merge Win11 and MacOS 11.7 code bases
2. Create `public enum Platform`
3. Split some of the code into **platform specific code**.
4. Try to get settings.txt in the folders below:
- Win11:
    - C:\Users\{User}\AppData\Local\alphagnfs\float_data_visualizer\settings.txt
    - C:\Users\{User}\OneDrive\Desktop\settings.txt
- MacOS 11.7:
    - /Users/{User}/Applications/alphagnfs/float_data_visualizer/settings.txt
    - /Users/{User}/Desktop/alphagnfs/float_data_visualizer/settings.txt
- Use **Java Swing** to get input if console input does not work on mac.
