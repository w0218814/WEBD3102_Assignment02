package com.example.tododatabase.model;

public class User {
    private long id;
    private String username;
    private String password; // Ensure this is always a hashed password
    private String fullName;
    private String email;
    private String roleName; // Field for role name

    // Default constructor
    public User() {}

    // Constructor without ID and roleName - for new user registration
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password; // Ensure password is hashed if setting directly
        this.fullName = fullName;
        this.email = email;
    }

    // Constructor with ID - for fetching from the database without roleName
    public User(long id, String username, String password, String fullName, String email) {
        this(id, username, password, fullName, email, null); // roleName is null when not specified
    }

    // Full constructor including roleName - for complete user details
    public User(long id, String username, String password, String fullName, String email, String roleName) {
        this.id = id;
        this.username = username;
        this.password = password; // Ensure password is hashed
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

    // Consider not providing a getter for the password in real applications to avoid exposing hashed passwords unnecessarily
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Ensure this is a hashed password
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
