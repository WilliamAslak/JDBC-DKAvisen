package org.example;
import org.example.models.Address;
import org.example.models.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.Random;

public class DataInserter {

    public static void insertPhoto(String title, Date date, String photoId, String reporterCPR, Connection conn) {
        String sql = "INSERT INTO photo (Photo_Title, Photo_Date, Photo_id, Reporter_CPR) VALUES (?, ?, ?, ?)";
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        //this is due to the fact that id is a kinda funky way to check uniqueness here.
        if (!PhotoExists(title,dateFormatter.format(date), reporterCPR, conn)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, dateFormatter.format(date));
                pstmt.setString(3, IdGenerator("Photo", conn));
                pstmt.setString(4, reporterCPR);
                pstmt.executeUpdate();
                System.out.println("Successfully created photo " + title);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else System.out.println("Duplicate entry for "+title);

    }

    public static void insertReporter(String firstName, String lastName, String cpr, Address address, String phone, String email, Connection conn) {
        String sql = "INSERT INTO reporter (Reporter_CPR, F_name, S_name, Address_id, Tlf, Email) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpr);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, CheckOrGenerateAddressID(address, conn));
            pstmt.setString(5, phone);
            pstmt.setString(6, email);
            pstmt.executeUpdate();
            System.out.println("Successfully created "+firstName + " " + lastName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void insertAddress(String street, String civic, String city, String zip, String country, String id, Connection conn) {
        String sql = "INSERT INTO address (Street_name, Civic_nr, City_name, Zipcode, Country, Address_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, street);
            pstmt.setString(2, civic);
            pstmt.setString(3, city);
            pstmt.setString(4, zip);
            pstmt.setString(5, country);
            pstmt.setString(6, id);
            pstmt.executeUpdate();
            System.out.println("Successfully created address with ID: "+id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public static String CheckOrGenerateAddressID(Address address, Connection conn){
        String sql = "SELECT Address_id FROM address WHERE Street_name = ? AND Civic_nr = ? AND Zipcode = ? AND City_name = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, address.getStreet());
            pstmt.setString(2, address.getCivic());
            pstmt.setString(3, address.getZIP());
            pstmt.setString(4, address.getCity());

            // Execute the query and check the result
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Address_id"); // Return address_id if the address is found, otherwise generate one.
                }
            }
            // The address was not found, so we will put a new one into the database.
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        String generatedID = IdGenerator("address", conn);
        if(!IDExists("address",generatedID,conn))
            insertAddress(address.getStreet(), address.getCivic(), address.getCity(), address.getZIP(), "Denmark", generatedID, conn);

        return generatedID;
    }
    public static boolean PhotoExists(String title, String date, String reporterCPR, Connection conn){
        String sql = "SELECT Photo_id FROM photo WHERE Photo_title = ? AND Photo_date = ? AND Reporter_CPR = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, title);
            pstmt.setString(2, date);
            pstmt.setString(3, reporterCPR);

            // Execute the query and check the result
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
            // The address was not found, so we will put a new one into the database.
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        // If query failed default to false.
        return false;
    }


    //Generates a unique id by getting a random number and then checking the sql database if the object with that id exists.
    public static String IdGenerator(String table, Connection conn){
        int maxlength = 11; // For 1-9 random numbers

        Random random = new Random(); // To ensure users can't predict and reverse engineer the data.
        String generatedID;
        do {
            generatedID = String.valueOf(random.nextInt((int) Math.pow(10,maxlength)));
        } while(IDExists(table, generatedID,conn));
        // Once we reach this point that means it has found a unique id.
        return generatedID;
    }

    public static boolean IDExists(String table, String ID, Connection conn) {

        String IDTag = table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase() + "_id";
        String sql = "SELECT COUNT(*) FROM "+table+" WHERE "+IDTag+" = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        System.out.println("Id doesn't exist");
        return false; // Default to false if the query fails
    }
}
