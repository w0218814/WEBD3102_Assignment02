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
        String insertOrderSql = "INSERT INTO orders (userId, orderDate, totalAmount, status, isFulfilled) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
            preparedStatement.setBigDecimal(3, totalAmount);
            preparedStatement.setString(4, status);
            preparedStatement.setBoolean(5, false);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            return true;
        }
    }

    public List<Order> getAllOrders() throws SQLException {
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

    public Order getOrderById(long orderId) throws SQLException {
        Order order = null;
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE orderId = ?";
        String itemsSql = "SELECT * FROM order_items WHERE orderId = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = new Order(rs.getLong("orderId"), rs.getLong("userId"), rs.getTimestamp("orderDate"),
                        rs.getBigDecimal("totalAmount"), rs.getString("status"), rs.getBoolean("isFulfilled"));
            }

            try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                itemsStmt.setLong(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();
                while (itemsRs.next()) {
                    OrderItem item = new OrderItem(itemsRs.getLong("orderItemId"), orderId,
                            itemsRs.getLong("productId"), itemsRs.getInt("quantity"),
                            itemsRs.getBigDecimal("price"));
                    orderItems.add(item);
                }
            }

            if (order != null) {
                order.setOrderItems(orderItems);
            }
        }
        return order;
    }

    public boolean updateOrderStatus(long orderId, String status, boolean isFulfilled) throws SQLException {
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

    public long addOrderWithItems(long userId, BigDecimal price, long productId, int quantity) throws SQLException {
        Connection connection = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;
        ResultSet generatedKeys = null;
        long orderId = -1;

        try {
            connection = MySQLConnection.getConnection();
            connection.setAutoCommit(false);

            String insertOrderSql = "INSERT INTO orders (userId, orderDate, totalAmount, status, isFulfilled) VALUES (?, NOW(), ?, 'Pending', FALSE);";
            orderStmt = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setLong(1, userId);
            orderStmt.setBigDecimal(2, price.multiply(new BigDecimal(quantity)));
            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            String insertItemSql = "INSERT INTO order_items (orderId, productId, quantity, price) VALUES (?, ?, ?, ?);";
            itemStmt = connection.prepareStatement(insertItemSql);
            itemStmt.setLong(1, orderId);
            itemStmt.setLong(2, productId);
            itemStmt.setInt(3, quantity);
            itemStmt.setBigDecimal(4, price);
            itemStmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Transaction rolled back due to an error");
                } catch (SQLException e2) {
                    System.out.println("Error during transaction rollback");
                    e2.printStackTrace();
                }
            }
            throw ex;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (itemStmt != null) try { itemStmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (orderStmt != null) try { orderStmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return orderId;
    }

    public boolean deleteOrder(long orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE orderId = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}