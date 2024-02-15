package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.UserDAO;
import com.example.tododatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/register":
                    registerUser(request, response);
                    break;
                case "/login":
                    loginUser(request, response);
                    break;
                case "/update":
                    updateUserProfile(request, response);
                    break;
                case "/changePassword":
                    changeUserPassword(request, response);
                    break;
                default:
                    redirectToLogin(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/logout":
                    logoutUser(request, response);
                    break;
                case "/register":
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                    break;
                default:
                    redirectToLogin(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error processing request", ex);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // TODO: Hash the password for security
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");

        User newUser = new User(username, password, fullName, email);
        userDAO.insertUser(newUser);
        response.sendRedirect(request.getContextPath() + "/user/login"); // Redirect to login page after registration
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // TODO: Hash the password check for security

        User user = userDAO.checkLogin(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath()+"/todo/list"); // Redirect to todo list after login
        } else {
            request.setAttribute("message", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
// Fetch user from session and update profile information
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");

            // Update the user object with new profile information
            user.setFullName(request.getParameter("fullName"));
            user.setEmail(request.getParameter("email"));
            // Here, you could add more fields to be updated as needed

            // Update user in the database
            boolean updated = userDAO.updateUser(user);
            if(updated) {
                // Update successful, redirect to profile page
                response.sendRedirect(request.getContextPath() + "/user/profile");
            } else {
                // Update failed, redirect to an error page or display a message
            }
        } else {
            // No user in session, redirect to login page
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void changeUserPassword(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
// Fetch user from session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");

            // TODO: Validate current password, hash new password
            boolean passwordChanged = userDAO.updatePassword(user.getId(), newPassword);
            if(passwordChanged) {
                // Password changed successfully, redirect to profile page
                response.sendRedirect(request.getContextPath() + "/user/profile");
            } else {
                // Password change failed, redirect to an error page or display a message
            }
        } else {
            // No user in session, redirect to login page
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the session to logout the user
        }
        response.sendRedirect(request.getContextPath() + "/user/login"); // Redirect to login page after logout
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    private void handleError(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
    }
}