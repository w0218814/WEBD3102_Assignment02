package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.ProductDAO;
import com.example.orderdatabase.model.Product;
import jakarta.servlet.RequestDispatcher;
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

@WebServlet(name = "ProductServlet", urlPatterns = {"/product/*"})
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private final static Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
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
                // Other cases for insert, update, delete would go here
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error occurred", ex);
            throw new ServletException("Database access error!", ex);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "General error occurred", ex);
            throw new ServletException("An error occurred processing your request", ex);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int page = 1;
        int recordsPerPage = 25;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Product> listProduct = productDAO.selectProducts((page - 1) * recordsPerPage, recordsPerPage);
        int noOfRecords = productDAO.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("listProduct", listProduct);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ProductDetails.jsp");
        dispatcher.forward(request, response);
    }
}
