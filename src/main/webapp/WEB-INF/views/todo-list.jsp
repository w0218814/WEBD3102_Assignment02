<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.tododatabase.model.User" %>
<%@ page import="com.example.tododatabase.model.Todo" %>
<%@ include file="header.jsp" %>
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
    <% User currentUser = (User) request.getSession().getAttribute("user");
        if ("admin".equalsIgnoreCase(currentUser.getRoleName())) {
            List<User> users = (List<User>) request.getAttribute("allUsers"); // This needs to be provided by your servlet
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
        <% List<Todo> listTodo = (List<Todo>) request.getAttribute("listTodo");
            for (Todo todo : listTodo) { %>
        <tr>
            <td><%= todo.getTitle() %></td>
            <td><%= todo.getDescription() %></td>
            <td><%= todo.getTargetDate().toString() %></td>
            <td><%= todo.isDone ? "Done" : "Pending" %></td>
                        <td>
                <a href="<%= request.getContextPath() %>/todo/edit?id=<%= todo.getId() %>" class="btn btn-primary">Edit</a>
                <form action="<%= request.getContextPath() %>/todo/delete" method="post" style="display: inline;">
                    <input type="hidden" name="id" value="<%= todo.getId() %>" />
                    <button type="submit" class="btn btn-danger">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>

</body>
</html>
