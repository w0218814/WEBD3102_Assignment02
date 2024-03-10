package com.example.orderdatabase.dao;

import com.example.orderdatabase.database.MySQLConnection;
import com.example.orderdatabase.model.Order;
import com.example.orderdatabase.model.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO {

    public OrderDAO() {
        // Constructor remains unchanged
    }

    // This method inserts a new order without considering order items
    public boolean addOrder(long userId, BigDecimal totalAmount, String status) throws SQLException {
        // Unchanged code for adding an order
        String insertOrderSql = "INSERT INTO orders (userId, orderDate, totalAmount, status, isFulfilled) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
            preparedStatement.setBigDecimal(3, totalAmount);
            preparedStatement.setString(4, status);
            preparedStatement.setBoolean(5, false); // Assuming new orders are not fulfilled initially

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            return true;
        }
    }

    // This method retrieves all orders from the database
    public List<Order> getAllOrders() throws SQLException {
        // Unchanged code for retrieving all orders
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT orderId, userId, orderDate, totalAmount, status, isFulfilled FROM orders";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(rs.getLong("orderId"), rs.getLong("userId"), rs.getTimestamp("orderDate"),
                        rs.getBigDecimal("totalAmount"), rs.getString("status"), rs.getBoolean("isFulfilled")));
            }
        }
        return orders;
    }

    // New method to retrieve an order by its ID, including its order items
    public Order getOrderById(long orderId) throws SQLException {
        Order order = null;
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE orderId = ?";
        String itemsSql = "SELECT * FROM order_items WHERE orderId = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {

            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = new Order(rs.getLong("orderId"), rs.getLong("userId"), rs.getTimestamp("orderDate"),
                        rs.getBigDecimal("totalAmount"), rs.getString("status"), rs.getBoolean("isFulfilled"));
            }

            itemsStmt.setLong(1, orderId);
            ResultSet itemsRs = itemsStmt.executeQuery();
            while (itemsRs.next()) {
                OrderItem item = new OrderItem(itemsRs.getLong("orderItemId"), orderId,
                        itemsRs.getLong("productId"), itemsRs.getInt("quantity"),
                        itemsRs.getBigDecimal("price"));
                orderItems.add(item);
            }

            if (order != null) {
                order.setOrderItems(orderItems);
            }
        }
        return order;
    }

    // Method to update an order's status and fulfillment
    public boolean updateOrderStatus(long orderId, String status, boolean isFulfilled) throws SQLException {
        // Unchanged code for updating order status
        String sql = "UPDATE orders SET status = ?, isFulfilled = ? WHERE orderId = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setBoolean(2, isFulfilled);
            stmt.setLong(3, orderId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Method to delete an order by its ID
    public boolean deleteOrder(long orderId) throws SQLException {
        // Unchanged code for deleting an order
        String sql = "DELETE FROM orders WHERE orderId = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Additional utility methods for order management could be added here
    // For example, methods for handling order items, such as addOrderItem, updateOrderItem, deleteOrderItem, etc.
}
