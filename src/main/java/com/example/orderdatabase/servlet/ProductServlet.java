package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.ProductDAO;
import com.example.orderdatabase.model.Product;
import com.example.orderdatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ProductServlet", urlPatterns = {"/product/*"})
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    private final static Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());

    public void init() {
        this.productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession(false); // Don't create a session if it doesn't exist

        // Check if the user is logged in by looking for a user object in the session
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        try {
            switch (action) {
                case "/list":
                    listProducts(request, response);
                    break;
                case "/details":
                    // Redirect to login if user is not logged in
                    if (user == null) {
                        String productId = request.getParameter("id"); // Get the product ID from the request
                        session = request.getSession(true); // Create a new session if one doesn't exist
                        session.setAttribute("pendingProductId", Long.parseLong(productId)); // Save the product ID to redirect after login
                        response.sendRedirect(request.getContextPath() + "/login"); // Redirect to login
                    } else {
                        showProductDetails(request, response);
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error", ex);
            throw new ServletException("SQL Error", ex);
        }
    }


    private void showProductDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Assume there's a "id" parameter in the request to identify the product
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is missing");
            return;
        }

        try {
            long id = Long.parseLong(idStr);
            Product product = productDAO.selectProduct(id);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/views/product-details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
        }
    }


    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Product> listProduct = productDAO.selectAllProducts();
        request.setAttribute("listProduct", listProduct);
        request.getRequestDispatcher("/WEB-INF/views/product-list.jsp").forward(request, response);
    }

    // Insert, update, delete methods have been removed as per your request
    // Add other methods if necessary
}
