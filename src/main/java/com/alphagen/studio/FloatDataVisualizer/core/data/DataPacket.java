package com.alphagen.studio.FloatDataVisualizer.core.data;

/**
 * The data packet received by the float
 * <p>
 * Ex: PN12-MiramarWaterJets,pkt-0,{time},{depth},{pressure}
 *
 * @param teamInfo  Name and number of the team
 * @param packetNum the number of the data packet
 * @param data      the data stored as an array
 */
public record DataPacket(
        TeamInfo teamInfo,
        int packetNum,
        double[] data
) {
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(teamInfo.toString());
        sb.append(",pkt-");
        sb.append(packetNum);
        sb.append(",");

        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (i != data.length - 1)
                sb.append(",");
        }

        return sb.toString();
    }
}
