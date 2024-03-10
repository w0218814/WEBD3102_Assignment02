package com.example.orderdatabase.model;

public class Product {
    private long productId; // Unique identifier for the Product
    private String productName; // Name of the Product
    private String productDescription; // Description of the Product
    private double price; // Price of the Product

    // Constructors
    public Product() {}

    public Product(long productId, String productName, String productDescription, double price) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
    }

    // Getters and Setters
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
