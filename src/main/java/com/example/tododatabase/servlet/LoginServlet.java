package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.UserDAO;
import com.example.tododatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher; // Import statement for RequestDispatcher
import java.io.IOException;


@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.checkLogin(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if ("admin".equalsIgnoreCase(user.getRoleName())) {
                    // Forward to the admin console view
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp");
                    dispatcher.forward(request, response);
                } else if ("user".equalsIgnoreCase(user.getRoleName())) {

                    // Redirect to the user todo list view
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
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during login.");
        }
    }
}
