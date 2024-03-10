package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.UserDAO;
import com.example.orderdatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
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

                // Redirect to OrderForm.jsp if there's a pending order
                Long pendingProductId = (Long) session.getAttribute("pendingProductId");
                if (pendingProductId != null) {
                    // Remove pendingProductId from session to clean up
                    session.removeAttribute("pendingProductId");
                    response.sendRedirect(request.getContextPath() + "/orderForm.jsp?productId=" + pendingProductId);
                } else {
                    // Default redirection if no pending product
                    response.sendRedirect(request.getContextPath() + "/defaultRedirectPage.jsp");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/login?error=Invalid credentials");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during login.");
        }
    }
}
