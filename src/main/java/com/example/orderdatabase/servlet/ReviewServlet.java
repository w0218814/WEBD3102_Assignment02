package com.example.orderdatabase.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/reviews":
                request.getRequestDispatcher("/WEB-INF/views/reviews.jsp").forward(request, response);
                break;
            // Add more cases if needed
            default:
                response.sendRedirect(request.getContextPath() + "/"); // Redirect to home page if no matching case
                break;
        }
    }
}
