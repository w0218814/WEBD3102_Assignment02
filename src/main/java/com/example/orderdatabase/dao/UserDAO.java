package com.example.orderdatabase.dao;

import com.example.orderdatabase.database.MySQLConnection;
import com.example.orderdatabase.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String INSERT_USERS_SQL = "INSERT INTO users (username, password, fullName, email, roleId, street, city, nearbyLandmark, province, postalCode, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT id, username, fullName, email, roleId, street, city, nearbyLandmark, province, postalCode, phoneNumber FROM users WHERE id = ?;";
    private static final String SELECT_ALL_USERS = "SELECT id, username, fullName, email, roleId, street, city, nearbyLandmark, province, postalCode, phoneNumber FROM users";
    private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String UPDATE_USERS_SQL = "UPDATE users SET username = ?, fullName = ?, email = ?, street = ?, city = ?, nearbyLandmark = ?, province = ?, postalCode = ?, phoneNumber = ? WHERE id = ?;";
    private static final String CHECK_LOGIN_SQL = "SELECT * FROM users WHERE username = ?;";

    public UserDAO() {}

    // Insert a new user
// In UserDAO.java
    public void insertUser(User user, String hashedPassword) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getEmail());
            // Set the default role ID to 2 directly here, assuming your schema allows for it
            preparedStatement.setInt(5, 2);
            preparedStatement.setString(6, user.getStreet());
            preparedStatement.setString(7, user.getCity());
            preparedStatement.setString(8, user.getNearbyLandmark());
            preparedStatement.setString(9, user.getProvince());
            preparedStatement.setString(10, user.getPostalCode());
            preparedStatement.setString(11, user.getPhoneNumber());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
    }


    // In your UserDAO class, add the following method:

    public boolean updatePassword(long userId, String newPassword) throws SQLException {
        boolean passwordUpdated;
        // Hash the new password
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        try (Connection connection = MySQLConnection.getConnection();
             // Prepare the SQL statement to update the user's password
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET password = ? WHERE id = ?;"
             )) {
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setLong(2, userId);

            // Execute the update statement and store the result
            passwordUpdated = preparedStatement.executeUpdate() > 0;
        }
        return passwordUpdated;
    }


    // Select a user by ID
    // Select a user by ID
    public User selectUserById(long id) throws SQLException {
        User user = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                long userId = rs.getLong("id");
                String username = rs.getString("username");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String nearbyLandmark = rs.getString("nearbyLandmark");
                String province = rs.getString("province");
                String postalCode = rs.getString("postalCode");
                String phoneNumber = rs.getString("phoneNumber");

                // roleId is not used as it's set by default in the User class
                user = new User(userId, username, fullName, email, street, city, nearbyLandmark, province, postalCode, phoneNumber);
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
        return user;
    }


    // Select all users
    // Select all users
    public List<User> selectAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long userId = rs.getLong("id");
                String username = rs.getString("username");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String nearbyLandmark = rs.getString("nearbyLandmark");
                String province = rs.getString("province");
                String postalCode = rs.getString("postalCode");
                String phoneNumber = rs.getString("phoneNumber");
                // Create a new User object using the constructor without roleId as it defaults to 2
                User user = new User(userId, username, fullName, email, street, city, nearbyLandmark, province, postalCode, phoneNumber);
                users.add(user);
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
        return users;
    }

        // Delete a user
    public boolean deleteUser(long id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL)) {
            statement.setLong(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Update a user
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getStreet());
            statement.setString(5, user.getCity());
            statement.setString(6, user.getNearbyLandmark());
            statement.setString(7, user.getProvince());
            statement.setString(8, user.getPostalCode());
            statement.setString(9, user.getPhoneNumber());
            statement.setLong(10, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // Check user login
    public User checkLogin(String username, String password) throws SQLException {
        User user = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LOGIN_SQL)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
                long userId = rs.getLong("id");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String nearbyLandmark = rs.getString("nearbyLandmark");
                String province = rs.getString("province");
                String postalCode = rs.getString("postalCode");
                String phoneNumber = rs.getString("phoneNumber");

                // roleId is not needed as a parameter here since it defaults to 2 in the User constructor
                user = new User(userId, username, fullName, email, street, city, nearbyLandmark, province, postalCode, phoneNumber);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }


    // Print SQLException for troubleshooting
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

    // Add other UserDAO methods if needed...
}
