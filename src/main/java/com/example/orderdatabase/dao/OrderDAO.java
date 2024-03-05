package com.example.orderdatabase.dao;

import com.example.orderdatabase.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // Database connection details
    private String jdbcURL = "jdbc:mysql://localhost:3306/yourDatabaseName"; // Update with your database URL
    private String jdbcUsername = "yourUsername"; // Update with your database username
    private String jdbcPassword = "yourPassword"; // Update with your database password

    // SQL queries for CRUD operations
    private static final String INSERT_ORDER_SQL = "INSERT INTO orders (user_id, total_price, status, order_date) VALUES (?, ?, ?, ?);";
    private static final String SELECT_ORDER_BY_ID = "SELECT id, user_id, total_price, status, order_date FROM orders WHERE id = ?";
    private static final String SELECT_ALL_ORDERS_BY_USER_ID = "SELECT * FROM orders WHERE user_id = ?";
    // Add more queries as needed, such as UPDATE_ORDER_SQL for updating an order's details or status

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

    public void createOrder(Order order) {
        // Example implementation for creating a new order in the database
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_SQL)) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setDouble(2, order.getTotalPrice());
            preparedStatement.setString(3, order.getStatus());
            preparedStatement.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public List<Order> getUserOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        // Example implementation for retrieving orders by user ID
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS_BY_USER_ID)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                double totalPrice = rs.getDouble("total_price");
                String status = rs.getString("status");
                Date orderDate = rs.getDate("order_date");
                orders.add(new Order(id, userId, totalPrice, status, orderDate));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return orders;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        // Example implementation for retrieving an order by its ID
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_BY_ID)) {
            preparedStatement.setInt(1, orderId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String status = rs.getString("status");
                Date orderDate = rs.getDate("order_date");
                order = new Order(orderId, userId, totalPrice, status, orderDate);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return order;
    }

    // Implement additional methods as needed for updating and deleting orders

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    t = t.getCause();
                }
            }
        }
    }
}
