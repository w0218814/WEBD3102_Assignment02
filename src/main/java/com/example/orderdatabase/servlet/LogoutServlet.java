package com.example.orderdatabase.servlet;

// Necessary imports for servlet operations and session management
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Servlet annotation to define the URL pattern for logout functionality
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    // Handle GET requests to perform logout
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtain the current session associated with the request
        HttpSession session = request.getSession();
        if (session != null) {
            // Invalidate the current session to clear all session attributes
            session.invalidate();
        }

        // After invalidating the session, redirect the user to the login page
        // This ensures the user is logged out and has to authenticate again to access protected resources
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
