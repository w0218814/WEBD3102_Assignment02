package com.example.tododatabase.model;

public class User {
    private long id;
    private String username;
    private String fullName;
    private String email;
    private String roleName; // Field for role name

    // Default constructor
    public User() {}

    // Constructor without ID - for new user registration and fetched user details without needing a password
    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    // Constructor with ID - for fetching from the database without password
    public User(long id, String username, String fullName, String email) {
        this(id, username, fullName, email, null); // roleName is null when not specified
    }

    // Full constructor including roleName - for complete user details without password
    public User(long id, String username, String fullName, String email, String roleName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roleName = roleName;
    }

    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
