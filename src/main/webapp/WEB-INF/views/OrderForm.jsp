<%@ page import="com.example.orderdatabase.model.Order"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Order</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h2 class="mt-5">Edit Order</h2>
    <%
        Order order = (Order) request.getAttribute("order");
        if(order != null) {
    %>
    <form action="<%= request.getContextPath() %>/order/update" method="post">
        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">

        <div class="form-group">
            <label for="totalAmount">Total Amount:</label>
            <input type="text" class="form-control" id="totalAmount" name="totalAmount" value="<%= order.getTotalAmount().toString() %>" required>
        </div>

        <div class="form-group">
            <label for="status">Status:</label>
            <input type="text" class="form-control" id="status" name="status" value="<%= order.getStatus() %>" required>
        </div>

        <div class="form-group">
            <label for="isFulfilled">Is Fulfilled:</label>
            <select class="form-control" id="isFulfilled" name="isFulfilled">
                <option value="true" <%= order.isFulfilled() ? "selected" : "" %>>Yes</option>
                <option value="false" <%= !order.isFulfilled() ? "selected" : "" %>>No</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Update Order</button>
    </form>
    <% } else { %>
    <p>Order not found.</p>
    <% } %>
</div>
</body>
</html>
