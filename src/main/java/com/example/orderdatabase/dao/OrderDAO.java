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

    // Initializes a new instance of the OrderDAO class.
    public OrderDAO() {
        // No initialization code needed in the constructor
    }

    // Adds a new order to the database without including any order items. It returns true if the order is successfully added.
    public boolean addOrder(long userId, BigDecimal totalAmount, String status) throws SQLException {
        // SQL statement to insert a new order into the database
        String insertOrderSql = "INSERT INTO orders (userId, orderDate, totalAmount, status, isFulfilled) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime())); // Sets the current time as the order date
            preparedStatement.setBigDecimal(3, totalAmount);
            preparedStatement.setString(4, status);
            preparedStatement.setBoolean(5, false); // Initially, the order is not fulfilled

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected."); // Ensures that the order was successfully added
            }
            return true;
        }
    }

    // Retrieves all orders from the database and returns them as a list of Order objects.
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT orderId, userId, orderDate, totalAmount, status, isFulfilled FROM orders"; // SQL query to fetch all orders
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Adds each fetched order to the list of orders
                orders.add(new Order(rs.getLong("orderId"), rs.getLong("userId"), rs.getTimestamp("orderDate"),
                        rs.getBigDecimal("totalAmount"), rs.getString("status"), rs.getBoolean("isFulfilled")));
            }
        }
        return orders;
    }

    // Fetches a specific order by its ID along with its associated order items and returns it as an Order object.
    public Order getOrderById(long orderId) throws SQLException {
        Order order = null;
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE orderId = ?"; // SQL query to fetch a specific order by its ID
        String itemsSql = "SELECT * FROM order_items WHERE orderId = ?"; // SQL query to fetch all items associated with the order

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Creates the Order object if the order is found
                order = new Order(rs.getLong("orderId"), rs.getLong("userId"), rs.getTimestamp("orderDate"),
                        rs.getBigDecimal("totalAmount"), rs.getString("status"), rs.getBoolean("isFulfilled"));
            }

            try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                itemsStmt.setLong(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();
                while (itemsRs.next()) {
                    // Adds each order item to the list of order items
                    OrderItem item = new OrderItem(itemsRs.getLong("orderItemId"), orderId,
                            itemsRs.getLong("productId"), itemsRs.getInt("quantity"),
                            itemsRs.getBigDecimal("price"));
                    orderItems.add(item);
                }
            }

            if (order != null) {
                // Sets the fetched order items to the order before returning it
                order.setOrderItems(orderItems);
            }
        }
        return order;
    }

    // Updates the status and fulfillment state of an existing order. Returns true if the update is successful.
    public boolean updateOrderStatus(long orderId, String status, boolean isFulfilled) throws SQLException {
        String sql = "UPDATE orders SET status = ?, isFulfilled = ? WHERE orderId = ?"; // SQL statement to update order status and fulfillment
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setBoolean(2, isFulfilled);
            stmt.setLong(3, orderId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Checks if the update was successful
        }
    }

    // Adds a new order along with its items to the database. It handles the transaction to ensure both order and items are added atomically. Returns the new order ID.
    public long addOrderWithItems(long userId, BigDecimal price, long productId, int quantity) throws SQLException {
        // The following variables are declared outside the try block to ensure they can be closed in the finally block
        Connection connection = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;
        ResultSet generatedKeys = null;
        long orderId = -1; // Default value indicating that the order ID has not been set

        try {
            connection = MySQLConnection.getConnection();
            connection.setAutoCommit(false); // Starts a transaction

            // SQL statement to insert a new order into the database
            String insertOrderSql = "INSERT INTO orders (userId, orderDate, totalAmount, status, isFulfilled) VALUES (?, NOW(), ?, 'Pending', FALSE);";
            orderStmt = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setLong(1, userId);
            orderStmt.setBigDecimal(2, price.multiply(new BigDecimal(quantity))); // Calculates the total amount for the order
            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected."); // Ensures the order was successfully added
            }

            generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getLong(1); // Retrieves the generated order ID
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            // SQL statement to insert an order item into the database
            String insertItemSql = "INSERT INTO order_items (orderId, productId, quantity, price) VALUES (?, ?, ?, ?);";
            itemStmt = connection.prepareStatement(insertItemSql);
            itemStmt.setLong(1, orderId);
            itemStmt.setLong(2, productId);
            itemStmt.setInt(3, quantity);
            itemStmt.setBigDecimal(4, price);
            itemStmt.executeUpdate();

            connection.commit(); // Commits the transaction if both insert operations were successful
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rolls back the transaction in case of any errors
                    System.out.println("Transaction rolled back due to an error");
                } catch (SQLException e2) {
                    System.out.println("Error during transaction rollback");
                    e2.printStackTrace();
                }
            }
            throw ex;
        } finally {
            // Ensures all resources are closed properly
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (itemStmt != null) try { itemStmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (orderStmt != null) try { orderStmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return orderId; // Returns the ID of the newly created order
    }

    // Deletes an order from the database by its ID. Returns true if the deletion is successful.
    public boolean deleteOrder(long orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE orderId = ?"; // SQL statement to delete an order by its ID
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Checks if the deletion was successful
        }
    }
}
