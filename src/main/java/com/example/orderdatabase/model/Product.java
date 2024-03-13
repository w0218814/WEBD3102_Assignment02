package com.example.orderdatabase.model;

public class Product {
    // Identifier for uniquely distinguishing each product in the database.
    private long productId;
    // The name of the product, used for display and identification purposes.
    private String productName;
    // A detailed description of the product, including its features, benefits, and use cases.
    private String productDescription;
    // The retail price of the product, reflecting its cost to the customer.
    private double price;

    // Default constructor that initializes a Product instance without setting its properties.
    public Product() {}

    // Constructor that initializes a Product with all its properties: ID, name, description, and price.
    public Product(long productId, String productName, String productDescription, double price) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
    }

    // Getter and setter methods for each property, providing controlled access to the fields of Product.

    // Returns the unique product identifier.
    public long getProductId() {
        return productId;
    }

    // Sets the unique product identifier.
    public void setProductId(long productId) {
        this.productId = productId;
    }

    // Returns the name of the product.
    public String getProductName() {
        return productName;
    }

    // Sets the name of the product.
    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Returns the description of the product.
    public String getProductDescription() {
        return productDescription;
    }

    // Sets the description of the product.
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    // Returns the price of the product.
    public double getPrice() {
        return price;
    }

    // Sets the price of the product.
    public void setPrice(double price) {
        this.price = price;
    }
}
