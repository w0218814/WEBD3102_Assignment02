<%@ page import="com.example.orderdatabase.model.Order"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <h2 class="mt-5">Order Management</h2>

    <%
        Order order = (Order) request.getAttribute("order");
        List<Order> orders = (List<Order>) request.getAttribute("listOrder");
    %>

    <!-- Order selection dropdown for editing -->
    <div class="form-group">
        <label for="orderSelect">Select Order to Edit:</label>
        <select class="form-control" id="orderSelect" onchange="location.href='<%= request.getContextPath() %>/order/edit?id='+this.value;">
            <option value="">Select an order</option>
            <% for (Order o : orders) { %>
            <option value="<%= o.getOrderId() %>" <%= (order != null && order.getOrderId() == o.getOrderId()) ? "selected" : "" %>><%= "Order " + o.getOrderId() + " - " + o.getStatus() %></option>
            <% } %>
        </select>
    </div>

    <!-- Form for updating an order -->
    <form action="<%= request.getContextPath() %>/order/update" method="post">
        <% if (order != null) { %>
        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>" />
        <div class="form-group">
            <label for="status">Status:</label>
            <input type="text" class="form-control" id="status" name="status" value="<%= order.getStatus() %>" placeholder="Status" required>
        </div>

        <div class="form-group">
            <label for="isFulfilled">Fulfilled:</label>
            <select class="form-control" id="isFulfilled" name="isFulfilled">
                <option value="true" <%= order.isFulfilled() ? "selected" : "" %>>Yes</option>
                <option value="false" <%= !order.isFulfilled() ? "selected" : "" %>>No</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Update Order</button>
        <% } else { %>
        <p>Please select an order to edit.</p>
        <% } %>
    </form>
</div>
</body>
</html>
