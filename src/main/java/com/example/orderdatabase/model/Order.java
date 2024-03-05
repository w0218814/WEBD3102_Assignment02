package com.example.orderdatabase.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private long orderId;
    private long userId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private boolean isFulfilled;

    // Default constructor
    public Order() {
        // Default constructor is often required for ORM (Object-Relational Mapping) frameworks and form submissions.
    }

    // Constructor with all the fields for complete instantiation
    public Order(long orderId, long userId, Date orderDate, double totalAmount, String status, boolean isFulfilled) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.isFulfilled = isFulfilled;
    }

    // Getters and setters for all fields

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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
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

    public void setFulfilled(boolean fulfilled) {
        isFulfilled = fulfilled;
    }

    // Overridden toString method for object representation in string format

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", isFulfilled=" + isFulfilled +
                '}';
    }

    // Overridden equals and hashCode methods for object comparison

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getOrderId() == order.getOrderId() &&
                getUserId() == order.getUserId() &&
                Double.compare(order.getTotalAmount(), getTotalAmount()) == 0 &&
                isFulfilled() == order.isFulfilled() &&
                getOrderDate().equals(order.getOrderDate()) &&
                Objects.equals(getStatus(), order.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getUserId(), getOrderDate(), getTotalAmount(), getStatus(), isFulfilled());
    }
}
