package com.example.orderdatabase.servlet;

// Imports for servlet functionality, database access, model handling, and security hashing
import com.example.orderdatabase.dao.UserDAO;
import com.example.orderdatabase.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

// Define servlet URL pattern for admin-related actions
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin/*"})
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;

    // Servlet initialization: Instantiate UserDAO for database operations
    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    // Handle GET requests: Direct requests to appropriate methods based on URL path
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extract the action from the request URL
        String action = request.getPathInfo();
        try {
            // Default to listing users if no specific action is provided
            if (action == null) {
                action = "/listUsers";
            }
            // Route to the appropriate method based on the action
            switch (action) {
                case "/listUsers":
                    showUserList(request, response);
                    break;
                case "/editUser":
                    editUser(request, response);
                    break;
                case "/adminConsole":
                    // Forward to admin console JSP page
                    request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp").forward(request, response);
                    break;
                // Placeholder for additional GET actions
            }
        } catch (SQLException ex) {
            // Handle database exceptions by throwing a ServletException
            throw new ServletException("Database error occurred", ex);
        }
    }

    // Handle POST requests: Process form submissions and database updates
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extract the action from the request URL
        String action = request.getPathInfo();
        try {
            // Route to the appropriate method based on the action
            switch (action) {
                case "/register":
                    insertOrUpdateUser(request, response);
                    break;
                // Placeholder for additional POST actions
            }
        } catch (SQLException ex) {
            // Handle database exceptions by throwing a ServletException
            throw new ServletException("Database error occurred", ex);
        }
    }
    // Display the list of all users by fetching from the database and forwarding to JSP
    private void showUserList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> listUser = userDAO.selectAllUsers(); // Fetch all users
        request.setAttribute("allUsers", listUser); // Set users in request scope for JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
        dispatcher.forward(request, response); // Forward to JSP for display
    }

    // Handle editing a specific user, selected by their ID, and show in the user list
    private void editUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String userIdStr = request.getParameter("userId"); // Extract user ID from request
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                long userId = Long.parseLong(userIdStr); // Convert ID to long
                User user = userDAO.selectUserById(userId); // Fetch user by ID
                request.setAttribute("user", user); // Set user in request scope for editing
            } catch (NumberFormatException e) {
                // Error handling for invalid user ID format
            }
        }
        showUserList(request, response); // Refresh user list, including the edited user
    }

    // Insert a new user or update an existing user's details based on the form submission
    private void insertOrUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("id"); // Extract user ID, if present
        long id = 0;
        boolean idProvided = false;
        if (idStr != null && !idStr.isEmpty()) {
            try {
                id = Long.parseLong(idStr); // Convert ID to long, indicating update operation
                idProvided = true;
            } catch (NumberFormatException e) {
                // Error handling for invalid ID format
            }
        }

        // Extract user details from request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        // Extract additional user details
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String nearbyLandmark = request.getParameter("nearbyLandmark");
        String province = request.getParameter("province");
        String postalCode = request.getParameter("postalCode");
        String phoneNumber = request.getParameter("phoneNumber");

        // Default to a standard role, unless overridden
        int roleId = 2; // Default role ID
        try {
            roleId = Integer.parseInt(request.getParameter("roleId")); // Attempt to extract role ID
        } catch (NumberFormatException e) {
            // Handle invalid format for role ID
        }

        // Secure password hashing
        String hashedPassword = "";
        if (password != null && !password.trim().isEmpty()) {
            hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // Hash the password
        }

        // Populate a User object with the extracted information
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setStreet(street);
        user.setCity(city);
        user.setNearbyLandmark(nearbyLandmark);
        user.setProvince(province);
        user.setPostalCode(postalCode);
        user.setPhoneNumber(phoneNumber);
        user.setRoleId(roleId);

        // Decide whether to insert a new user or update an existing one
        if (idProvided) {
            user.setId(id); // Set the user's ID for an update operation
            userDAO.updateUser(user); // Update the user
            if (!hashedPassword.isEmpty()) {
                userDAO.updatePassword(id, hashedPassword); // Update password separately if provided
            }
        } else {
            userDAO.insertUser(user, hashedPassword, roleId); // Insert a new user
        }

        // Redirect to the user list page after the insert/update operation
        response.sendRedirect(request.getContextPath() + "/admin/listUsers");
    }
}
