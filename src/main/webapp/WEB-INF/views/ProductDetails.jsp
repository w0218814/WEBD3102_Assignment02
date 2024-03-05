<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="your.package.Product" %> <!-- Make sure to replace 'your.package' with the actual package name where your Product class is located -->
<html>
<head>
    <title>Product Details</title>
    <style>
        /* Basic styling; replace or extend as needed */
        body {
            font-family: Arial, sans-serif;
        }
        .product-image {
            max-width: 300px;
            max-height: 300px;
            padding: 10px;
        }
    </style>
</head>
<body>
<%
    // Retrieve the product object passed from the servlet
    Product product = (Product) request.getAttribute("product");
    if (product != null) {
%>
<h2><%= product.getName() %></h2>
<% if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) { %>
<img src="<%= product.getImageUrl() %>" alt="Product Image" class="product-image"/>
<% } %>
<p><%= product.getDescription() %></p>
<p>Category: <%= product.getCategory() %></p> <!-- Assuming there's a getCategory() method -->
<p>Price: $<%= product.getPrice() %></p>
<!-- Add more product details as needed -->
<a href="addToCart?id=<%= product.getId() %>">Add to Cart</a> <!-- Placeholder link; implement cart functionality as needed -->
<%
} else {
%>
<p>Product not found.</p>
<%
    }
%>
</body>
</html>
