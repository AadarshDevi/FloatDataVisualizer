package com.alphagen.studio.FloatDataVisualizer.core.data;

/**
 * The team name and number stored as a record.
 * <p>
 * Ex: PN12-MiramarWaterJets
 *
 * @param teamNum  the number of the team
 * @param teamName the name of the team
 */
public record TeamInfo(
        String teamNum,
        String teamName
) {
    @Override
    public String toString() {
        return teamNum + "-" + teamName;
    }
}
