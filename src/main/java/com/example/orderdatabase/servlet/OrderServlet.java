package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.OrderDAO;
import com.example.orderdatabase.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

// Ensure OrderDAO is imported if it's in a different package
import com.example.orderdatabase.dao.OrderDAO;

public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        // Initialize the DAO
        this.orderDAO = new OrderDAO(); // Make sure OrderDAO is properly defined
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle form submission for placing an order
        placeOrder(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Determine the action based on the servlet path
        String action = request.getServletPath();

        try {
            // Routing logic based on the action
            switch (action) {
                case "/orderList":
                    listOrders(request, response);
                    break;
                case "/orderDetails":
                    showOrderDetails(request, response);
                    break;
                default:
                    // Redirect to login if the action is unrecognized
                    response.sendRedirect("login");
                    break;
            }
        } catch (Exception ex) {
            // Replace printStackTrace with proper logging
            log("ServletException in OrderServlet: ", ex);
            throw new ServletException(ex);
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Implementation details for placing an order

        // Example:
        // Order newOrder = new Order();
        // Populate the order details from the request
        // orderDAO.createOrder(newOrder);
        // Redirect to the order list page after placing the order
        response.sendRedirect("orderList");
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Assume the user ID is retrieved from the session or another mechanism
        // Cast to the appropriate type if necessary (int to long)
        Long userId = (Long) request.getSession().getAttribute("userId"); // Updated to Long to match the database
        request.setAttribute("orderList", orderDAO.getUserOrders(userId));
        RequestDispatcher dispatcher = request.getRequestDispatcher("OrderList.jsp");
        dispatcher.forward(request, response);
    }

    private void showOrderDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the order ID from the request and cast to long
        Long orderId = Long.parseLong(request.getParameter("id"));
        request.setAttribute("order", orderDAO.getOrderById(orderId));
        RequestDispatcher dispatcher = request.getRequestDispatcher("OrderDetails.jsp");
        dispatcher.forward(request, response);
    }

    // Overriding the destroy() method if you need to release any resources
    @Override
    public void destroy() {
        // Resources release logic, if needed
    }
}
