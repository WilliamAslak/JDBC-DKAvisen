package org.example;

import org.example.models.PhotoAndReporter;
import org.example.models.PhotosAndReportersLoader;

import java.io.IOException;
import java.util.List;

public class LoaderExample {

	public static void main(String[] args) {
		PhotosAndReportersLoader loader = new PhotosAndReportersLoader();
		String path = "src/main/java/org/example/uploads.csv";
		try {
			System.out.println("loading from "+path);
			List<PhotoAndReporter> photosAndReporters = loader.loadPhotosAndReporters(path);
			for(PhotoAndReporter photoAndReporter : photosAndReporters) {
				System.out.print("\tPhoto: " + photoAndReporter.getPhoto());
				System.out.println("\tReporter: " + photoAndReporter.getReporter());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


