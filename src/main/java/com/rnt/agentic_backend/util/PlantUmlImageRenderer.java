package com.rnt.agentic_backend.util;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PlantUmlImageRenderer {
	public static void renderToPng(String plantUmlSource, String outputFilePath) throws Exception {
		File outputFile = new File(outputFilePath);

		// Ensure parent directory exists:
		File parentDir = outputFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			boolean created = parentDir.mkdirs();
			if (!created) {
				throw new RuntimeException("Failed to create output directory: " + parentDir.getAbsolutePath());
			}
		}

		SourceStringReader reader = new SourceStringReader(plantUmlSource);
		try (OutputStream os = new FileOutputStream(outputFile)) {
			reader.outputImage(os, 0, new FileFormatOption(FileFormat.PNG));
		}
	}
}