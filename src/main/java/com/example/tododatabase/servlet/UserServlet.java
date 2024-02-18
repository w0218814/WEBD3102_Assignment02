package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.UserDAO;
import com.example.tododatabase.model.User;
import org.mindrot.jbcrypt.BCrypt;
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
        this.userDAO = new UserDAO(); // Ensure this is correctly connected to your DB
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/register":
                    registerUser(request, response);
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
            handleError(request, response, "Database error", ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/logout":
                    logoutUser(request, response);
                    break;
                case "/register":
                    showRegistrationForm(request, response);
                    break;
                case "/profile":
                    showProfile(request, response);
                    break;
                case "/adminConsole":
                    showAdminDashboard(request, response);
                    break;
                default:
                    redirectToLogin(request, response);
                    break;
            }
        } catch (Exception ex) {
            handleError(request, response, "Error processing request", ex.getMessage());
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User(username, fullName, email);
        userDAO.insertUser(newUser, hashedPassword);
        response.sendRedirect(request.getContextPath() + "/user/login");
    }


    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException { // Now also declares ServletException
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            user.setFullName(fullName);
            user.setEmail(email);

            boolean updated = userDAO.updateUser(user);
            if (updated) {
                session.setAttribute("user", user); // Refresh session user data
                response.sendRedirect(request.getContextPath() + "/user/profile");
            } else {
                handleError(request, response, "Update Failed", "Unable to update user profile.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }


    private void changeUserPassword(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException { // Add ServletException here
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String newPassword = request.getParameter("newPassword");
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            boolean passwordChanged = userDAO.updatePassword(user.getId(), hashedNewPassword);
            if (passwordChanged) {
                response.sendRedirect(request.getContextPath() + "/user/profile");
            } else {
                handleError(request, response, "Password Change Failed", "Unable to change password.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    private void showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        } else {
            redirectToLogin(request, response);
        }
    }

    private void showAdminDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp").forward(request, response);
        } else {
            redirectToLogin(request, response);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String title, String message)
            throws ServletException, IOException { // Declare that it throws ServletException and IOException
        request.setAttribute("title", title);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}