package org.example;

import org.example.models.Address;
import org.example.models.Photo;
import org.example.models.PhotoAndReporter;
import org.example.models.Reporter;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import static org.example.DataInserter.insertReporter;
import static org.example.DataInserter.insertPhoto;

public class loadCSV {
    public static void loadCSVtoSQL(String filename,Connection conn) {
        PhotosAndReportersLoader loader = new PhotosAndReportersLoader();
        String path = "src/main/java/org/example/"+filename+".csv";
        try {
            System.out.println("loading from "+path);
            List<PhotoAndReporter> photosAndReporters = loader.loadPhotosAndReporters(path);
            for(PhotoAndReporter photoAndReporter : photosAndReporters) {
                //we need to insert the reporter first since the foreign key in photo references the reporter.
                Reporter r = photoAndReporter.getReporter();
                Address address = new Address(r.getStreetName(),String.valueOf(r.getCivicNumber()),String.valueOf(r.getZIPCode()),r.getCity());
                Photo p = photoAndReporter.getPhoto();
                insertReporter(r.getFirstName(),r.getLastName(),"CPR"+r.getCPR(),address,null,null, conn);
                insertPhoto(p.getTitle(),p.getDate(),null, "CPR"+r.getCPR(), conn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
