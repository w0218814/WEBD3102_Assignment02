package com.example.orderdatabase.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
    // Unique identifier for the order
    private long orderId;
    // Unique identifier for the user who placed the order
    private long userId;
    // The date and time when the order was placed
    private Date orderDate;
    // Total monetary value of the order
    private BigDecimal totalAmount;
    // Current status of the order (e.g., "Shipped", "Processing")
    private String status;
    // Flag indicating whether the order has been fulfilled
    private boolean isFulfilled;
    // Collection of items that are part of this order
    private List<OrderItem> orderItems;

    // Constructs an Order object without initializing its fields
    public Order() {}

    // Constructs an Order object without including order items
    public Order(long orderId, long userId, Date orderDate, BigDecimal totalAmount, String status, boolean isFulfilled) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.isFulfilled = isFulfilled;
    }

    // Constructs an Order object including order items
    public Order(long orderId, long userId, Date orderDate, BigDecimal totalAmount, String status, boolean isFulfilled, List<OrderItem> orderItems) {
        this(orderId, userId, orderDate, totalAmount, status, isFulfilled); // Calls the constructor above
        this.orderItems = orderItems;
    }

    // Standard getter and setter methods follow for each field
    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFulfilled() {
        return isFulfilled;
    }

    public void setFulfilled(boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
