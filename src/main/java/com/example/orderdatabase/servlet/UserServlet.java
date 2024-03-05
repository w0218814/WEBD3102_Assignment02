package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.UserDAO;
import com.example.orderdatabase.model.User;
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

    public void init() {
        // Initialize the UserDAO when the servlet starts
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST requests
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    // Handle user registration
                    registerUser(request, response);
                    break;
                case "/login":
                    // Handle user login
                    authenticateUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle GET requests
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    // Show registration page
                    showRegisterPage(request, response);
                    break;
                case "/logout":
                    // Handle user logout
                    logoutUser(request, response);
                    break;
                case "/login":
                    // Show login page
                    showLoginPage(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward the request to the register.jsp page located in WEB-INF/views directory
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        // Extract registration information from request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        int roleId = Integer.parseInt(request.getParameter("role"));
        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a new User object with registration information
        User newUser = new User(username, fullName, email);
        // Insert the user into the database
        userDAO.insertUser(newUser, hashedPassword, roleId);

        // Redirect to the login page after successful registration
        response.sendRedirect(request.getContextPath() + "/user/login");
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        // Extract login information from request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check if the provided credentials are valid
        User user = userDAO.checkLogin(username, password);
        if (user != null) {
            // If valid, create a session and redirect to the todo list page
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // Set user in session
            response.sendRedirect(request.getContextPath() + "/todo/list");
        } else {
            // If invalid, set an error message and forward back to the login page
            request.setAttribute("message", "Invalid username or password");
            request.getRequestDispatcher("/user/login").forward(request, response);
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Invalidate the session and redirect to the login page
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user"); // Remove user from session
            session.invalidate(); // Invalidate session
        }
        response.sendRedirect(request.getContextPath() + "/user/login");
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward the request to the login.jsp page located in WEB-INF/views directory
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
