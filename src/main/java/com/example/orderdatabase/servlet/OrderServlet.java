package com.example.orderdatabase.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() {
        // Initialize the DAO
        orderDAO = new OrderDAO(); // Assuming OrderDAO is properly defined elsewhere
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
            // Exception handling: Log and throw a ServletException
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Example implementation for placing an order
        // Extract order details from the request
        // Note: Implementation details such as retrieving form data, creating an Order object,
        // and calling orderDAO to save the order would go here

        // Redirect to the order list page after placing the order
        response.sendRedirect("orderList");
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Example implementation for listing orders for a user
        // Assume the user ID is retrieved from the session or another mechanism
        int userId = 1; // Placeholder value
        // Retrieve the list of orders for the user from the DAO
        request.setAttribute("orderList", orderDAO.getUserOrders(userId));
        // Forward to the JSP page for rendering the list
        RequestDispatcher dispatcher = request.getRequestDispatcher("OrderList.jsp");
        dispatcher.forward(request, response);
    }

    private void showOrderDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Example implementation for showing order details
        // Retrieve the order ID from the request
        int orderId = Integer.parseInt(request.getParameter("id"));
        // Retrieve the order details from the DAO
        request.setAttribute("order", orderDAO.getOrderById(orderId));
        // Forward to the JSP page for rendering the order details
        RequestDispatcher dispatcher = request.getRequestDispatcher("OrderDetails.jsp");
        dispatcher.forward(request, response);
    }
}
