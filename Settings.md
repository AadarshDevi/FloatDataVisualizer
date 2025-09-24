
## settings.txt
Settings FilePath: `FloatDataRecorder_2_0\FloatDataRecorder_2_0\app\classes\com\alphagen\studio\float_data_recorder_2\settings.txt`

Table of Contents:
1. [commPort](#commport)
2. [baudRate](#baudrate)
3. [packetData](#packetdata)
4. [startDataTransfer](#startdatatransfer)
5. [endDataTransfer](#enddatatransfer)
6. [dataGroupName](#datagroupname)
7. [Future Update] [projectVersion](#projectversion)
8. [Future Update] [platform](#platform)

### Variables and Values

These values of the variables can be changed to suit each float.

#### **_commPort_**
The Serial Comm Port that has the Electronic Float Data Reciever connected to.
> commPort=COM3

#### **_baudRate_**
The speed of which information is transfered. Data Transfered Per Second (I might be wrong).
> baudRate=115200

#### **_packetData_**
Contains the team number, team name and the name of the packet.
> packetData=PN12-MiramarWaterJets,pkt-

#### **_startDataTransfer_**
A Signal to tell the app to start collecting data.
> startDataTransfer=--start-data-transfer

#### **_endDataTransfer_**
A Signal to tell the app to stop collecting data.
> endDataTransfer=--end-data-transfer

#### **_dataGroupName_**
This is the name of data groups that will be visible on the chart.
> dataGroupName=Group

#### **_[Future Update] projectVersion_**
It is the version of the project. This is to ensure that the settings file does not break the program.
> projectVersion

#### **_[Future Update] platform_**
This is to keep track of the platform of the project. this should not impact the program.
> 

#### **__**

> 

[Table of Contents](README.md)
