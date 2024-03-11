package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.ProductDAO;
import com.example.orderdatabase.model.Product;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/productDetail")
public class ProductDetailServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() {
        // Ensure your DAO is properly initialized here.
        // This might involve setting up a database connection within the DAO if it's not already handled.
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String productIdStr = request.getParameter("productId");
        try {
            long productId = Long.parseLong(productIdStr);
            Product product = productDAO.selectProduct(productId);

            if (product != null) {
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(product);
                response.getWriter().write(jsonResponse);
            } else {
                // Handle case where product is not found
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\":\"Product not found\"}");
            }
        } catch (NumberFormatException e) {
            // Handle case where productId is not a valid long
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Invalid product ID format\"}");
        } catch (Exception e) {
            // General error handling
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"An error occurred\"}");
        }
    }
}
