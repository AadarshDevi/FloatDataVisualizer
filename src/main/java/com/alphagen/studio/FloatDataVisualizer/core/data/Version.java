package com.alphagen.studio.FloatDataVisualizer.core.data;

public record Version(int major, int minor, int patch, String build) {
    @Override
    public String toString() {
        if (build == null || build.isBlank()) {
            return major + "." + minor + "." + patch;
        }
        return major + "." + minor + "." + patch + "-" + build;
    }
}
