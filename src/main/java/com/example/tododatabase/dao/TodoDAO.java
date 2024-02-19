package com.example.tododatabase.dao;

import com.example.tododatabase.database.MySQLConnection;
import com.example.tododatabase.model.Todo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {
    private static final String INSERT_TODO_SQL = "INSERT INTO todos (userId, title, description, targetDate, isDone) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_TODO_BY_ID = "SELECT id, title, description, targetDate, isDone FROM todos WHERE id = ? AND userId = ?;";
    private static final String SELECT_ALL_TODOS_BY_USER = "SELECT id, title, description, targetDate, isDone FROM todos WHERE userId = ?;";
    private static final String DELETE_TODOS_SQL = "DELETE FROM todos WHERE id = ? AND userId = ?;";
    private static final String UPDATE_TODOS_SQL = "UPDATE todos SET title = ?, description = ?, targetDate = ?, isDone = ? WHERE id = ? AND userId = ?;";

    public TodoDAO() {
    }

    public void insertTodo(Todo todo) {
        // Using try-with-resources to auto close resources
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TODO_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, todo.getUserId());
            preparedStatement.setString(2, todo.getTitle());
            preparedStatement.setString(3, todo.getDescription());
            preparedStatement.setDate(4, new java.sql.Date(todo.getTargetDate().getTime()));
            preparedStatement.setBoolean(5, todo.isDone); // Accessing the public field directly

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                // If you want to handle the generated keys, do it here
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        todo.setId(generatedKeys.getLong(1)); // Set the id of the todo object
                    }
                }
            }
        } catch (SQLException e) {
            // Handle the SQL exception
        }
    }

    public List<Todo> selectAllTodosByUser(long userId) {
        List<Todo> todos = new ArrayList<>();
        // Using try-with-resources to auto close resources
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TODOS_BY_USER)) {

            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Todo todo = new Todo(
                        rs.getLong("id"),
                        userId, // We already have the userId
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("targetDate"),
                        rs.getBoolean("isDone")
                );
                todos.add(todo);
            }
        } catch (SQLException e) {
            // Handle the SQL exception
        }
        return todos;
    }
    public boolean updateTodo(Todo todo) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TODOS_SQL)) {
            statement.setString(1, todo.getTitle());
            statement.setString(2, todo.getDescription());
            statement.setDate(3, new java.sql.Date(todo.getTargetDate().getTime()));
            statement.setBoolean(4, todo.isDone); // Direct access
            statement.setLong(5, todo.getId());
            statement.setLong(6, todo.getUserId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public Todo selectTodo(long id, long userId) throws SQLException {
        Todo todo = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TODO_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date targetDate = rs.getDate("targetDate");
                boolean isDone = rs.getBoolean("isDone");
                todo = new Todo(id, userId, title, description, targetDate, isDone);
            }
        }
        return todo;
    }

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
                todos.add(new Todo(id, userId, title, description, targetDate, isDone));
            }
        }
        return todos;
    }

    public boolean deleteTodo(long id, long userId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TODOS_SQL)) {
            statement.setLong(1, id);
            statement.setLong(2, userId);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
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