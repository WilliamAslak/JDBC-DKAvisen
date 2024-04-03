package org.example;
import org.example.models.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class DataInserter {

    public static void insertPhoto(String title, String date, String photoId, String reporterCPR, String articleId, Connection conn) {
        String sql = "INSERT INTO photo (Photo_Title, Photo_Date, Photo_id, Reporter_CPR, Article_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, date);
            pstmt.setString(3, photoId);
            pstmt.setString(4, reporterCPR);
            pstmt.setString(5, articleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Successfully created "+firstName + " " + lastName);
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Successfully created address with ID: "+id);
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

    public static String IdGenerator(String table, Connection conn){
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Available characters to choose from
        int maxlength = 9; // For 1-9 random numbers

        Random random = new Random(); // To ensure users can't predict and reverse engineer the data.
        String generatedString;

        do {
            StringBuilder builder = new StringBuilder();

            // The format for ID should be "capital letter" followed by 1-9 random integers.
            builder.append(letters.charAt(random.nextInt(letters.length()))); // Appends random char from letter string
            // nextInt(9) will output a random number between 0-8, so +1 makes it 1-9
            for(int i = 0; i < random.nextInt(maxlength)+1; i++)
                builder.append(random.nextInt(10));
            generatedString = builder.toString();
            // possible idea is to implement a counter to ensure it doesnt run infinitely.
        } while(IDExists(table, generatedString,conn));
        // Once we reach this point that means it has found a unique id.
        return generatedString;
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
