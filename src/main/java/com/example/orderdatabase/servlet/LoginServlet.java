package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.UserDAO;
import com.example.orderdatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        // Initialize the UserDAO when the servlet is initialized
        super.init();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Serve only the login page or directly allow /user/register access
        if (request.getRequestURI().endsWith("/user/register")) {
            // If the request is for user registration, redirect to the registration page
            response.sendRedirect(request.getContextPath() + "/user/register");
        } else {
            // For all other requests, forward to the login page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract username and password from the request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Attempt to authenticate the user
            User user = userDAO.checkLogin(username, password);
            if (user != null) {
                // If authentication is successful, create a session and set user attribute
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if ("admin".equalsIgnoreCase(user.getRoleName())) {
                    // If the user is an admin, forward to the admin console view
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp");
                    dispatcher.forward(request, response);
                } else if ("user".equalsIgnoreCase(user.getRoleName())) {
                    // If the user is a regular user, redirect to the user todo list view
                    response.sendRedirect(request.getContextPath() + "/todo/list");
                } else {
                    // Handle unknown role or redirect to an error page or login page with an error message
                    response.sendRedirect(request.getContextPath() + "/login?error=Role not recognized");
                }
            } else {
                // Redirect back to login with error message for invalid credentials
                response.sendRedirect(request.getContextPath() + "/login?error=Invalid credentials");
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the login process
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during login.");
        }
    }
}
