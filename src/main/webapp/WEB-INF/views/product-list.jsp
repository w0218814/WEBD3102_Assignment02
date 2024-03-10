<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%> <!-- Importing List class -->
<%@ page import="com.example.orderdatabase.model.Product"%> <!-- Importing Product class -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> <!-- Bootstrap CSS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- jQuery -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> <!-- Bootstrap JS -->
</head>
<body>
<div class="container mt-5">
    <h2>Product List</h2>
    <a href="<%= request.getContextPath() %>/product/new" class="btn btn-success mb-3">Add New Product</a>
    <table class="table table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>Product ID</th>
            <th>Product Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Product> listProduct = (List<Product>) request.getAttribute("listProduct");
            if (listProduct != null && !listProduct.isEmpty()) {
                for (Product product : listProduct) {
        %>
        <tr>
            <td><%= product.getProductId() %></td>
            <td><%= product.getProductName() %></td>
            <td><%= product.getProductDescription() %></td>
            <td><%= product.getPrice() %></td>
            <td>
                <a href="<%= request.getContextPath() %>/product/edit?id=<%= product.getProductId() %>" class="btn btn-primary">Edit</a>
                <a href="<%= request.getContextPath() %>/product/delete?id=<%= product.getProductId() %>" class="btn btn-danger">Delete</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="5" class="text-center">No products found</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
<script>
    // Function to go back to the previous page in the browser's history
    function goBack() {
        window.history.back();
    }
</script>
</body>
</html>
