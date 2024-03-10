package com.example.orderdatabase.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
    private long orderId;
    private long userId;
    private Date orderDate;
    private BigDecimal totalAmount;
    private String status;
    private boolean isFulfilled;
    private List<OrderItem> orderItems; // A list to hold order items

    // Default constructor
    public Order() {}

    // Constructor without order items
    public Order(long orderId, long userId, Date orderDate, BigDecimal totalAmount, String status, boolean isFulfilled) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.isFulfilled = isFulfilled;
    }

    // Constructor including order items
    public Order(long orderId, long userId, Date orderDate, BigDecimal totalAmount, String status, boolean isFulfilled, List<OrderItem> orderItems) {
        this(orderId, userId, orderDate, totalAmount, status, isFulfilled);
        this.orderItems = orderItems;
    }

    // Getters and setters
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
