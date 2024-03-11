<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h2>Your order has been placed successfully!</h2>
    <p>Thank you for your purchase.</p>
    <a href="<%=request.getContextPath()%>/product/list" class="btn btn-primary">Continue Shopping</a>
</div>
</body>
</html>
