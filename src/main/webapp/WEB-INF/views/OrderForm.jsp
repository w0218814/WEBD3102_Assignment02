<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Place Order</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        form {
            width: 300px;
            margin: auto;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="number"], select, input[type="text"] {
            width: 100%;
            padding: 8px;
            margin: 4px 0;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            width: 100%;
            background-color: #4CAF50;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<h2>Place a New Order</h2>
<form action="OrderServlet" method="post">
    <div class="form-group">
        <label for="product">Product:</label>
        <select name="product" id="product">
            <!-- Dynamically generate product options based on available inventory -->
            <option value="1">Product 1</option>
            <option value="2">Product 2</option>
            <!-- Add more options here -->
        </select>
    </div>
    <div class="form-group">
        <label for="quantity">Quantity:</label>
        <input type="number" id="quantity" name="quantity" min="1" value="1">
    </div>
    <!-- Consider adding more fields such as customer details or delivery options -->
    <div class="form-group">
        <label for="customerName">Your Name:</label>
        <input type="text" id="customerName" name="customerName">
    </div>
    <div class="form-group">
        <label for="address">Delivery Address:</label>
        <input type="text" id="address" name="address">
    </div>
    <input type="submit" value="Place Order">
</form>
</body>
</html>
