package com.example.tododatabase.dao;

import com.example.tododatabase.database.MySQLConnection;
import com.example.tododatabase.model.Todo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {
    private static final String INSERT_TODOS_SQL = "INSERT INTO todos (title, userId, description, targetDate, isDone) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_TODO_BY_ID = "SELECT id, title, description, targetDate, isDone FROM todos WHERE id = ?";
    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos";
    private static final String DELETE_TODOS_SQL = "DELETE FROM todos WHERE id = ?;";
    private static final String UPDATE_TODOS_SQL = "UPDATE todos SET title = ?, description = ?, targetDate = ?, isDone = ? WHERE id = ?;";

    public TodoDAO() {}

    public void insertTodo(Todo todo, long userId) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TODOS_SQL)) {
            preparedStatement.setString(1, todo.getTitle());
            preparedStatement.setLong(2, userId);
            preparedStatement.setString(3, todo.getDescription());
            preparedStatement.setDate(4, new Date(todo.getTargetDate().getTime()));
            preparedStatement.setBoolean(5, todo.isDone());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public boolean updateTodo(Todo todo) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TODOS_SQL)) {
            statement.setString(1, todo.getTitle());
            statement.setString(2, todo.getDescription());
            statement.setDate(3, new Date(todo.getTargetDate().getTime()));
            statement.setBoolean(4, todo.isDone());
            statement.setLong(5, todo.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public Todo selectTodo(long id) {
        Todo todo = null;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TODO_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date targetDate = rs.getDate("targetDate");
                boolean isDone = rs.getBoolean("isDone");
                todo = new Todo(id, title, description, targetDate, isDone);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return todo;
    }

    public List<Todo> selectAllTodos() {
        List<Todo> todos = new ArrayList<>();
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TODOS)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date targetDate = rs.getDate("targetDate");
                boolean isDone = rs.getBoolean("isDone");
                todos.add(new Todo(id, title, description, targetDate, isDone));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return todos;
    }

    public boolean deleteTodo(long id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TODOS_SQL)) {
            statement.setLong(1, id);

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
