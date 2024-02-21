<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %> <!-- Importing List class -->
<%@ page import="com.example.tododatabase.model.User" %> <!-- Importing User class -->
<%@ page import="com.example.tododatabase.model.Todo" %> <!-- Importing Todo class -->
<%@ include file="header.jsp" %> <!-- Including header.jsp file -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Todo List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> <!-- Bootstrap CSS -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> <!-- Bootstrap JS -->
</head>
<body>

<div class="container mt-5">
    <h2>Todo List</h2>
    <%
        User currentUser = (User) request.getSession().getAttribute("user"); // Retrieving current user from session
        if ("admin".equalsIgnoreCase(currentUser.getRoleName())) { // Checking if current user is admin
            List<User> users = (List<User>) request.getAttribute("allUsers"); // Retrieving list of users from request
    %>
    <div class="mb-3">
        <label for="userSelect">Select User:</label>
        <select id="userSelect" class="form-control" onchange="location.href='?userId=' + this.value;">
            <% for (User user : users) { %>
            <option value="<%= user.getId() %>"
                    <%= request.getParameter("userId") != null && request.getParameter("userId").equals(String.valueOf(user.getId())) ? "selected" : "" %>>
                <%= user.getFullName() %>
            </option>
            <% } %>
        </select>
    </div>
    <% } %>
    <a href="<%= request.getContextPath() %>/todo/new" class="btn btn-success mb-3">Add New Todo</a> <!-- Link to add new todo -->
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
            List<Todo> listTodo = (List<Todo>) request.getAttribute("listTodo"); // Retrieving list of todos from request
            for (Todo todo : listTodo) { // Iterating over todos
        %>
        <tr>
            <td><%= todo.getTitle() %></td> <!-- Displaying todo title -->
            <td><%= todo.getDescription() %></td> <!-- Displaying todo description -->
            <td><%= todo.getTargetDate().toString() %></td> <!-- Displaying todo target date -->
            <td><%= todo.isDone ? "Done" : "Pending" %></td> <!-- Displaying todo status -->
            <td>
                <a href="<%= request.getContextPath() %>/todo/edit?id=<%= todo.getId() %>&userId=<%= todo.getUserId() %>" class="btn btn-primary">Edit</a> <!-- Link to edit todo -->
                <form action="<%= request.getContextPath() %>/todo/delete" method="post" style="display: inline;">
                    <input type="hidden" name="id" value="<%= todo.getId() %>" /> <!-- Hidden input to send todo ID -->
                    <button type="submit" class="btn btn-danger">Delete</button> <!-- Button to delete todo -->
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>

</body>
</html>
