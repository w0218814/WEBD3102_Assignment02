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
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = {"/user/*"})
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
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
                case "/editUser":
                    editUser(request, response);
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
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String roleName = request.getParameter("role"); // This will be 'admin' or 'user'
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Convert role name to role ID
        int roleId = "admin".equalsIgnoreCase(roleName) ? 1 : 2; // Use 1 for admin, 2 for user

        try {
            User newUser = new User(0, username, fullName, email, roleName); // 0 for ID as it will be auto-generated
            userDAO.insertUser(newUser, hashedPassword, roleId); // Pass roleId to insertUser method
            response.sendRedirect(request.getContextPath() + "/user/login");
        } catch (SQLException e) {
            // Handle the SQL exception
            throw new ServletException("SQL error occurred during user registration", e);
        }
    }

    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            long id = Long.parseLong(request.getParameter("id")); // Modification: Support for updating other users
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String role = request.getParameter("role"); // Modification: Support for role updates

            User user = new User(id, null, fullName, email, role); // Modified to support role updates
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
            throws SQLException, IOException, ServletException {
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

// Remaining methods (logoutUser, redirectToLogin, showRegistrationForm, editUser, showProfile, showAdminDashboard, handleError)
    // Continuation of UserServlet

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
        HttpSession session = request.getSession(false);
        User currentUser = null;
        if (session != null) {
            currentUser = (User) session.getAttribute("user");
        }

        // Check if currentUser is null or does not have a role, treat as a regular user
        if (currentUser == null || currentUser.getRoleName() == null || currentUser.getRoleName().isEmpty()) {
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } else if ("admin".equalsIgnoreCase(currentUser.getRoleName())) {
            // Existing admin logic here
            try {
                List<User> users = userDAO.selectAllUsers();
                request.setAttribute("allUsers", users); // Forward all users to the JSP for admin
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            } catch (Exception ex) {
                handleError(request, response, "Database error", "Error fetching users: " + ex.getMessage());
            }
        } else {
            // If not admin but has a role, redirect or show a different page
            response.sendRedirect(request.getContextPath() + "/user/profile");
        }
    }



    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long userId = Long.parseLong(request.getParameter("userId"));
        try {
            User user = userDAO.selectUserById(userId);
            request.setAttribute("user", user);
            showRegistrationForm(request, response);
        } catch (SQLException ex) {
            handleError(request, response, "Database error", "Error fetching user: " + ex.getMessage());
        }
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
        if (session != null && "admin".equalsIgnoreCase(((User) session.getAttribute("user")).getRoleName())) {
            request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp").forward(request, response);
        } else {
            redirectToLogin(request, response);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String title, String message)
            throws ServletException, IOException {
        request.setAttribute("title", title);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
