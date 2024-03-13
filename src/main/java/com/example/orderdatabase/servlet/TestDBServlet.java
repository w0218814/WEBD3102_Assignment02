package com.example.orderdatabase.servlet;

import com.example.orderdatabase.database.MySQLConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

// Servlet to test the database connection.
@WebServlet("/TestDBServlet")
public class TestDBServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TestDBServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Sets the content type of the response to HTML.
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter(); Connection conn = MySQLConnection.getConnection()) {
            // Tries to establish a database connection.
            if (conn != null) {
                // If connection is successful, displays a success message.
                out.println("<html><body>");
                out.println("<h2>DB Connection successful!</h2>");
                // You can optionally run a simple SQL SELECT statement here to test the connection fully.
                out.println("</body></html>");
            } else {
                // If connection fails, displays an error message.
                out.println("<html><body>");
                out.println("<h2>Failed to make connection!</h2>");
                out.println("</body></html>");
            }
        } catch (SQLException e) {
            // Logs and rethrows SQLException if database connection fails.
            LOGGER.log(Level.SEVERE, "SQL Exception: Could not connect to the database", e);
            throw new ServletException("SQL Exception: Could not connect to the database", e);
        }
    }
}
