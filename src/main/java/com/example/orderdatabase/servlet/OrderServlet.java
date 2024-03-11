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
                case "/create":
                    // Direct to the page to create a new order
                    request.getRequestDispatcher("/WEB-INF/views/orderForm.jsp").forward(request, response);
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
                case "/insert":
                    insertOrder(request, response);
                    break;
                case "/update":
                    updateOrder(request, response);
                    break;
                case "/insertWithItem":
                    insertOrderWithItem(request, response);
                    break;
                case "/delete":
                    deleteOrder(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("SQL Error", ex);
        }
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Order> listOrder = orderDAO.getAllOrders();
        request.setAttribute("listOrder", listOrder);
        request.getRequestDispatcher("/WEB-INF/views/orderList.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        Order existingOrder = orderDAO.getOrderById(id);
        request.setAttribute("order", existingOrder);
        request.getRequestDispatcher("/WEB-INF/views/orderForm.jsp").forward(request, response);
    }

    private void insertOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        long userId = Long.parseLong(request.getParameter("userId"));
        BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));
        String status = request.getParameter("status");

        orderDAO.addOrder(userId, totalAmount, status);
        response.sendRedirect("list");
    }

    private void updateOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        long orderId = Long.parseLong(request.getParameter("orderId"));
        String status = request.getParameter("status");
        boolean isFulfilled = Boolean.parseBoolean(request.getParameter("isFulfilled"));

        orderDAO.updateOrderStatus(orderId, status, isFulfilled);
        response.sendRedirect("list");
    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        long orderId = Long.parseLong(request.getParameter("id"));

        orderDAO.deleteOrder(orderId);
        response.sendRedirect("list");
    }

    private void insertOrderWithItem(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        // Retrieve user and product details from request
        long userId = Long.parseLong(request.getParameter("userId"));
        long productId = Long.parseLong(request.getParameter("productId"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity")); // Assuming quantity is being sent

        // Insert the order and order item using a new method in OrderDAO
        long orderId = orderDAO.addOrderWithItems(userId, price, productId, quantity);

        // Prepare response
        if (orderId > 0) {
            response.getWriter().write("Order placed successfully with Order ID: " + orderId);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.getWriter().write("Failed to place order.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
