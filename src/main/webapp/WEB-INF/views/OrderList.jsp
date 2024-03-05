<%@ page import="java.util.List" %>
<%@ page import="your.package.Order" %> <!-- Replace 'your.package' with the actual package where your Order class is located -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .order-list {
            list-style-type: none;
            padding: 0;
        }
        .order-list li {
            margin-bottom: 10px;
            padding: 10px;
            background-color: #f0f0f0;
            border: 1px solid #ddd;
        }
        .order-list li a {
            text-decoration: none;
            color: #333;
        }
        .order-status {
            font-weight: bold;
        }
    </style>
</head>
<body>
<h2>Your Orders</h2>
<ul class="order-list">
    <%
        // Retrieve the list of orders from the request attribute
        List<Order> orderList = (List<Order>) request.getAttribute("orderList");
        if (orderList != null && !orderList.isEmpty()) {
            for (Order order : orderList) {
    %>
    <li>
        <a href="orderDetails?id=<%=order.getId()%>">Order #<%=order.getId()%></a> -
        Status: <span class="order-status"><%=order.getStatus()%></span>
        <!-- Add more details as needed, e.g., order date, total price -->
    </li>
    <%
        }
    } else {
    %>
    <p>No orders found.</p>
    <%
        }
    %>
</ul>
</body>
</html>
