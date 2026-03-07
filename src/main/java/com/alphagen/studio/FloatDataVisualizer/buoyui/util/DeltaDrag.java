package com.alphagen.studio.FloatDataVisualizer.buoyui.util;

import lombok.Getter;
import lombok.Setter;

public class DeltaDrag {

	@Getter
	@Setter
	private double deltaX;

	@Getter
	@Setter
	private double deltaY;

	public DeltaDrag() {
		this.deltaX = 0;
		this.deltaY = 0;
	}
}