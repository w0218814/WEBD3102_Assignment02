package com.example.orderdatabase.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Product implements Serializable {

    // serialVersionUID is used for serialization and deserialization
    private static final long serialVersionUID = 1L;

    // Class attributes corresponding to the products table columns in the database
    private long productId;
    private String productName;
    private String productDescription;
    private double price;
    private boolean inStock;
    private Date createdAt;
    private Date updatedAt;

    // Default constructor
    public Product() {
        // Default constructor is often required for ORM (Object-Relational Mapping) frameworks and form submissions.
    }

    // Constructor with parameters for product creation without ID (auto-generated)
    public Product(String productName, String productDescription, double price, boolean inStock) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.inStock = inStock;
    }
    public Product(long productId, String productName, String productDescription, double price, boolean inStock) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.inStock = inStock;
        // The createdAt and updatedAt fields are omitted, potentially to be managed by the database.
    }

    // Full constructor including productId for complete instantiation
    public Product(long productId, String productName, String productDescription, double price, boolean inStock, Date createdAt, Date updatedAt) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.inStock = inStock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters for each attribute
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

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Override toString for object representation in a string format
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Override equals and hashCode for object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getProductId() == product.getProductId() &&
                Double.compare(product.getPrice(), getPrice()) == 0 &&
                isInStock() == product.isInStock() &&
                getProductName().equals(product.getProductName()) &&
                Objects.equals(getProductDescription(), product.getProductDescription()) &&
                Objects.equals(getCreatedAt(), product.getCreatedAt()) &&
                Objects.equals(getUpdatedAt(), product.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getProductName(), getProductDescription(), getPrice(), isInStock(), getCreatedAt(), getUpdatedAt());
    }
}
