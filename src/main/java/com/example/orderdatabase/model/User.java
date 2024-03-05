package com.example.tododatabase.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id; // Unique identifier for the User
    private String username; // Username of the User
    private String fullName; // Full name of the User
    private String email; // Email address of the User
    private String roleName; // Role name of the User

    // Default constructor
    public User() {}

    // Constructor without ID - for new user registration and fetching user details without needing a password
    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    // Constructor with ID - for fetching from the database without password
    public User(long id, String username, String fullName, String email) {
        this(id, username, fullName, email, null); // Role name is null when not specified
    }

    // Full constructor including roleName - for complete user details including role without password
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

    // toString method for printing User details
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    // equals and hashCode methods for comparing User objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(fullName, user.fullName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(roleName, user.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, fullName, email, roleName);
    }
}
