package com.example.tododatabase.dao;

import com.example.tododatabase.database.MySQLConnection;
import com.example.tododatabase.model.Todo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {
    // SQL queries with placeholders for prepared statements
    private static final String INSERT_TODOS_SQL = "INSERT INTO todos (title, userId, description, targetDate, isDone) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_TODO_BY_ID = "SELECT id, title, description, targetDate, isDone FROM todos WHERE id = ?;";
    // Updated to include userId in WHERE clause for user-specific selection
    private static final String SELECT_ALL_TODOS_BY_USER = "SELECT * FROM todos WHERE userId = ?;";
    private static final String DELETE_TODOS_SQL = "DELETE FROM todos WHERE id = ?;";
    // Updated SQL to ensure updates are user-specific
    private static final String UPDATE_TODOS_SQL = "UPDATE todos SET title = ?, description = ?, targetDate = ?, isDone = ? WHERE id = ? AND userId = ?;";

    public TodoDAO() {}

    // Insert a todo item into the database
    public void insertTodo(Todo todo) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TODOS_SQL)) {
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setLong(2, todo.getUserId()); // Now using getUserId() from Todo
            preparedStatement.setString(3, todo.getDescription());
            preparedStatement.setDate(4, new java.sql.Date(todo.getTargetDate().getTime()));
            preparedStatement.setBoolean(5, todo.isDone());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    // Update a todo item based on its ID and userId
    public boolean updateTodo(Todo todo) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TODOS_SQL)) {
            statement.setString(1, todo.getTitle());
            statement.setString(2, todo.getDescription());
            statement.setDate(3, new Date(todo.getTargetDate().getTime()));
            statement.setBoolean(4, todo.isDone());
            statement.setLong(5, todo.getId());
            statement.setLong(6, todo.getUserId()); // Ensure update is user-specific
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // Select a single todo item by its ID
    public Todo selectTodo(long id, long userId) throws SQLException { // Added userId for user-specific selection
        Todo todo = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TODO_BY_ID)) {
            preparedStatement.setLong(1, id);
            // preparedStatement.setLong(2, userId); // If SELECT_TODO_BY_ID query is updated to consider userId
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) { // Use if instead of while since id is unique
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date targetDate = rs.getDate("targetDate");
                boolean isDone = rs.getBoolean("isDone");
                todo = new Todo(id, userId, title, description, targetDate, isDone); // Includes userId in the Todo object
            }
        }
        return todo;
    }

    // Select all todo items for a specific user
    public List<Todo> selectAllTodos(long userId) throws SQLException {
        List<Todo> todos = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TODOS_BY_USER)) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date targetDate = rs.getDate("targetDate");
                boolean isDone = rs.getBoolean("isDone");
                todos.add(new Todo(id, userId, title, description, targetDate, isDone)); // Construct Todo objects with userId
            }
        }
        return todos;
    }

    // Delete a todo item based on its ID
    public boolean deleteTodo(long id, long userId) throws SQLException { // Consider adding userId for user-specific deletion
        boolean rowDeleted;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TODOS_SQL)) {
            statement.setLong(1, id);
            // Additional logic may be added here to ensure the todo belongs to the user
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Utility method to print SQL exceptions
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
