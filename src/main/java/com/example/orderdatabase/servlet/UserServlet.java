package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.UserDAO;
import com.example.orderdatabase.model.User;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

// Defines this class as a servlet handling requests with URL patterns matching "/user/*".
@WebServlet(name = "UserServlet", urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {
    private UserDAO userDAO; // DAO for user data interaction.

    @Override
    public void init() {
        // Initializes the UserDAO instance required for user-related operations.
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handles HTTP POST requests, determining the specific action based on the URL path info.
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    // Registers a new user.
                    registerUser(request, response);
                    break;
                case "/login":
                    // Handles user login (code for login functionality will go here).
                    break;
                // Additional POST actions can be handled here.
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handles HTTP GET requests, determining the specific action based on the URL path info.
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    // Displays the registration page.
                    showRegisterPage(request, response);
                    break;
                case "/logout":
                    // Handles user logout (code for logout functionality will go here).
                    break;
                // Additional GET actions can be handled here.
            }
        } catch (Exception ex) {
            throw new ServletException("Error: " + ex.getMessage(), ex);
        }
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forwards to the registration JSP page.
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        // Extracts form data for user registration.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String nearbyLandmark = request.getParameter("nearbyLandmark");
        String province = request.getParameter("province");
        String postalCode = request.getParameter("postalCode");
        String phoneNumber = request.getParameter("phoneNumber");

        // Hashes the password using BCrypt.
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Creates a new User object and sets all fields.
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setStreet(street);
        newUser.setCity(city);
        newUser.setNearbyLandmark(nearbyLandmark);
        newUser.setProvince(province);
        newUser.setPostalCode(postalCode);
        newUser.setPhoneNumber(phoneNumber);

        // Sets the default role ID for regular users (change as per your role system).
        int defaultRoleId = 2;

        // Inserts the new user into the database.
        userDAO.insertUser(newUser, hashedPassword, defaultRoleId);

        // Redirects to the login page or a confirmation page.
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
