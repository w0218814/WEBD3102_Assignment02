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
        this.userDAO = new UserDAO(); // Initialize your UserDAO here
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
            throw new ServletException("Database error: " + ex.getMessage(), ex);
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
                    // Assuming you have a registration page setup
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                    break;
                default:
                    redirectToLogin(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error processing request: " + ex.getMessage(), ex);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User(username, hashedPassword, fullName, email);
        userDAO.insertUser(newUser);
        response.sendRedirect(request.getContextPath() + "/user/login");
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.checkLogin(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Fetching the role name for the user and logging it for debugging
            String roleName = user.getRoleName();
            System.out.println("Logged in user role: " + roleName); // Debugging line to check the role

            // Redirect based on the role name
            if ("admin".equalsIgnoreCase(roleName)) {
                response.sendRedirect(request.getContextPath() + "/admin/console");
            } else if ("user".equalsIgnoreCase(roleName)) {
                response.sendRedirect(request.getContextPath() + "/todo/list");
            } else {
                // If the role is not recognized, or if there's any issue, redirect back to the login page
                System.out.println("Role name not recognized or user not assigned a role: " + roleName); // Additional debugging
                response.sendRedirect(request.getContextPath() + "/user/login");
            }
        } else {
            // If login fails, set an error message and forward back to the login page
            request.setAttribute("message", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }



    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            user.setFullName(request.getParameter("fullName"));
            user.setEmail(request.getParameter("email"));
            boolean updated = userDAO.updateUser(user);
            if (updated) {
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/user/profile");
            } else {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void changeUserPassword(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String newPassword = request.getParameter("newPassword");
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            boolean passwordChanged = userDAO.updatePassword(user.getId(), hashedNewPassword);
            if (passwordChanged) {
                response.sendRedirect(request.getContextPath() + "/user/profile");
            } else {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
