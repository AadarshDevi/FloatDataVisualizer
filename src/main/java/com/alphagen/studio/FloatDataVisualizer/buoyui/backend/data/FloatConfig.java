package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.data;


import lombok.Builder;

// fixme: add params
@Builder
public record FloatConfig(String teamData, String startFlag, String endFlag,
						  String measurement, String measurementUnit) {}