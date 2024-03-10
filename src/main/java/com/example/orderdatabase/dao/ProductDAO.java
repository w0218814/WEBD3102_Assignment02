package com.example.orderdatabase.dao;

import com.example.orderdatabase.database.MySQLConnection;
import com.example.orderdatabase.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String INSERT_PRODUCT_SQL = "INSERT INTO products (productName, productDescription, price) VALUES (?, ?, ?);";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT productId, productName, productDescription, price FROM products WHERE productId = ?;";
    private static final String SELECT_ALL_PRODUCTS = "SELECT productId, productName, productDescription, price FROM products;";
    private static final String DELETE_PRODUCT_SQL = "DELETE FROM products WHERE productId = ?;";
    private static final String UPDATE_PRODUCT_SQL = "UPDATE products SET productName = ?, productDescription = ?, price = ? WHERE productId = ?;";

    public ProductDAO() {
    }

    public void insertProduct(Product product) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductDescription());
            preparedStatement.setDouble(3, product.getPrice());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
    }

    public Product selectProduct(long productId) throws SQLException {
        Product product = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setLong(1, productId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String productName = rs.getString("productName");
                    String productDescription = rs.getString("productDescription");
                    double price = rs.getDouble("price");
                    product = new Product(productId, productName, productDescription, price);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
        return product;
    }

    public List<Product> selectAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    long productId = rs.getLong("productId");
                    String productName = rs.getString("productName");
                    String productDescription = rs.getString("productDescription");
                    double price = rs.getDouble("price");
                    products.add(new Product(productId, productName, productDescription, price));
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
        return products;
    }

    public void deleteProduct(long productId) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_SQL)) {
            statement.setLong(1, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
    }

    public void updateProduct(Product product) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getProductDescription());
            statement.setDouble(3, product.getPrice());
            statement.setLong(4, product.getProductId());

            statement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
    }

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
