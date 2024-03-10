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
        String roleName = request.getParameter("roleName");

        int roleId = convertRoleNameToRoleId(roleName);

        // Ensure the password is not null before attempting to hash it
        String hashedPassword = (password != null && !password.trim().isEmpty()) ? BCrypt.hashpw(password, BCrypt.gensalt()) : null;

        // Assuming your User model can handle a null password or you ensure hashedPassword is not null before proceeding
        User user = new User(username, fullName, email); // Here we use the constructor with three parameters
        // The rest of your logic for inserting or updating the user

        if (id == null || id.isEmpty()) {
            // New user registration
            if (hashedPassword != null) { // Ensure hashedPassword is not null
                userDAO.insertUser(user, hashedPassword, roleId);
            } else {
                // Handle the case where the password is null
                // You might want to log an error or return an error message to the user
            }
        } else {
            // User update
            user.setId(Long.parseLong(id));
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
