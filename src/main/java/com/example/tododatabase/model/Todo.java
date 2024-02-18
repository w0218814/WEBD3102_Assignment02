package com.example.tododatabase.model;

import java.util.Date;

public class Todo {
    private long id;
    private long userId;
    private String title;
    private String description;
    private Date targetDate;
    public boolean isDone; // isDone is now public

    // Constructors
    public Todo() {}

    public Todo(String title, String description, Date targetDate, boolean isDone) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.isDone = isDone;
    }

    public Todo(long id, String title, String description, Date targetDate, boolean isDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.isDone = isDone;
    }

    public Todo(long id, long userId, String title, String description, Date targetDate, boolean isDone) {
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
