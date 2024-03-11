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

@WebServlet(name = "ProductServlet", urlPatterns = {"/product/*"})
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    private OrderDAO orderDAO; // Assume this exists for handling orders
    private final static Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());

    @Override
    public void init() {
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO(); // Initialize OrderDAO here
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        try {
            switch (action) {
                case "/list":
                    listProducts(request, response);
                    break;
                case "/details":
                    showProductDetails(request, response, user);
                    break;
                case "/order":
                    String productIdStr = request.getParameter("productId");
                    if (productIdStr != null && !productIdStr.isEmpty()) {
                        long productId = Long.parseLong(productIdStr);
                        if (user == null) {
                            // User is not logged in, redirect to login
                            response.sendRedirect(request.getContextPath() + "/user/login");
                        } else {
                            // User is logged in, proceed to process the order
                            processOrder(request, response, user, productId);
                        }
                    } else {
                        // Product ID not provided, handle as needed
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

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Product> listProduct = productDAO.selectAllProducts();
        request.setAttribute("listProduct", listProduct);
        request.getRequestDispatcher("/WEB-INF/views/product-list.jsp").forward(request, response);
    }

    private void showProductDetails(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException, SQLException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is missing");
            return;
        }
        long id = Long.parseLong(idStr);
        Product product = productDAO.selectProduct(id);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }
        request.setAttribute("product", product);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/product-details.jsp").forward(request, response);
    }

    private void processOrder(HttpServletRequest request, HttpServletResponse response, User user, long productId) throws IOException, ServletException, SQLException {
        // Fetch the product price
        Product product = productDAO.selectProduct(productId);
        if (product == null) {
            // Handle error: Product not found
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
            return;
        }

        BigDecimal totalAmount = BigDecimal.valueOf(product.getPrice()); // Assuming you want to use the product's price as the total amount
        String status = "Pending"; // Example status, you might have a different approach

        boolean isOrderAdded = orderDAO.addOrder(user.getId(), totalAmount, status);
        if (isOrderAdded) {
            // Handle success: Order was successfully added
            response.sendRedirect(request.getContextPath() + "/product/list?orderSuccess=true");
        } else {
            // Handle error: Order was not added
            response.sendRedirect(request.getContextPath() + "/product/details?id=" + productId + "&error=Order processing failed");
        }
    }

    // Other utility methods if needed
}
