<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.orderdatabase.model.Product"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container mt-5">
    <h2>Product List</h2>
    <table class="table table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>Product ID</th>
            <th>Product Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Order</th>
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
                <a href="<%= request.getContextPath() %>/product/details?id=<%= product.getProductId() %>" class="btn btn-primary">Order</a>
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
</body>
</html>
