package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.ProductDAO;
import com.example.orderdatabase.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Handles all /product/* URL patterns
@WebServlet(name = "ProductServlet", urlPatterns = {"/product/*"})
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;
    private final static Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());

    // Initialization
    public void init() {
        this.productDAO = new ProductDAO();
    }

    // Handles GET requests
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            switch (action) {
                case "/new":
                    // Code for showing new product form (unchanged)
                    break;
                case "/edit":
                    // Code for showing edit product form (unchanged)
                    break;
                case "/delete":
                    // Code for handling delete (unchanged)
                    break;
                case "/list":
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error", ex);
            throw new ServletException("SQL Error", ex);
        }
    }

    // Handles POST requests
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Your existing code to handle POST requests for insert, update, delete actions
        // Unchanged
    }

    // Method to list products to be displayed in JSP
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Product> listProduct = productDAO.selectAllProducts();
        request.setAttribute("listProduct", listProduct);
        request.getRequestDispatcher("/WEB-INF/views/product-list.jsp").forward(request, response);
    }


    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        Product newProduct = new Product();
        newProduct.setProductName(name);
        newProduct.setProductDescription(description);
        newProduct.setPrice(price);

        productDAO.insertProduct(newProduct);
        response.sendRedirect("list");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));

        Product product = new Product(id, name, description, price);

        productDAO.updateProduct(product);
        response.sendRedirect("list");
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));

        productDAO.deleteProduct(id);
        response.sendRedirect("list");
    }

    // Other necessary methods...
}
