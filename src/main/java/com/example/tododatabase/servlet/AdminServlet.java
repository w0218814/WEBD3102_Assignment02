package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.UserDAO;
import com.example.tododatabase.model.User;
import jakarta.servlet.RequestDispatcher;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
                String userId = request.getParameter("userId");
                if (userId != null && !userId.isEmpty()) {
                    User existingUser = userDAO.selectUserById(Long.parseLong(userId));
                    request.setAttribute("user", existingUser);
                }
                List<User> listUser = userDAO.selectAllUsers();
                request.setAttribute("allUsers", listUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
                dispatcher.forward(request, response);
            } else {
                // Handle other actions or show the default page
                List<User> listUser = userDAO.selectAllUsers();
                request.setAttribute("allUsers", listUser);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
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

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Fetch all users for the dropdown
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("allUsers", listUser);

        // Check if an edit is requested
        String userId = request.getParameter("userId");
        if (userId != null) {
            User existingUser = userDAO.selectUserById(Long.parseLong(userId));
            request.setAttribute("user", existingUser);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
        dispatcher.forward(request, response);
    }

    private void insertOrUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        // Assume a default role ID for simplicity, you should get this from the form or context
        int roleId = 2; // Default role ID

        User user = new User(username, fullName, email);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (id == null || id.isEmpty()) {
            userDAO.insertUser(user, hashedPassword, roleId);
            // After a successful registration, redirect to a confirmation page or user list
            response.sendRedirect(request.getContextPath() + "/admin/userList"); // Replace with your valid URL
        } else {
            user.setId(Long.parseLong(id));
            // If password is not provided or is empty, don't update it
            if (password != null && !password.trim().isEmpty()) {
                userDAO.updatePassword(user.getId(), password);
            }
            boolean updated = userDAO.updateUser(user);
            if (updated) {
                // Redirect to a confirmation page or back to the user list after an update
                response.sendRedirect(request.getContextPath() + "/admin/userList"); // Replace with your valid URL
            } else {
                // Handle the case where the user update fails
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to update user.");
            }
        }
    }
}