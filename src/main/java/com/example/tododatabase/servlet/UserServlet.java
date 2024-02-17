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
        this.userDAO = new UserDAO(); // Ensure this is connected to your DB properly
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/logout":
                    logoutUser(request, response);
                    break;
                case "/register":
                    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
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
            throw new ServletException("Error processing request", ex);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Assuming User model has been adjusted to not include password in constructor for security reasons
        User newUser = new User(username, fullName, email); // Updated constructor
        userDAO.insertUser(newUser, hashedPassword); // Updated method to include hashed password separately
        response.sendRedirect(request.getContextPath() + "/user/login"); // Redirect after registration
    }


    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.checkLogin(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            String role = userDAO.getUserRole(user.getId());
            if ("admin".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/user/adminConsole"); // Redirect to the admin console
            } else if ("user".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/todo/list"); // Redirect to the user todo list
            } else {
                session.invalidate(); // Invalidating the session for security reasons
                request.setAttribute("message", "Access Denied: Your role is not recognized.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("message", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }


    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");

            user.setFullName(request.getParameter("fullName"));
            user.setEmail(request.getParameter("email"));
            // Assume no role update here

            boolean updated = userDAO.updateUser(user); // Ensure this method correctly updates the user
            if (updated) {
                session.setAttribute("user", user); // Update session
                response.sendRedirect(request.getContextPath() + "/user/profile"); // Redirect to user profile
            } else {
                response.sendRedirect(request.getContextPath() + "/error.jsp"); // Or handle error properly
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login"); // Redirect to login if session not found
        }
    }

    private void changeUserPassword(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String newPassword = request.getParameter("newPassword");
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            boolean passwordChanged = userDAO.updatePassword(user.getId(), hashedNewPassword); // Ensure this method exists and works
            if (passwordChanged) {
                response.sendRedirect(request.getContextPath() + "/user/profile"); // Redirect to profile
            } else {
                response.sendRedirect(request.getContextPath() + "/error.jsp"); // Or handle error
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate session
            response.sendRedirect(request.getContextPath() + "/user/login"); // Redirect to login
        }
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
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
            User user = (User) session.getAttribute("user");
            try {
                String role = userDAO.getUserRole(user.getId());
                if ("admin".equals(role)) {
                    request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp").forward(request, response);
                } else {
                    // If the user is not an admin, redirect to a different page or show an error
                    request.setAttribute("message", "Access Denied: You do not have permission to view the admin console.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                throw new ServletException("Database error while retrieving user role", e);
            }
        } else {
            redirectToLogin(request, response);
        }
    }
}
