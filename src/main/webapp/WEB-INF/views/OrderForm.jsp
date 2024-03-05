<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Place Order</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <h2>Place a New Order</h2>
    <form action="OrderServlet" method="post" class="mt-3">
        <div class="form-group">
            <label for="product">Product:</label>
            <select name="product" id="product" class="form-control">
                <!-- Options should be populated dynamically from the server-side -->
                <option value="1">Product 1</option>
                <option value="2">Product 2</option>
            </select>
        </div>
        <div class="form-group">
            <label for="quantity">Quantity:</label>
            <input type="number" id="quantity" name="quantity" min="1" value="1" class="form-control">
        </div>
        <!-- Add any other form fields required for the order -->
        <button type="submit" class="btn btn-primary">Place Order</button>
    </form>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
