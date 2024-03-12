<%@ page import="com.example.orderdatabase.model.User"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        function handleUserSelectChange(selectElement) {
            if(selectElement.value) {
                var userId = selectElement.value;
                var path = '<%= request.getContextPath() %>';
                window.location.href = path + '/admin/editUser?userId=' + userId;
            }
        }
    </script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <h2 class="mt-5">User Management</h2>

    <%
        User user = (User) request.getAttribute("user");
        List<User> users = (List<User>) request.getAttribute("allUsers");
    %>

    <!-- User selection dropdown -->
    <div class="form-group">
        <label for="userSelect">Select User:</label>
        <select class="form-control" id="userSelect" name="userId" onchange="handleUserSelectChange(this);">
            <option value="">Select a user to edit</option>
            <% for(User u : users) { %>
            <option value="<%= u.getId() %>" <%= user != null && user.getId() == u.getId() ? "selected" : "" %>><%= u.getFullName() %></option>
            <% } %>
        </select>
    </div>

    <!-- Start of the form -->
    <form action="<%= request.getContextPath() %>/admin/register" method="post">
        <% if (user != null) { %>
        <input type="hidden" name="id" value="<%= user.getId() %>" />
        <% } %>

        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username" value="<%= user != null ? user.getUsername() : "" %>" required>
        </div>

        <% if (user == null) { %>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <% } %>

        <div class="form-group">
            <label for="fullName">Full Name:</label>
            <input type="text" class="form-control" id="fullName" name="fullName" value="<%= user != null ? user.getFullName() : "" %>" required>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" name="email" value="<%= user != null ? user.getEmail() : "" %>" required>
        </div>

        <div class="form-group">
            <label for="street">Street:</label>
            <input type="text" class="form-control" id="street" name="street" value="<%= user != null ? user.getStreet() : "" %>">
        </div>

        <div class="form-group">
            <label for="city">City:</label>
            <input type="text" class="form-control" id="city" name="city" value="<%= user != null ? user.getCity() : "" %>">
        </div>

        <div class="form-group">
            <label for="nearbyLandmark">Nearby Landmark:</label>
            <input type="text" class="form-control" id="nearbyLandmark" name="nearbyLandmark" value="<%= user != null ? user.getNearbyLandmark() : "" %>">
        </div>

        <div class="form-group">
            <label for="province">Province:</label>
            <input type="text" class="form-control" id="province" name="province" value="<%= user != null ? user.getProvince() : "" %>">
        </div>

        <div class="form-group">
            <label for="postalCode">Postal Code:</label>
            <input type="text" class="form-control" id="postalCode" name="postalCode" value="<%= user != null ? user.getPostalCode() : "" %>">
        </div>

        <div class="form-group">
            <label for="phoneNumber">Phone Number:</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" value="<%= user != null ? user.getPhoneNumber() : "" %>">
        </div>

        <div class="form-group">
            <label for="role">Role:</label>
            <select class="form-control" id="role" name="roleId">
                <option value="1" <%= user != null && user.getRoleId() == 1 ? "selected" : "" %>>Admin</option>
                <option value="2" <%= user != null && user.getRoleId() == 2 ? "selected" : "" %>>User</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary"><%= user != null ? "Update User" : "Register User" %></button>
    </form>
</div>
</body>
</html>
