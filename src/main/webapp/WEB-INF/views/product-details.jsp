<%@ page import="com.example.orderdatabase.model.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Product Details</title>
</head>
<body>
<h2>Product Details</h2>
<!-- Example Product Display -->
<p>Name: ${product.productName}</p>
<p>Description: ${product.productDescription}</p>
<p>Price: ${product.price}</p>
<button id="orderButton">1-Click Order</button>

<script>
    document.getElementById("orderButton").addEventListener("click", function() {
        const confirmed = confirm("1 Click Ordering is Enabled, are you sure you wish to order this product?");
        if (confirmed) {
            window.location.href = "<%=request.getContextPath()%>/product/order?productId=${product.productId}";
        }
    });
</script>
</body>
</html>
