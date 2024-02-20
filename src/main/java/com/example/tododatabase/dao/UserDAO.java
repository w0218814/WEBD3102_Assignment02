package com.example.tododatabase.dao;

import com.example.tododatabase.database.MySQLConnection;
import com.example.tododatabase.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String INSERT_USERS_SQL = "INSERT INTO users (username, password, fullName, email, roleId) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT u.id, u.username, u.fullName, u.email, r.roleName FROM users u JOIN roles r ON u.roleId = r.roleId WHERE u.id = ?;";
    private static final String SELECT_ALL_USERS = "SELECT u.id, u.username, u.fullName, u.email FROM users u";
    private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String UPDATE_USERS_SQL = "UPDATE users SET username = ?, fullName = ?, email = ? WHERE id = ?;";
    private static final String SELECT_USER_ROLE_SQL = "SELECT roleName FROM roles WHERE roleId = (SELECT roleId FROM users WHERE id = ?);";
    private static final String CHECK_LOGIN_SQL = "SELECT u.id, u.username, u.fullName, u.email, r.roleName, u.password FROM users u INNER JOIN roles r ON u.roleId = r.roleId WHERE username = ?";
    public UserDAO() {}

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

    public User selectUserById(long id) throws SQLException {
        User user = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("roleName"));
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw e;
        }
        return user;
    }

    public void insertUser(User user, String hashedPassword, int roleId) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, roleId); // Dynamic role ID based on the parameter
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw e; // Rethrow the exception to be handled further up the call stack
        }
    }


    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getEmail());
            statement.setLong(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
            rowUpdated = false;
        }
        return rowUpdated;
    }

    public boolean deleteUser(long id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL)) {
            statement.setLong(1, id);

            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
    public User checkLogin(String username, String password) throws SQLException {
        User user = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LOGIN_SQL)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next() && BCrypt.checkpw(password, rs.getString("password"))) {
                user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("roleName"));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public String getUserRole(long userId) throws SQLException {
        String role = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_ROLE_SQL)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("roleName");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return role;
    }
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        null); // Role is not fetched here
                users.add(user);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public boolean updatePassword(long id, String newPassword) throws SQLException {
        boolean rowUpdated;
        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ? WHERE id = ?;")) {
            statement.setString(1, hashedNewPassword);
            statement.setLong(2, id);

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}