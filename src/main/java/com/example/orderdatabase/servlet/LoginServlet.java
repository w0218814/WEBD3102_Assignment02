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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userDAO.checkLogin(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Check for pendingProductId and redirect appropriately
                String pendingProductId = (String) session.getAttribute("pendingProductId");
                if (pendingProductId != null) {
                    // Remove the attribute to clean up the session
                    session.removeAttribute("pendingProductId");
                    // Redirect to an intermediate page or servlet that confirms the order
                    // This could redirect to the product list with a flag indicating an order confirmation is needed.
                    response.sendRedirect(request.getContextPath() + "/product-list.jsp?confirmOrder=" + pendingProductId);
                } else {
                    // No pending product, redirect to a default or home page
                    response.sendRedirect(request.getContextPath() + "/product/list"); // Adjust the redirection as needed.
                }
            } else {
                // Set error message and forward back to the login page
                request.setAttribute("errorMessage", "Invalid Username or Password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Login failed due to an error: " + e.getMessage(), e);
        }
    }

}
