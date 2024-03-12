<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.orderdatabase.model.Product"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="com.example.orderdatabase.model.User"%>
<%@ include file="header.jsp" %>

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
            <% HttpSession sessionLive = request.getSession(false); %>
            <% if (sessionLive != null && sessionLive.getAttribute("user") != null) { %>
            // User is logged in, proceed to confirm the order
            fetchProductDetailsAndConfirmOrder(productId);
            <% } else { %>
            // User is not logged in, store the product ID and redirect to login
            sessionStorage.setItem('orderProductId', productId);
            window.location.href = '<%=request.getContextPath()%>/login';
            <% } %>
        }

        function fetchProductDetailsAndConfirmOrder(productId) {
            $.ajax({
                url: '<%=request.getContextPath()%>/productDetail',
                type: 'GET',
                data: { productId: productId },
                success: function(product) {
                    var orderConfirmation = confirm(
                        "Product ID: " + product.productId + "\n" +
                        "Product Name: " + product.productName + "\n" +
                        "Description: " + product.productDescription + "\n" +
                        "Price: $" + product.price.toFixed(2) + "\n\n" +
                        "Do you want to confirm the order?"
                    );
                    if (orderConfirmation) {
                        // If the user confirms, proceed to place the order
                        placeOrder(productId, product.price);
                    }
                },
                error: function() {
                    alert('There was an error fetching product details. Please try again.');
                }
            });
        }

        function placeOrder(productId, price) {
            <% if (session != null && session.getAttribute("user") != null) { %>
            var userId = <%= ((User) session.getAttribute("user")).getId() %>;
            $.ajax({
                url: '<%=request.getContextPath()%>/order/insert',
                type: 'POST',
                data: {
                    userId: userId,
                    productId: productId,
                    price: price,
                    quantity: 1 // This example assumes a quantity of 1; adjust as needed
                },
                success: function(response) {
                    alert('Your order has been placed successfully!');
                    window.location.reload(); // Reload the page to refresh the state
                },
                error: function() {
                    alert('There was an error placing your order. Please try again.');
                }
            });
            <% } else { %>
            // User is not logged in or session has expired
            alert('Your session has expired. Please log in again.');
            window.location.href = '<%=request.getContextPath()%>/login';
            <% } %>
        }

        // On page load, check if we have an orderProductId stored from a previous session (pre-login)
        $(document).ready(function() {
            var productId = sessionStorage.getItem('orderProductId');
            if (productId) {
                sessionStorage.removeItem('orderProductId'); // Clear the stored ID
                handleOrder(productId); // This will now fetch product details and proceed to order
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
                <button onclick="handleOrder(<%= product.getProductId() %>)" class="btn btn-primary">Order</button>
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