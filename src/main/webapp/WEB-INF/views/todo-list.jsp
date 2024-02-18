<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.tododatabase.model.Todo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Todo List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container mt-5">
    <h2>Todo List</h2>
    <a href="<%= request.getContextPath() %>/todo/new" class="btn btn-success mb-3">Add New Todo</a>
    <table class="table table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>Title</th>
            <th>Description</th>
            <th>Target Date</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Todo> listTodo = (List<Todo>) request.getAttribute("listTodo");
            if (listTodo != null) {
                for (Todo todo : listTodo) {
        %>
        <tr>
            <td><%= todo.getTitle() %></td>
            <td><%= todo.getDescription() %></td>
            <td><%= todo.getTargetDate().toString() %></td>
            <td><%= todo.isDone ? "Done" : "Pending" %></td> <!-- Direct access to public field -->
            <td>
                <a href="<%= request.getContextPath() %>/todo/edit?id=<%= todo.getId() %>" class="btn btn-primary">Edit</a>
                <a href="<%= request.getContextPath() %>/todo/delete?id=<%= todo.getId() %>" class="btn btn-danger">Delete</a>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>

</body>
</html>
