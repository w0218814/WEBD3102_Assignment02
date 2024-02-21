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

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    registerUser(request, response);
                    break;
                case "/login":
                    authenticateUser(request, response);
                    break;
                // Additional case statements for other POST requests like updating or deleting a user
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/logout":
                    logoutUser(request, response);
                    break;
                case "/login": // Handle direct access to login page
                    showLoginPage(request, response);
                    break;
                // Additional case statements for other GET requests like showing a user profile
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        int roleId = Integer.parseInt(request.getParameter("role"));
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User(username, fullName, email);
        userDAO.insertUser(newUser, hashedPassword, roleId);

        response.sendRedirect("login"); // Redirect to login page after successful registration
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.checkLogin(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // Set user in session
            response.sendRedirect(request.getContextPath() + "/todo/list"); // Redirect to todo list
        } else {
            request.setAttribute("message", "Invalid username or password");
            request.getRequestDispatcher("/user/login").forward(request, response);
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("user"); // Remove user from session
            session.invalidate(); // Invalidate session
            response.sendRedirect(request.getContextPath() + "/user/login"); // Redirect to login page
        }
    }
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the login page located in WEB-INF/views directory
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
