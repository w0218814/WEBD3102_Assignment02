package com.example.orderdatabase.model;

import java.math.BigDecimal;

public class OrderItem {
    // Unique identifier for the order item
    private long orderItemId;
    // Identifier for the order this item belongs to
    private long orderId;
    // Identifier for the product being ordered
    private long productId;
    // Quantity of the product ordered
    private int quantity;
    // Price of the product at the time of the order
    private BigDecimal price;

    // Default constructor initializes an empty OrderItem
    public OrderItem() {}

    // Constructor to initialize all fields of an OrderItem
    public OrderItem(long orderItemId, long orderId, long productId, int quantity, BigDecimal price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getter and setter methods for each field
    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
