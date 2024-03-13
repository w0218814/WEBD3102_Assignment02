package com.example.orderdatabase.servlet;

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

@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() {
        this.orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";

        try {
            switch (action) {
                case "/list":
                    listOrders(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("SQL Error", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/update":
                    updateOrder(request, response);
                    break;
                case "/create": // Add this case for order creation
                    createOrder(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("SQL Error", ex);
        }
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Order> listOrder = orderDAO.getAllOrders();
        request.setAttribute("listOrder", listOrder);
        request.getRequestDispatcher("/WEB-INF/views/OrderList.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        Order existingOrder = orderDAO.getOrderById(id);
        request.setAttribute("order", existingOrder);
        request.getRequestDispatcher("/WEB-INF/views/OrderForm.jsp").forward(request, response);
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Extracting request parameters
        long userId = Long.parseLong(request.getParameter("userId"));
        long productId = Long.parseLong(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        BigDecimal totalAmount = price.multiply(new BigDecimal(quantity));

        try {
            // Add the order with items
            long orderId = orderDAO.addOrderWithItems(userId, price, productId, quantity);

            // Construct JSON response based on the result
            if (orderId > 0) {
                response.getWriter().write("{\"message\":\"Order successfully created\", \"orderId\":" + orderId + "}");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                // orderId will be -1 if the creation failed
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



    private void updateOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long orderId = -1;
        String status = request.getParameter("status");
        boolean isFulfilled = Boolean.parseBoolean(request.getParameter("isFulfilled"));

        try {
            orderId = Long.parseLong(request.getParameter("orderId"));
            boolean success = orderDAO.updateOrderStatus(orderId, status, isFulfilled);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/order/list");
            } else {
                // This branch might be unnecessary since you're not checking for failure but for exception handling
                throw new SQLException("Failed to update the order with ID: " + orderId);
            }
        } catch (NumberFormatException | SQLException e) {
            // Log the exception or handle it as per your application's requirement
            request.setAttribute("errorMessage", "Error updating order: " + e.getMessage());

            // When orderId is invalid or update fails, redirect to an error page or back to the form
            // Consider providing feedback or logging the error as needed
            // Consider providing feedback or logging the error as needed
            if(orderId != -1) {
                request.setAttribute("orderId", orderId);
            }

            request.getRequestDispatcher("/WEB-INF/views/OrderForm.jsp").forward(request, response);
        }
    }

}
