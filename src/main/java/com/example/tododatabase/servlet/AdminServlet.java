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
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            if ("/editUser".equals(action)) {
                editUser(request, response);
            } else {
                // Handle other actions or show the default page
                showUserList(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String userId = request.getParameter("userId");
        if (userId != null && !userId.isEmpty()) {
            User existingUser = userDAO.selectUserById(Long.parseLong(userId));
            request.setAttribute("user", existingUser);
        }
        showUserList(request, response);
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("allUsers", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        // Convert roleName to roleId. Implement this method based on your role management.
        int roleId = convertRoleNameToRoleId(roleName);

        User user = new User(username, fullName, email);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (id == null || id.isEmpty()) {
            userDAO.insertUser(user, hashedPassword, roleId);
        } else {
            user.setId(Long.parseLong(id));
            if (password != null && !password.trim().isEmpty()) {
                userDAO.updatePassword(user.getId(), password);
            }
            userDAO.updateUser(user, roleId);
        }
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
