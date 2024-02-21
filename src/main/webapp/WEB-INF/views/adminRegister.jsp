<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.tododatabase.model.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("user") != null ? "Edit User" : "User Registration" %></title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h2 class="mt-5"><%= request.getAttribute("user") != null ? "Edit User" : "User Registration" %></h2>

    <!-- Dropdown for selecting a user to edit -->
    <div class="form-group">
        <label for="userSelect">Select User to Edit:</label>
        <select class="form-control" id="userSelect" name="userId">
            <option value="">Choose...</option>
            <% List<User> users = (List<User>) request.getAttribute("allUsers");
                for (User user : users) {
                    String selected = (request.getAttribute("user") != null && user.getId() == ((User) request.getAttribute("user")).getId()) ? "selected" : "";
            %>
            <option value="<%= user.getId() %>" <%= selected %>><%= user.getUsername() %></option>
            <% } %>
        </select>
    </div>

    <!-- Form for registering or editing a user -->
    <form action="<%= request.getContextPath() %>/admin/register" method="post">
        <% User editingUser = (User) request.getAttribute("user");
            String usernameValue = editingUser != null ? editingUser.getUsername() : "";
            String fullNameValue = editingUser != null ? editingUser.getFullName() : "";
            String emailValue = editingUser != null ? editingUser.getEmail() : "";
            String roleNameValue = editingUser != null ? editingUser.getRoleName() : "";
        %>
        <% if (editingUser != null) { %>
        <input type="hidden" name="id" value="<%= editingUser.getId() %>"/>
        <% } %>
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username" value="<%= usernameValue %>" required>
        </div>
        <!-- Only show password field for new registrations -->
        <% if (editingUser == null) { %>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <% } %>
        <div class="form-group">
            <label for="fullName">Full Name:</label>
            <input type="text" class="form-control" id="fullName" name="fullName" value="<%= fullNameValue %>" required>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" name="email" value="<%= emailValue %>" required>
        </div>
        <div class="form-group">
            <label for="roleName">Role:</label>
            <select class="form-control" id="roleName" name="roleName">
                <option value="admin" <%= "admin".equals(roleNameValue) ? "selected" : "" %>>Admin</option>
                <option value="user" <%= "user".equals(roleNameValue) ? "selected" : "" %>>User</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary"><%= editingUser != null ? "Update User" : "Register User" %></button>
    </form>
</div>

<script>
    // When the selected user changes, redirect with the selected user's ID as a query parameter
    $('#userSelect').change(function() {
        var userId = $(this).val();
        if(userId) {
            window.location.href = '<%= request.getContextPath() %>/admin/editUser?userId=' + userId;
        }
    });
</script>
</body>
</html>
