package com.example.orderdatabase.dao;

import com.example.orderdatabase.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // A field to cache the total number of records. Initialized to -1 to indicate it's not loaded.
    private int noOfRecords = -1;

    public ProductDAO() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcPassword = "password";
            String jdbcUsername = "root";
            String jdbcURL = "jdbc:mysql://127.0.0.1:3306/orderdatabase";
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Other CRUD operations methods like insertProduct, selectProduct, updateProduct, deleteProduct

    public List<Product> selectProducts(int offset, int recordsPerPage) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products LIMIT ?, ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, recordsPerPage);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("productId");
                    String name = rs.getString("productName");
                    String description = rs.getString("productDescription");
                    double price = rs.getDouble("price");
                    boolean inStock = rs.getBoolean("inStock");
                    Timestamp createdAt = rs.getTimestamp("createdAt");
                    Timestamp updatedAt = rs.getTimestamp("updatedAt");

                    products.add(new Product(id, name, description, price, inStock, createdAt, updatedAt));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void loadTotalRecordsCount() {
        String query = "SELECT COUNT(*) FROM orderdatabase.products";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                this.noOfRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNoOfRecords() {
        if (this.noOfRecords == -1) {
            loadTotalRecordsCount();
        }
        return noOfRecords;
    }
}
