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

@WebServlet("/TestDBServlet")
public class TestDBServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TestDBServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter(); Connection conn = MySQLConnection.getConnection()) {
            if (conn != null) {
                out.println("<html><body>");
                out.println("<h2>DB Connection successful!</h2>");
                // Maybe run a simple SQL SELECT statement here to test the connection fully
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h2>Failed to make connection!</h2>");
                out.println("</body></html>");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Exception: Could not connect to the database", e);
            throw new ServletException("SQL Exception: Could not connect to the database", e);
        }
    }
}
