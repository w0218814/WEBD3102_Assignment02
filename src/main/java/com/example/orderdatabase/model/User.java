package com.example.orderdatabase.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String fullName;
    private String email;
    private int roleId; // No default value set
    private String street;
    private String city;
    private String nearbyLandmark;
    private String province;
    private String postalCode;
    private String phoneNumber;

    // Default constructor
    public User() {}

    // Constructor for username, fullName, email
    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        // RoleId must be set explicitly elsewhere
    }

    // New constructor that includes all parameters, except for roleId
    public User(long id, String username, String fullName, String email, String street, String city, String nearbyLandmark, String province, String postalCode, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.street = street;
        this.city = city;
        this.nearbyLandmark = nearbyLandmark;
        this.province = province;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        // RoleId must be set explicitly elsewhere
    }

    // Getters and setters for all fields
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getNearbyLandmark() { return nearbyLandmark; }
    public void setNearbyLandmark(String nearbyLandmark) { this.nearbyLandmark = nearbyLandmark; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // equals, hashCode, and toString implementations
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getRoleId() == user.getRoleId() &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getFullName(), user.getFullName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getStreet(), user.getStreet()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getNearbyLandmark(), user.getNearbyLandmark()) &&
                Objects.equals(getProvince(), user.getProvince()) &&
                Objects.equals(getPostalCode(), user.getPostalCode()) &&
                Objects.equals(getPhoneNumber(), user.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getFullName(), getEmail(), getRoleId(), getStreet(), getCity(), getNearbyLandmark(), getProvince(), getPostalCode(), getPhoneNumber());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", roleId=" + roleId +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", nearbyLandmark='" + nearbyLandmark + '\'' +
                ", province='" + province + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
