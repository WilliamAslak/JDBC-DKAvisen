package org.example;

import java.sql.*;
import java.util.Scanner;

public class printReporters {

    public static void main() {
        // Adapt the following variables to your database.
        String host = "localhost"; //host is "localhost" or "127.0.0.1"
        String port = "3306"; //port is where to communicate with the RDBMS
        String database = "ProjectDB"; //database containing tables to be queried
        String cp = "utf8"; //Database codepage supporting Danish (i.e. æøåÆØÅ)
        // Set username and password.
        String username = "root";		// Username for connection
        String password = "Williamersej123";	// Password for username



        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=" + cp;
        try {

            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Reporter");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            // Print all attribute names.
            // --------------------------
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSetMetaData.getColumnName(i)+"; ");
            }
            System.out.println();
            System.out.println("------");

            // Print all table rows.
            // ---------------------
            resultSet.beforeFirst(); // Set pointer for resultSet.next()
            while (resultSet.next()) {
                // Print all values in a row.
                // --------------------------
                for (int i = 1; i <= columnCount; i++) {
                    if (resultSet.getString(i) == null) {
                        System.out.print("null; ");
                    } else {
                        System.out.print(resultSet.getString(i)+"; ");
                    };
                }
                System.out.println();
            }
            connection.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

}