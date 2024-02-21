<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Console</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <h1 class="mt-5">Admin Console</h1>
    <%
        // Check if there is a message to display and if so, display it
        if (session != null && session.getAttribute("updateMessage") != null) {
    %>
    <div class="alert alert-success" role="alert">
        <%= session.getAttribute("updateMessage") %> <!-- Display the success message -->
    </div>
    <%
        // Remove the message from the session after displaying it
        session.removeAttribute("updateMessage");
    %>
    <% } %>
    <p>Welcome to the Admin Console! Here, you can manage users, roles, and other administrative tasks.</p>

    <!-- Links for admin tasks -->
    <div class="list-group">
        <a href="<%= request.getContextPath() %>/user/adminRegister" class="list-group-item list-group-item-action">Manage Users</a>
        <a href="<%= request.getContextPath() %>/todo/list" class="list-group-item list-group-item-action">Manage Todo Lists</a>
    </div>
</div>

</body>
</html>
