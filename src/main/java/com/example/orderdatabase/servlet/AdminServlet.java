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
            if (action == null) action = "/listUsers"; // default action
            switch (action) {
                case "/listUsers":
                    showUserList(request, response);
                    break;
                case "/editUser":
                    editUser(request, response);
                    break;
                case "/adminConsole":
                    request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    insertOrUpdateUser(request, response);
                    break;
                // Add more cases if necessary for other POST actions
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("allUsers", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
        dispatcher.forward(request, response);
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long userId = Long.parseLong(request.getParameter("userId"));
        User existingUser = userDAO.selectUserById(userId);
        request.setAttribute("user", existingUser);
        showUserList(request, response);
    }

    private void insertOrUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        // Retrieve role from the request, with a default value for regular users
        int roleId = Integer.parseInt(request.getParameter("role") != null ? request.getParameter("role") : "2");

        String hashedPassword = (password != null && !password.trim().isEmpty()) ? BCrypt.hashpw(password, BCrypt.gensalt()) : null;

        User user;
        if (id == null || id.isEmpty()) {
            // New user registration
            user = new User(username, fullName, email); // Initialize with other parameters as needed
            if (hashedPassword != null) {
                userDAO.insertUser(user, hashedPassword, roleId); // Pass roleId here
            }
        } else {
            // Update existing user
            user = userDAO.selectUserById(Long.parseLong(id));
            user.setUsername(username);
            user.setFullName(fullName);
            user.setEmail(email);
            // Set other fields on the user object as necessary

            if (hashedPassword != null) {
                userDAO.updatePassword(user.getId(), hashedPassword);
            }
            userDAO.updateUser(user);
        }
        response.sendRedirect(request.getContextPath() + "/admin/listUsers");
    }


    // ...
}
