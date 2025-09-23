# Download Float Data Recorder

1. Go to Releases section of this Repository
2. Download 2.0.0 Stable and Unzip it.
3. Go into the extracted folder till you find `FloatDataRecorder_2_0.exe`
4. Double click `FloatDataRecorder_2_0.exe`
5. If you get Windows protected your PC > click "More info" > click "Run anyway"
6. If there is a Serial Comm Port connected to your PC, the terminal will show up and print data and values.
7. If there are no Serial Comm Ports connected, the terminal will close.

## settings.txt
Settings FilePath: `FloatDataRecorder_2_0\FloatDataRecorder_2_0\app\classes\com\alphagen\studio\float_data_recorder_2\settings.txt`

### Variables and Values
<dl><dt>commPort</dt>
  <dd>The Serial Comm Port that has the Electronic Float Data Reciever connected to.
    <br>Default value: COM3</dd>
</dl>
<dl><dt>baudRate</dt>
  <dd>The speed of which information is transfered. Data Transfered Per Second (I might be wrong).
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

## Data Transfer

The app will start recieving data when it recieves the start flag **_startDataTransfer_**.<br><br>

The data transfered will be in a rigid structure below:<br>
Structure: TeamNum-TeamName,Packet-PacketNum,Time,Depth<br>
Example: PN12-MiramarWaterJets,pkt-0,5.67,30<br>

Once all data is transfered, the app will stop recieving data when it recieves the end flag **_endDataTransfer_**.<br>
