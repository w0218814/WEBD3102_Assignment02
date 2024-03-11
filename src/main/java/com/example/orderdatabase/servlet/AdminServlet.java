package com.example.orderdatabase.servlet;

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

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin/*"})
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            if ("/editUser".equals(action)) {
                editUser(request, response);
            } else {
                showUserList(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String userId = request.getParameter("userId");
        if (userId != null && !userId.isEmpty()) {
            User existingUser = userDAO.selectUserById(Long.parseLong(userId));
            request.setAttribute("user", existingUser);
        }
        showUserList(request, response);
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("allUsers", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            if ("/register".equals(action)) {
                insertOrUpdateUser(request, response);
            }
            // Handle other post actions if necessary
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
    }

    private void insertOrUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");

        // Ensure the password is not null before attempting to hash it
        String hashedPassword = (password != null && !password.trim().isEmpty()) ? BCrypt.hashpw(password, BCrypt.gensalt()) : null;

        User user;
        if (id == null || id.isEmpty()) {
            // New user registration
            user = new User(username, fullName, email); // roleId is already set to 2 in the User constructor
            if (hashedPassword != null) { // Ensure hashedPassword is not null
                userDAO.insertUser(user, hashedPassword); // roleId is now part of the user object
            } else {
                // Handle the case where the password is null
                // You might want to log an error or return an error message to the user
            }
        } else {
            // User update
            user = userDAO.selectUserById(Long.parseLong(id)); // Retrieve the existing user from the database
            user.setUsername(username);
            user.setFullName(fullName);
            user.setEmail(email);
            if (hashedPassword != null) {
                // Update the user's password only if a new password is provided
                userDAO.updatePassword(user.getId(), hashedPassword);
            }
            // Update the rest of the user's information
            userDAO.updateUser(user);
        }
        // Redirect or forward to the appropriate page
        response.sendRedirect(request.getContextPath() + "/admin/userList");
    }

}

