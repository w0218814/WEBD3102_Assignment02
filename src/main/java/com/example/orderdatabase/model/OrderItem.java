package com.example.tododatabase.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private long orderItemId;
    private long orderId;
    private long productId;
    private int quantity;
    private double price;

    // Constructors, getters, and setters omitted for brevity

    // toString, equals, and hashCode methods
    // Implementation similar to Product model
}
