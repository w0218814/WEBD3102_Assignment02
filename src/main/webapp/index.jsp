<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome to TodoApplication</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 20px;
        }
        .navbar {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <h1 class="text-center">Welcome to TodoApplication</h1>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-item nav-link" href="${pageContext.request.contextPath}/user/login">Login</a>
                <a class="nav-item nav-link" href="${pageContext.request.contextPath}/user/register">Register</a>
                <% if (session != null && session.getAttribute("user") != null) { %>
                <a class="nav-item nav-link" href="${pageContext.request.contextPath}/todo/list">View Todo List</a>
                <a class="nav-item nav-link" href="${pageContext.request.contextPath}/user/logout">Logout</a>
                <% } %>
            </div>
        </div>
    </nav>
    <p>This is a simple todo list application that allows you to manage your tasks efficiently.</p>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>