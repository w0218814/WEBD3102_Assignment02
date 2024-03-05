package com.example.orderdatabase.dao;

import com.example.orderdatabase.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // Database connection details
    private String jdbcURL = "jdbc:mysql://localhost:3306/orderdatabase"; // Update with your database URL
    private String jdbcUsername = "root"; // Update with your database username
    private String jdbcPassword = "inet2005"; // Update with your database password

    // SQL queries for CRUD operations
    // MODIFIED: Changed user_id to userId, total_price to totalAmount, isFulfilled is included in the SELECT queries
    private static final String INSERT_ORDER_SQL = "INSERT INTO orders (userId, orderDate, totalAmount, status) VALUES (?, ?, ?, ?);";
    private static final String SELECT_ORDER_BY_ID = "SELECT orderId, userId, orderDate, totalAmount, status, isFulfilled FROM orders WHERE orderId = ?";
    private static final String SELECT_ALL_ORDERS_BY_USER_ID = "SELECT * FROM orders WHERE userId = ?";

    // UNCHANGED: getConnection method remains the same
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            printSQLException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Handle the error appropriately
        }
        return connection;
    }

    // MODIFIED: Updated the method to match the Order class constructor and include totalAmount and status
    public void createOrder(Order order) {
        // Example implementation for creating a new order in the database
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.setTimestamp(2, new Timestamp(order.getOrderDate().getTime()));
            preparedStatement.setDouble(3, order.getTotalAmount());
            preparedStatement.setString(4, order.getStatus());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated key for the orderId
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setOrderId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    // MODIFIED: Updated the method to include new fields totalAmount and status
    public List<Order> getUserOrders(long userId) {
        List<Order> orders = new ArrayList<>();
        // Example implementation for retrieving orders by user ID
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long orderId = rs.getLong("orderId");
                double totalAmount = rs.getDouble("totalAmount");
                String status = rs.getString("status");
                Date orderDate = new Date(rs.getTimestamp("orderDate").getTime());
                boolean isFulfilled = rs.getBoolean("isFulfilled");
                Order order = new Order(orderId, userId, orderDate, isFulfilled, totalAmount, status);
                orders.add(order);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return orders;
    }

    // MODIFIED: Updated the method to include new fields totalAmount and status
    public Order getOrderById(long orderId) {
        Order order = null;
        // Example implementation for retrieving an order by its ID
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_BY_ID)) {
            preparedStatement.setLong(1, orderId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                long userId = rs.getLong("userId");
                double totalAmount = rs.getDouble("totalAmount");
                String status = rs.getString("status");
                Date orderDate = new Date(rs.getTimestamp("orderDate").getTime());
                boolean isFulfilled = rs.getBoolean("isFulfilled");

                order = new Order(orderId, userId, orderDate, isFulfilled, totalAmount, status);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return order;
    }

    // UNCHANGED: The printSQLException method remains the same
    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
