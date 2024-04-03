package org.example;

import org.example.models.Address;

import java.sql.*;

import static org.example.printStuff.*;
import static org.example.DataInserter.insertReporter;
import static org.example.DataInserter.insertPhoto;

public class Main {

        public static void main(String[] args) {
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

                //printAllTableInfo(connection);



    //            Address tempAddress = new Address("Abildgaardsvej", "139", "2830", "Virum");
  //              insertReporter("Barrack", "Obama", "CPR1624618", tempAddress, "25856499","WhiteHouse@gmail.com",connection);

                printSpecificTable("address",connection);

                connection.close();
            }
            catch (Exception e) { e.printStackTrace(); }
        }

}