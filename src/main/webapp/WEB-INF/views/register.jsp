<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.tododatabase.model.User" %>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("user") != null ? "Edit User" : "User Registration" %></title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <h2 class="mt-5"><%= request.getAttribute("user") != null ? "Edit User" : "User Registration" %></h2>

    <%
        User currentUser = (User) request.getSession(false).getAttribute("user");
        boolean isAdmin = currentUser != null && "admin".equalsIgnoreCase(currentUser.getRoleName());
        if (isAdmin) {
            List<User> users = (List<User>) request.getAttribute("allUsers");
    %>
    <form action="<%= request.getContextPath() %>/user/editUser" method="get">
        <div class="form-group">
            <label for="userSelect">Select User to Edit:</label>
            <select class="form-control" id="userSelect" name="userId" onchange="this.form.submit()">
                <option value="">Choose...</option>
                <% for (User user : users) { %>
                <option value="<%= user.getId() %>" <%= request.getParameter("userId") != null && request.getParameter("userId").equals(String.valueOf(user.getId())) ? "selected" : "" %>><%= user.getUsername() %></option>
                <% } %>
            </select>
        </div>
    </form>
    <% } %>

    <form action="<%= request.getContextPath() %>/user/<%= request.getAttribute("user") != null ? "update" : "register" %>" method="post">
        <% if (request.getAttribute("user") != null) { %>
        <input type="hidden" name="id" value="<%= ((User) request.getAttribute("user")).getId() %>"/>
        <% } %>
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username" value="<%= request.getAttribute("user") != null ? ((User) request.getAttribute("user")).getUsername() : "" %>" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password" <%= request.getAttribute("user") == null ? "required" : "" %>>
            <% if (request.getAttribute("user") != null) { %>
            <small class="form-text text-muted">Leave blank to keep the current password.</small>
            <% } %>
        </div>
        <div class="form-group">
            <label for="fullName">Full Name:</label>
            <input type="text" class="form-control" id="fullName" name="fullName" value="<%= request.getAttribute("user") != null ? ((User) request.getAttribute("user")).getFullName() : "" %>" required>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" name="email" value="<%= request.getAttribute("user") != null ? ((User) request.getAttribute("user")).getEmail() : "" %>" required>
        </div>
        <% if (isAdmin) { %>
        <div class="form-group">
            <label for="role">Role:</label>
            <select class="form-control" id="role" name="role">
                <option value="user" <%= request.getAttribute("user") != null && "user".equals(((User) request.getAttribute("user")).getRoleName()) ? "selected" : "" %>>User</option>
                <option value="admin" <%= request.getAttribute("user") != null && "admin".equals(((User) request.getAttribute("user")).getRoleName()) ? "selected" : "" %>>Admin</option>
            </select>
        </div>
        <% } else { %>
        <input type="hidden" name="role" value="user">
        <% } %>
        <button type="submit" class="btn btn-primary"><%= request.getAttribute("user") != null ? "Update User" : "Register User" %></button>
    </form>
</div>

</body>
</html>
