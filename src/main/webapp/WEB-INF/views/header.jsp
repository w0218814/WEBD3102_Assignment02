<%@ page import="com.example.orderdatabase.model.User" %>
<%@ page session="true" %>

<%
    User loggedInUser = (User) session.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
        <a class="navbar-brand" href="<%= request.getContextPath() %>/">Order Management System</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/">Home</a>
                </li>
                <% if (loggedInUser != null && loggedInUser.getRoleId() == 1) { %>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/admin/listUsers">User Management</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/admin/adminConsole">Admin Console</a>
                </li>
                <% } else if (loggedInUser != null && loggedInUser.getRoleId() == 2) { %>
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/product/list">Products</a>
                </li>
                <% } %>
            </ul>
            <% if (loggedInUser != null) { %>
            <span class="navbar-text">
                        Welcome, <%= loggedInUser.getFullName() %>
                    </span>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/logout">Logout</a>
                </li>
            </ul>
            <% } else { %>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="<%= request.getContextPath() %>/login">Login</a>
                </li>
            </ul>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
