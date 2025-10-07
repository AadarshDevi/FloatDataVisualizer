package com.alphagen.studio.FloatDataVisualizer.data;

public record DataPointRecord(int packetId, double time, double depth) {

    private static final Settings SETTINGS = Settings.getInstance();
//    private static double temperature;

    @Override
    public String toString() {
        return SETTINGS.TEAM_DATA + SETTINGS.getPacketName() + "-" + packetId + "," + time + "," + depth;
    }
}
