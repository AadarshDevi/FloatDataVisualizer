package com.alphagen.studio.FloatDataVisualizer.buoyui.backend.lib.jFileSystem;

import java.io.File;
import java.net.URI;

public class Folder extends File {
	public Folder(String pathname) {
		super(pathname);
	}

	public Folder(URI uri) {
		super(uri);
	}
}