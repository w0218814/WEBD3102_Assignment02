package com.example.orderdatabase.model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private long orderId;
    private long userId;
    private Date orderDate;
    private boolean isFulfilled;

    // Constructors
    public Order() {}

    public Order(long orderId, long userId, Date orderDate, boolean isFulfilled) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.isFulfilled = isFulfilled;
    }

    // Getters and Setters

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

    public boolean isFulfilled() {
        return isFulfilled;
    }

    public void setFulfilled(boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
    }
}
