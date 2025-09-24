
## settings.txt
Settings FilePath: `FloatDataRecorder_2_0\FloatDataRecorder_2_0\app\classes\com\alphagen\studio\float_data_recorder_2\settings.txt`

### Variables and Values

These values of the variables can be changed to suit each float.

#### **_commPort_**
The Serial Comm Port that has the Electronic Float Data Reciever connected to.
> commPort=COM3

#### **_baudRate_**
The speed of which information is transfered. Data Transfered Per Second (I might be wrong).
> baudRate=115200

<dl><dt></dt>
  <dd>
    <br>Default value: 115200</dd>
</dl>
<dl><dt>packetData</dt>
  <dd>Contains the team number, team name and the name of the packet.
    <br>PacketData: TeamNum-TeamName,Packet-PacketName,time,depth
    <br>Default value: PN12-MiramarWaterJets,pkt-</dd>
</dl>
<dl><dt>startDataTransfer</dt>
  <dd>A Signal to tell the app to start collecting data.
    <br>Default value: --start-data-transfer</dd>
</dl>
<dl><dt>endDataTransfer</dt>
  <dd>A Signal to tell the app to stop collecting data.
    <br>Default value: --end-data-transfer</dd>
</dl>
<dl><dt>dataGroupName</dt>
  <dd>This is the name of data groups that will be visible on the chart.
    <br>Default value: Group</dd>
</dl>

[Table of Contents](README.md)
