package org.example;

import java.sql.*;

public class printStuff {

    public static void printSpecificTable(String table, Connection connection) {
       try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM "+table);
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
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void printAllTableInfo(Connection connection) {

        try {
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
