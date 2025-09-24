# Future Updates

 > _**Update Progress > Paused**_

Internal Version / Release Version
## 2.0.2/1.0.2 Stable Release
### 2.0.1/1.0.1
1. Add version number to settings file. This must match the app version
   > projectVersion=2.0.1
1. Add [platform](Platforms.md) to settings file. This must match the app platform
   > platform=PlatformName

### 2.0.2/1.0.2
1. Make the data packet structure flexible. (depth and time switched places)
   > PN12-MiramarWaterJets,pkt-0,time,depth<br>
   > PN12-MiramarWaterJets,pkt-0,depth,time

## 2.0.4/1.0.4 Stable Release

### 2.0.3/1.0.3
1. add temperature to data packet
2. add pressure to data packet.

### 2.0.4/1.0.4
1. Some data [packetData](DataTransfer.md) will not be used so they do not have to appear in packetData.

[Table of Contents](README.md)
