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

@WebServlet(name = "UserServlet", urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    registerUser(request, response);
                    break;
                case "/login":
                    // Code for handling user login will go here
                    break;
                // Additional POST actions can be handled here
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error: " + ex.getMessage(), ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    showRegisterPage(request, response);
                    break;
                case "/logout":
                    // Code for handling user logout will go here
                    break;
                // Additional GET actions can be handled here
            }
        } catch (Exception ex) {
            throw new ServletException("Error: " + ex.getMessage(), ex);
        }
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the registration JSP page
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        // Extract form data
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

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a new user object and set all fields
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        // Set default roleId in User object constructor or here
        newUser.setStreet(street);
        newUser.setCity(city);
        newUser.setNearbyLandmark(nearbyLandmark);
        newUser.setProvince(province);
        newUser.setPostalCode(postalCode);
        newUser.setPhoneNumber(phoneNumber);

        // Insert the new user into the database
        userDAO.insertUser(newUser, hashedPassword); // Make sure this method in UserDAO accepts a User object with all these fields

        // Redirect to login page or a confirmation page
        response.sendRedirect(request.getContextPath() + "/user/login");
    }
}
