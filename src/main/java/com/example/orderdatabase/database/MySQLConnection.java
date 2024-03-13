package com.example.orderdatabase.database;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnection {

    // Specifies the JNDI name for the JDBC resource as configured in the server environment (e.g., GlassFish).
    private static final String JNDI_NAME = "java:comp/env/jdbc/myDataSource";

    // Attempts to establish a connection with the MySQL database using a connection pool.
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Initialize the context for performing the JNDI lookup to find the DataSource.
            InitialContext context = new InitialContext();
            // Look up the DataSource using the JNDI name, which represents a database connection pool.
            DataSource dataSource = (DataSource) context.lookup(JNDI_NAME);

            // Request a database connection from the DataSource's connection pool.
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            // Logs and handles the exception if the JNDI lookup fails, indicating a potential issue with resource configuration.
            System.err.println("JNDI Lookup failed for the JDBC Resource: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            // Logs and handles the exception if acquiring a connection from the pool fails, indicating a potential issue with the database connectivity.
            System.err.println("Failed to get a connection from the pool: " + e.getMessage());
            e.printStackTrace();
        }
        // Returns the database connection object; may return null if establishing a connection fails.
        return connection;
    }
}
