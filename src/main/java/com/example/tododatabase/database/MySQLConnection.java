package com.example.tododatabase.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    // Database URL, username and password
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/tododatabase";
    private static final String DATABASE_USER = "yourUsername";
    private static final String DATABASE_PASSWORD = "yourPassword";

    // JDBC driver name
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (ClassNotFoundException e) {
            // Handle error for Class.forName
            e.printStackTrace();
        } catch (SQLException e) {
            // Handle errors for JDBC
            e.printStackTrace();
        }
        return connection;
    }
}
