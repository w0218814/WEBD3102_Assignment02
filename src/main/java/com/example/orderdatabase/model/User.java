package com.example.orderdatabase.model;

import java.io.Serializable;
import java.util.Objects;

// The User class represents a user in the system, with personal and contact information.
public class User implements Serializable {

    // Serial version UID for serialization
    private static final long serialVersionUID = 1L;

    // Unique identifier for the user
    private long id;
    // User's login username
    private String username;
    // User's full name
    private String fullName;
    // User's email address
    private String email;
    // Role ID to define user's role and permissions (not initialized in constructors)
    private int roleId;
    // User's street address
    private String street;
    // City part of the user's address
    private String city;
    // A landmark near the user's location for easier identification
    private String nearbyLandmark;
    // Province or state part of the user's address
    private String province;
    // Postal code of the user's address
    private String postalCode;
    // User's phone number
    private String phoneNumber;

    // Default constructor initializes a new instance of User without setting any properties
    public User() {}

    // Constructor to initialize a User with basic identification and contact information
    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        // RoleId must be explicitly set using its setter method
    }

    // Comprehensive constructor including all user details except for the role ID
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
        // RoleId must be explicitly set using its setter method
    }

    // Getter and setter methods for each property to control access and modifications

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

    // Overridden equals method to compare User objects based on their properties
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

    // Overridden hashCode method based on User's properties
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getFullName(), getEmail(), getRoleId(), getStreet(), getCity(), getNearbyLandmark(), getProvince(), getPostalCode(), getPhoneNumber());
    }

    // Overridden toString method for representing a User object as a String
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
