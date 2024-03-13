package com.example.orderdatabase.servlet;

// Required imports for servlet operations, model handling, and exception management
import com.example.orderdatabase.dao.OrderDAO;
import com.example.orderdatabase.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

// Servlet annotation for defining URL patterns related to order actions
@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO; // Instance of OrderDAO for database operations

    // Servlet initialization: Creates an instance of OrderDAO for interacting with the database
    @Override
    public void init() {
        this.orderDAO = new OrderDAO();
    }

    // Handles GET requests by routing to appropriate methods based on the URL path
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list"; // Default to listing orders if no specific action is provided

        try {
            switch (action) {
                case "/list":
                    listOrders(request, response); // Method to list all orders
                    break;
                case "/edit":
                    showEditForm(request, response); // Method to display the edit form for an order
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND); // Handle unknown actions
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("SQL Error", ex); // Exception handling for SQL errors
        }
    }

    // Handles POST requests for updating or creating orders based on the URL path
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/update":
                    updateOrder(request, response); // Method to update an existing order's details
                    break;
                case "/create":
                    createOrder(request, response); // Method to create a new order
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND); // Handle unknown actions
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("SQL Error", ex); // General exception handling for database errors
        }
    }

    // Lists all orders by fetching from the database and forwarding to the JSP page for display
    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Order> listOrder = orderDAO.getAllOrders(); // Fetch all orders
        request.setAttribute("listOrder", listOrder); // Set orders in request scope
        request.getRequestDispatcher("/WEB-INF/views/OrderList.jsp").forward(request, response); // Forward to JSP page
    }

    // Displays the edit form for an order, pre-populated with existing order details
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id")); // Extract order ID from request
        Order existingOrder = orderDAO.getOrderById(id); // Fetch the order by ID
        request.setAttribute("order", existingOrder); // Set order in request scope
        request.getRequestDispatcher("/WEB-INF/views/OrderForm.jsp").forward(request, response); // Forward to JSP page
    }

    // Creates a new order with details extracted from the request parameters
    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Content type and character encoding for response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Extracting and parsing order details from request parameters
        long userId = Long.parseLong(request.getParameter("userId"));
        long productId = Long.parseLong(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        BigDecimal totalAmount = price.multiply(new BigDecimal(quantity)); // Calculate total amount

        try {
            // Adding the order to the database and getting the order ID
            long orderId = orderDAO.addOrderWithItems(userId, price, productId, quantity);

            // Constructing JSON response based on operation success
            if (orderId > 0) {
                response.getWriter().write("{\"message\":\"Order successfully created\", \"orderId\":" + orderId + "}");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.getWriter().write("{\"message\":\"Failed to create order\"}");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"message\":\"Invalid input format\"}");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            response.getWriter().write("{\"message\":\"Database error: " + e.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Updates an existing order's status and fulfillment based on the request parameters
    private void updateOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long orderId = -1; // Initial order ID declaration
        String status = request.getParameter("status"); // Extract status from request
        boolean isFulfilled = Boolean.parseBoolean(request.getParameter("isFulfilled")); // Extract fulfillment flag

        try {
            orderId = Long.parseLong(request.getParameter("orderId")); // Parse order ID from request
            boolean success = orderDAO.updateOrderStatus(orderId, status, isFulfilled); // Update order in database

            if (success) {
                response.sendRedirect(request.getContextPath() + "/order/list"); // Redirect to order list if update successful
            } else {
                // Handling for unsuccessful update, though this block may never be reached due to exception handling
                throw new SQLException("Failed to update the order with ID: " + orderId);
            }
        } catch (NumberFormatException | SQLException e) {
            // Error handling for invalid inputs or SQL errors
            request.setAttribute("errorMessage", "Error updating order: " + e.getMessage());

            if(orderId != -1) {
                request.setAttribute("orderId", orderId); // Set order ID in request scope if available
            }

            request.getRequestDispatcher("/WEB-INF/views/OrderForm.jsp").forward(request, response); // Forward to the form page for error display
        }
    }
}
