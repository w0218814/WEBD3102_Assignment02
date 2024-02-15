package com.example.tododatabase.database;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnection {

    // JNDI name that you configured in GlassFish for the JDBC Resource
    private static final String JNDI_NAME = "java:comp/env/jdbc/myDataSource";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Obtain the DataSource from JNDI
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup(JNDI_NAME);

            // Get a connection from the pool
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            System.err.println("JNDI Lookup failed for the JDBC Resource: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to get a connection from the pool: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}
