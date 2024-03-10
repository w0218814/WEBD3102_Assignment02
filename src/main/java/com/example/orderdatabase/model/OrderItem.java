package com.example.orderdatabase.model;

import java.math.BigDecimal;

public class OrderItem {
    private long orderItemId;
    private long orderId;
    private long productId;
    private int quantity;
    private BigDecimal price;

    // Constructors
    public OrderItem() {}

    public OrderItem(long orderItemId, long orderId, long productId, int quantity, BigDecimal price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
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
