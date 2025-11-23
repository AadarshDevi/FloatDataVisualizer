package com.alphagen.studio.FloatDataVisualizer.data;

public record DataPointRecord(int packetId, double time, double depth) {

    private static final DataConfigurator DATA_CONFIGURATOR = DataConfigurator.getInstance();
//    private static double temperature;

    @Override
    public String toString() {
        return DATA_CONFIGURATOR.TEAM_DATA + "," + DATA_CONFIGURATOR.getPacketName() + "-" + packetId + "," + time + "," + depth;
    }

    public String toCSV() {
        return time + "," + depth;
    }
}
