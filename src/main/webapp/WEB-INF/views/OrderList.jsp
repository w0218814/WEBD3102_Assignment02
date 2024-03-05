<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.orderdatabase.model.Order" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <h2>Your Orders</h2>
    <ul class="list-group mt-3">
        <%
            List<Order> orderList = (List<Order>) request.getAttribute("orderList");
            if (orderList != null && !orderList.isEmpty()) {
                for (Order order : orderList) {
        %>
        <li class="list-group-item d-flex justify-content-between align-items-center">
            Order #<%= order.getOrderId() %>
            <span class="badge badge-primary badge-pill"><%= order.getStatus() %></span>
            <!-- Implement additional order details display -->
        </li>
        <%
            }
        } else {
        %>
        <li class="list-group-item">No orders found.</li>
        <%
            }
        %>
    </ul>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
