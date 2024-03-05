<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
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
    <h2 class="mb-3">Product List</h2>
    <%
        List<Product> listProduct = (List<Product>) request.getAttribute("listProduct");
        if (listProduct != null && !listProduct.isEmpty()) {
            for (Product product : listProduct) {
    %>
    <div class="card mb-3">
        <div class="card-body">
            <h5 class="card-title"><%= product.getProductName() %></h5>
            <p class="card-text"><%= product.getProductDescription() %></p>
            <p>Price: $<%= product.getPrice() %></p>
            <p>Status: <%= product.isInStock() ? "In Stock" : "Out of Stock" %></p>
            <a href="#" class="btn btn-success">Add to Cart</a> <!-- Implement Add to Cart functionality -->
        </div>
    </div>
    <%
        }
    } else {
    %>
    <p class="text-warning">No products found.</p>
    <%
        }
    %>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
