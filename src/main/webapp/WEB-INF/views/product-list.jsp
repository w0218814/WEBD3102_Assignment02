<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.orderdatabase.model.Product"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        function handleOrder(productId) {
            <% HttpSession sessionCurrent = request.getSession(false); %>
            <% if (sessionCurrent != null && sessionCurrent.getAttribute("user") != null) { %>
            // User is logged in, proceed to confirm the order
            confirmOrder(productId);
            <% } else { %>
            // User is not logged in, store the product ID and redirect to login
            sessionStorage.setItem('orderProductId', productId);
            window.location.href = '<%=request.getContextPath()%>/login';
            <% } %>
        }

        function confirmOrder(productId) {
            // Placeholder for your confirm order logic
            alert('Implement product order confirmation logic for product ID: ' + productId);
        }

        $(document).ready(function() {
            // Automatically try to confirm an order if a product ID is saved
            var productId = sessionStorage.getItem('orderProductId');
            if (productId) {
                sessionStorage.removeItem('orderProductId'); // Clear the stored ID
                confirmOrder(productId);
            }
        });
    </script>
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
            <td>$<%= String.format("%.2f", product.getPrice()) %></td>
            <td>
                <button onclick="handleOrder('<%= product.getProductId() %>')" class="btn btn-primary">Order</button>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="5" class="text-center">No products found.</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
