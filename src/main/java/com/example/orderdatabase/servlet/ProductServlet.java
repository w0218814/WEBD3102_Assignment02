package com.example.orderdatabase.servlet;

import jakarta.servlet.RequestDispatcher;
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

@WebServlet(name = "ProductServlet", urlPatterns = {"/product/*"})
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Route POST requests to doGet for simplicity.
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";
        try {
            switch (pathInfo) {
                case "/list":
                    listProducts(request, response);
                    break;
                case "/insert":
                    // Display form for inserting a new product or handle the insertion if this is a form submission
                    // Example: showInsertForm(request, response) or insertProduct(request, response)
                    break;
                case "/update":
                    // Display form for updating an existing product or handle the update if this is a form submission
                    // Example: showUpdateForm(request, response) or updateProduct(request, response)
                    break;
                case "/delete":
                    // Handle product deletion
                    // Example: deleteProduct(request, response)
                    break;
                default:
                    // For any other GET request, redirect to the product list
                    listProducts(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Product> listProduct = productDAO.selectAllProducts();
        request.setAttribute("listProduct", listProduct);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ProductDetails.jsp");
        dispatcher.forward(request, response);
    }

    // Placeholder methods for insertProduct, updateProduct, deleteProduct, showInsertForm, showUpdateForm
}
