<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> <!-- Bootstrap CSS -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> <!-- Bootstrap JS -->
</head>
<body>

<div class="container">
    <h2 class="mt-5">Login</h2>
    <%-- Display error message if it exists --%>
    <% if (request.getParameter("error") != null) { %> <!-- Checking for error parameter in request -->
    <div class="alert alert-danger" role="alert">
        <%= request.getParameter("error") %> <!-- Displaying error message -->
    </div>
    <% } %>
    <form action="<%=request.getContextPath()%>/login" method="post"> <!-- Form for login action -->
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <button type="submit" class="btn btn-primary">Login</button> <!-- Login button -->
    </form>
    <p class="mt-2">
        Don't have an account? <a href="<%=request.getContextPath()%>/user/register">Register here</a> <!-- Link to registration page -->
    </p>
</div>

</body>
</html>
