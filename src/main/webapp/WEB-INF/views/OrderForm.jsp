<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script>
        function confirmOrder() {
            var isConfirmed = confirm("1 Click Ordering is Enabled, are you sure you wish to order this product?");
            if (isConfirmed) {
                alert("Order confirmed");
                return true; // Proceed with form submission
            } else {
                return false; // Prevent form submission
            }
        }
    </script>
</head>
<body>
<div class="container">
    <h2 class="mt-5">Place Your Order</h2>
    <form action="${pageContext.request.contextPath}/order/insert" method="post" onsubmit="return confirmOrder()">
        <input type="hidden" name="userId" value="${sessionScope.user.id}"> <!-- Assuming you have the user in the session -->
        <input type="hidden" name="productId" value="${param.productId}"> <!-- Assuming product ID is passed as a parameter -->
        <input type="hidden" name="totalAmount" value="100"> <!-- Example total amount, adjust as needed -->
        <input type="hidden" name="status" value="New"> <!-- Example status, adjust as needed -->
        <button type="submit" class="btn btn-primary">Order Now</button>
    </form>
</div>
</body>
</html>
