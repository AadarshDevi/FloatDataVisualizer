package com.alphagen.studio.FloatDataVisualizer.core.data;

import lombok.Builder;

/**
 * The team name and number stored as a record.
 * <p>
 * Ex: PN12-MiramarWaterJets
 *
 * @param number number of the team
 * @param name   name of the team
 */
@Builder
public record TeamInfo(
        String number,
        String name
) {
    @Override
    public String toString() {
        return number + "-" + name;
    }
}
