package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.UserDAO;
import com.example.tododatabase.model.User;
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

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin/*"})
public class AdminServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        // Initialize the UserDAO when the servlet is initialized
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the requested action from the URL path
        String action = request.getPathInfo();
        try {
            if ("/editUser".equals(action)) {
                // If action is to edit user, call the editUser method
                editUser(request, response);
            } else {
                // Otherwise, show the list of users
                showUserList(request, response);
            }
        } catch (SQLException ex) {
            // If any SQL exception occurs, throw ServletException with a database error message
            throw new ServletException("Database error occurred", ex);
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Extract user ID from request parameter
        String userId = request.getParameter("userId");
        if (userId != null && !userId.isEmpty()) {
            // If user ID is provided, retrieve the user object by ID
            User existingUser = userDAO.selectUserById(Long.parseLong(userId));
            // Set the user attribute to be used in the JSP
            request.setAttribute("user", existingUser);
        }
        // Show the user list after editing
        showUserList(request, response);
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Retrieve all users from the database
        List<User> listUser = userDAO.selectAllUsers();
        // Set the list of users as a request attribute
        request.setAttribute("allUsers", listUser);
        // Forward the request to the JSP page responsible for displaying the user list
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the requested action from the URL path
        String action = request.getPathInfo();
        try {
            if ("/register".equals(action)) {
                // If action is to register a new user, call the insertOrUpdateUser method
                insertOrUpdateUser(request, response);
            }
            // Handle other post actions if necessary
        } catch (SQLException ex) {
            // If any SQL exception occurs, throw ServletException with a database error message
            throw new ServletException("Database error occurred", ex);
        }
    }

    private void insertOrUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // Extract user data from request parameters
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String roleName = request.getParameter("roleName");

        // Convert roleName to roleId. Implement this method based on your role management.
        int roleId = convertRoleNameToRoleId(roleName);

        // Create a new User object with the extracted data
        User user = new User(username, fullName, email);
        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Check if the user is being inserted or updated
        if (id == null || id.isEmpty()) {
            // If no ID is provided, insert a new user into the database
            userDAO.insertUser(user, hashedPassword, roleId);
        } else {
            // If an ID is provided, update the existing user
            user.setId(Long.parseLong(id));
            if (password != null && !password.trim().isEmpty()) {
                // If a new password is provided, update the password
                userDAO.updatePassword(user.getId(), password);
            }
            // Update the user's information in the database
            userDAO.updateUser(user, roleId);
        }
        // Redirect the user to the user list page after registration or update
        response.sendRedirect(request.getContextPath() + "/admin/userList");
    }

    private int convertRoleNameToRoleId(String roleName) {
        // Example conversion logic. Adjust according to your actual role IDs.
        switch (roleName) {
            case "admin":
                return 1;
            case "user":
            default:
                return 2;
        }
    }
}
