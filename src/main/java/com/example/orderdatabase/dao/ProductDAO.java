package com.example.orderdatabase.dao;

import com.example.orderdatabase.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    // Database connection details
    private final String jdbcURL = "jdbc:mysql://localhost:3306/orderdatabase"; // Update with your database URL
    private final String jdbcUsername = "root"; // Update with your database username
    private final String jdbcPassword = "password"; // Update with your database password

    // SQL queries for CRUD operations
    private static final String INSERT_PRODUCTS_SQL = "INSERT INTO products (productName, productDescription, price, inStock) VALUES (?, ?, ?, ?);";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT productId, productName, productDescription, price, inStock, createdAt, updatedAt FROM products WHERE productId = ?;";
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String UPDATE_PRODUCT_SQL = "UPDATE products SET productName = ?, productDescription = ?, price = ?, inStock = ? WHERE productId = ?;";
    private static final String DELETE_PRODUCT_SQL = "DELETE FROM products WHERE productId = ?;";

    public ProductDAO() {
        // Constructor can be used for setting up things like connection pools
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean insertProduct(Product product) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCTS_SQL)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setBoolean(4, product.isInStock());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public Product selectProduct(long productId) {
        Product product = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setLong(1, productId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("productName");
                String description = rs.getString("productDescription");
                double price = rs.getDouble("price");
                boolean inStock = rs.getBoolean("inStock");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                product = new Product(productId, name, description, price, inStock, new Date(createdAt.getTime()), new Date(updatedAt.getTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> selectAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("productId");
                String name = rs.getString("productName");
                String description = rs.getString("productDescription");
                double price = rs.getDouble("price");
                boolean inStock = rs.getBoolean("inStock");
                // Assume there are columns for createdAt and updatedAt
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");

                products.add(new Product(id, name, description, price, inStock, createdAt, updatedAt));
            }
        }
        return products;
    }


    public boolean updateProduct(Product product) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getProductDescription());
            statement.setDouble(3, product.getPrice());
            statement.setBoolean(4, product.isInStock());
            statement.setLong(5, product.getProductId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public boolean deleteProduct(long productId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_SQL)) {
            statement.setLong(1, productId);

            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
}
