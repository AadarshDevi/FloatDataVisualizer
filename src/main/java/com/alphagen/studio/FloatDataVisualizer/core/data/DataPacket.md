DataPacket

```json
{
  "teamInfo": {
    "num": "",
    "name": ""
  },
  "packetNum": "",
  "data": [
    0.5,
    23.6,
    23.63,
    12.56
  ]
}
```

ConnectionConfig

1. "commands": list of ids of commands from global command list

```json
{
  "name": "Windows 11 Serial Connection Test",
  "operatingSystem": "windows",
  "version": "3.0.0",
  "connectionType": "serial",
  "serialPort": ".//COM3",
  "baudRate": 115200,
  "startDataFlag": "--start-data-transfer",
  "endDataFlag": "--end-data-transfer",
  "teamInfo": {
    "num": "PN12",
    "name": "MiramarWaterJets"
  },
  "sensorData": [
    {
      "name": "time",
      "unit": "s"
    },
    {
      "name": "depth",
      "unit": "m"
    },
    {
      "name": "pressure",
      "unit": "kPa"
    }
  ],
  "commands": [
    1,
    2,
    3,
    4
  ]
}
```