package com.example.orderdatabase.servlet;

// Necessary imports for servlet operations, database access, and model handling
import com.example.orderdatabase.dao.UserDAO;
import com.example.orderdatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Servlet annotation for defining the URL pattern for the login functionality
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO; // Declaration of UserDAO for database interactions

    // Servlet initialization: Instantiate UserDAO for database operations
    @Override
    public void init() throws ServletException {
        super.init(); // Call to the superclass's init method
        userDAO = new UserDAO(); // Initialize UserDAO instance
    }

    // Handle GET requests: Show the login page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward the request to the login.jsp page
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    // Handle POST requests: Process login attempts
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extract username and password from the request
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Attempt to authenticate the user with the provided credentials
            User user = userDAO.checkLogin(username, password);
            if (user != null) {
                // Login successful: Create or get the current session
                HttpSession session = request.getSession();
                session.setAttribute("user", user); // Store user object in session

                // Check if the user is an admin based on their role ID
                if (user.getRoleId() == 1) { // Assuming 1 is the roleId for admin
                    // Redirect admin users to the admin console
                    response.sendRedirect(request.getContextPath() + "/admin/adminConsole");
                } else {
                    // Handle login for regular users
                    // Check for a pending product ID that might have been saved before login
                    String pendingProductId = (String) session.getAttribute("pendingProductId");
                    if (pendingProductId != null) {
                        // If there is a pending product, clear it and redirect to confirm the order
                        session.removeAttribute("pendingProductId");
                        response.sendRedirect(request.getContextPath() + "/product/list?confirmOrder=" + pendingProductId);
                    } else {
                        // Redirect to product listing if there's no pending action
                        response.sendRedirect(request.getContextPath() + "/product/list");
                    }
                }
            } else {
                // Login failed: Set error message and show the login page again
                request.setAttribute("errorMessage", "Invalid Username or Password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // General error handling for login failures
            throw new ServletException("Login failed due to an error: " + e.getMessage(), e);
        }
    }
}
