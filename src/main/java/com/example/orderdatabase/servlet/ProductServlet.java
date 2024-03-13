package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.ProductDAO;
import com.example.orderdatabase.dao.OrderDAO;
import com.example.orderdatabase.model.Product;
import com.example.orderdatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Defines this class as a servlet handling requests with URL patterns matching "/product/*".
@WebServlet(name = "ProductServlet", urlPatterns = {"/product/*"})
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO; // DAO for product data interaction.
    private OrderDAO orderDAO; // DAO for order management.
    private final static Logger LOGGER = Logger.getLogger(ProductServlet.class.getName()); // Logger for logging errors.

    @Override
    public void init() {
        // Initializes the DAO instances required for product and order operations.
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Determines the specific action requested by the URL path and delegates to the appropriate method.
        String action = request.getPathInfo();
        HttpSession session = request.getSession(false); // Attempts to get the existing session without creating a new one.
        User user = (session != null) ? (User) session.getAttribute("user") : null; // Retrieves the logged-in user, if present.

        try {
            switch (action) {
                case "/list":
                    // Lists all available products.
                    listProducts(request, response);
                    break;
                case "/details":
                    // Displays details of a specific product.
                    showProductDetails(request, response, user);
                    break;
                case "/order":
                    // Handles the product ordering process.
                    String productIdStr = request.getParameter("productId");
                    if (productIdStr != null && !productIdStr.isEmpty()) {
                        long productId = Long.parseLong(productIdStr);
                        if (user == null) {
                            // Redirects to the login page if the user is not logged in.
                            response.sendRedirect(request.getContextPath() + "/login");
                        } else {
                            // Processes the order for the logged-in user.
                            processOrder(request, response, user, productId);
                        }
                    } else {
                        // Product ID is not provided; handle accordingly.
                    }
                    break;
                default:
                    // Sends a 404 error if the requested action is not recognized.
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException ex) {
            // Logs and rethrows SQL exceptions as ServletExceptions.
            LOGGER.log(Level.SEVERE, "SQL Error", ex);
            throw new ServletException("SQL Error", ex);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        // Retrieves a list of all products and forwards the request to the product list view.
        List<Product> listProduct = productDAO.selectAllProducts();
        request.setAttribute("listProduct", listProduct);
        request.getRequestDispatcher("/WEB-INF/views/product-list.jsp").forward(request, response);
    }

    private void showProductDetails(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException, SQLException {
        // Retrieves and displays the details of a specific product.
        String idStr = request.getParameter("id");
        if (idStr == null) {
            // Sends a 400 error if the product ID is missing.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is missing");
            return;
        }
        long id = Long.parseLong(idStr);
        Product product = productDAO.selectProduct(id);
        if (product == null) {
            // Sends a 404 error if the product is not found.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }
        request.setAttribute("product", product);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/product-details.jsp").forward(request, response);
    }

    private void processOrder(HttpServletRequest request, HttpServletResponse response, User user, long productId) throws IOException, ServletException, SQLException {
        // Processes a product order, including calculating the total amount and updating the order status.
        Product product = productDAO.selectProduct(productId);
        if (product == null) {
            // Sends a 404 error if the product is not found.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
            return;
        }

        BigDecimal totalAmount = BigDecimal.valueOf(product.getPrice()); // Assumes the product price as the total order amount.
        String status = "Pending"; // Sets a default order status.

        boolean isOrderAdded = orderDAO.addOrder(user.getId(), totalAmount, status);
        if (isOrderAdded) {
            // Redirects to the product list with a success message if the order is successfully processed.
            response.sendRedirect(request.getContextPath() + "/product/list?orderSuccess=true");
        } else {
            // Redirects to the product details page with an error message if the order processing fails.
            response.sendRedirect(request.getContextPath() + "/product/details?id=" + productId + "&error=Order processing failed");
        }
    }
}
