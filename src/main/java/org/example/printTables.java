package org.example;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class printTables {
    public static void main() {
        // Adapt the following variables to your database.
        String host = "localhost"; //host is "localhost" or "127.0.0.1"
        String port = "3306"; //port is where to communicate with the RDBMS
        String database = "ProjectDB"; //database containing tables to be queried
        String cp = "utf8"; //Database codepage supporting Danish (i.e. æøåÆØÅ)
        // Set username and password.
        String username = "root";        // Username for connection
        String password = "Williamersej123";    // Password for username


        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=" + cp;
        try {
            // Get a connection to the database.
            Connection connection = DriverManager.getConnection(url, username, password);

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSetTables = databaseMetaData.getTables(null, null, null, types);
            // For each table
            while (resultSetTables.next()) {
                String tableName = resultSetTables.getString(3);
                System.out.println("\n=== TABLE: " + tableName);
                ResultSet resultSetColumns = databaseMetaData.getColumns(null, null, tableName, null);
                ResultSet resultSetPrimaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName);
                // For each attribute in the table
                while (resultSetColumns.next()) {
                    String columnName = resultSetColumns.getString("COLUMN_NAME");
                    String columnType = resultSetColumns.getString("TYPE_NAME");
                    int columnSize = resultSetColumns.getInt("COLUMN_SIZE");
                    System.out.println("\t" + columnName + " - " + columnType + "(" + columnSize + ")");
                }
                // For each primary key in the table
                while (resultSetPrimaryKeys.next()) {
                    String primaryKeyColumn = resultSetPrimaryKeys.getString("COLUMN_NAME");
                    System.out.println("\tPrimary Key Column: " + primaryKeyColumn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
