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

// WebServlet annotation defines this class as a servlet with a specific URL pattern.
@WebServlet("/productDetail")
public class ProductDetailServlet extends HttpServlet {
    private ProductDAO productDAO; // Instance variable for the ProductDAO.

    @Override
    public void init() {
        // Initializes the servlet. This method is called before the servlet handles its first request.
        // It's used here to instantiate the ProductDAO, ensuring it's ready for database operations.
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Sets the response content type to JSON and encoding to UTF-8, preparing for a JSON response.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Retrieves the productId from the request parameter.
        String productIdStr = request.getParameter("productId");
        try {
            // Attempts to parse the productId string to a long. If successful, uses the productDAO to fetch the product.
            long productId = Long.parseLong(productIdStr);
            Product product = productDAO.selectProduct(productId);

            if (product != null) {
                // If the product is found, converts the product object to a JSON string and writes it to the response.
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(product);
                response.getWriter().write(jsonResponse);
            } else {
                // If the product is not found, sets the response status to 404 (Not Found) and returns a JSON message indicating the product is not found.
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\":\"Product not found\"}");
            }
        } catch (NumberFormatException e) {
            // Catches NumberFormatException if productId is not a valid long, sets the response status to 400 (Bad Request),
            // and returns a JSON message indicating an invalid product ID format.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Invalid product ID format\"}");
        } catch (Exception e) {
            // General exception handling: sets the response status to 500 (Internal Server Error) and returns a JSON message indicating an error occurred.
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"An error occurred\"}");
        }
    }
}
