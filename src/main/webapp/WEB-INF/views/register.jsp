<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Registration</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- jQuery -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> <!-- Bootstrap CSS -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> <!-- Bootstrap JS -->
</head>
<body>

<div class="container">
    <h2 class="mt-5">User Registration</h2>

    <form action="<%= request.getContextPath() %>/admin/register" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="form-group">
            <label for="fullName">Full Name:</label>
            <input type="text" class="form-control" id="fullName" name="fullName" required>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" name="email" required>
        </div>
        <!-- New fields for address information -->
        <div class="form-group">
            <label for="street">Street:</label>
            <input type="text" class="form-control" id="street" name="street">
        </div>
        <div class="form-group">
            <label for="city">City:</label>
            <input type="text" class="form-control" id="city" name="city">
        </div>
        <div class="form-group">
            <label for="nearbyLandmark">Nearby Landmark:</label>
            <input type="text" class="form-control" id="nearbyLandmark" name="nearbyLandmark">
        </div>
        <div class="form-group">
            <label for="province">Province:</label>
            <input type="text" class="form-control" id="province" name="province">
        </div>
        <div class="form-group">
            <label for="postalCode">Postal Code:</label>
            <input type="text" class="form-control" id="postalCode" name="postalCode">
        </div>
        <div class="form-group">
            <label for="phoneNumber">Phone Number:</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber">
        </div>
        <!-- The role field might need to be handled differently based on your user management system -->
        <div class="form-group">
            <label for="role">Role:</label>
            <select class="form-control" id="role" name="role">
                <option value="2" selected>User</option>
                <option value="1">Admin</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Register User</button>
    </form>
</div>

</body>
</html>
