<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.orderdatabase.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product Details</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <%
        Product product = (Product) request.getAttribute("product");
        if (product != null) {
    %>
    <h2 class="mb-3"><%= product.getProductName() %></h2>
    <p><%= product.getProductDescription() %></p>
    <p>Price: $<%= product.getPrice() %></p>
    <p>Status: <%= product.isInStock() ? "In Stock" : "Out of Stock" %></p>
    <a href="#" class="btn btn-success">Add to Cart</a> <!-- Implement Add to Cart functionality -->
    <%
    } else {
    %>
    <p class="text-warning">Product not found.</p>
    <%
        }
    %>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
