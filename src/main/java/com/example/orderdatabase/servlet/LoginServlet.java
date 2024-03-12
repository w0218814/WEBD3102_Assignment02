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

                if (user.getRoleId() == 1) { // Assuming 1 is the roleId for admin
                    response.sendRedirect(request.getContextPath() + "/admin/adminConsole"); // Redirect to admin console
                } else {
                    // For regular users
                    String pendingProductId = (String) session.getAttribute("pendingProductId");
                    if (pendingProductId != null) {
                        session.removeAttribute("pendingProductId");
                        response.sendRedirect(request.getContextPath() + "/product/list?confirmOrder=" + pendingProductId);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/product/list");
                    }
                }
            } else {
                request.setAttribute("errorMessage", "Invalid Username or Password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Login failed due to an error: " + e.getMessage(), e);
        }
    }

}
