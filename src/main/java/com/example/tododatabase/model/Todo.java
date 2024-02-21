package com.example.tododatabase.model;

import java.util.Date;

public class Todo {
    private long id; // Unique identifier for the Todo
    private long userId; // Identifier for the user associated with the Todo
    private String title; // Title of the Todo
    private String description; // Description of the Todo
    private Date targetDate; // Date by which the Todo should be completed
    public boolean isDone; // Indicates whether the Todo is done or not, now public

    // Constructors

    // Default constructor
    public Todo() {}

    // Constructor with parameters
    public Todo(String title, String description, Date targetDate, boolean isDone) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.isDone = isDone;
    }

    // Constructor with parameters including ID
    public Todo(long id, String title, String description, Date targetDate, boolean isDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.isDone = isDone;
    }

    // Constructor with parameters including ID and user ID
    public Todo(long id, long userId, String title, String description, Date targetDate, boolean isDone) {
        // Reusing constructor with parameters including ID
        this(id, title, description, targetDate, isDone);
        this.userId = userId;
    }

    // Getters and Setters (excluding isDone since it's now public)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }
}
