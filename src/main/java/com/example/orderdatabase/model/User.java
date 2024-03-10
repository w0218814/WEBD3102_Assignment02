package com.example.orderdatabase.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String password; // Assume that we store password hash
    private String fullName;
    private String email;
    private int roleId; // Assuming the role ID is an integer
    // New fields
    private String street;
    private String city;
    private String nearbyLandmark;
    private String province;
    private String postalCode;
    private String phoneNumber;

    // Default constructor
    public User() {}

    // Constructor with username, fullName, and email only
    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        // The following fields are initialized with default values, update as necessary
        this.street = "";
        this.city = "";
        this.nearbyLandmark = "";
        this.province = "";
        this.postalCode = "";
        this.phoneNumber = "";
        this.roleId = 2; // Default role, you might need to adjust this
    }

    // Full constructor
    public User(long id, String username, String fullName, String email, int roleId, String street, String city, String nearbyLandmark, String province, String postalCode, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roleId = roleId;
        this.street = street;
        this.city = city;
        this.nearbyLandmark = nearbyLandmark;
        this.province = province;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters for all fields, including new ones

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
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

    // equals, hashCode, and toString methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getRoleId() == user.getRoleId() &&
                getUsername().equals(user.getUsername()) &&
                getFullName().equals(user.getFullName()) &&
                getEmail().equals(user.getEmail()) &&
                getStreet().equals(user.getStreet()) &&
                getCity().equals(user.getCity()) &&
                getNearbyLandmark().equals(user.getNearbyLandmark()) &&
                getProvince().equals(user.getProvince()) &&
                getPostalCode().equals(user.getPostalCode()) &&
                getPhoneNumber().equals(user.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getFullName(), getEmail(), getRoleId(), getStreet(), getCity(), getNearbyLandmark(), getProvince(), getPostalCode(), getPhoneNumber());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' + // Remember to handle with care to not expose the password hash
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
