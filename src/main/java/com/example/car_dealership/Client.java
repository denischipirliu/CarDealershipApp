package com.example.car_dealership;

import java.util.prefs.Preferences;

public class Client extends User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;


    public Client(String firstName, String lastName, String email, String phoneNumber, String address) {
        super(Preferences.userRoot().get("username", null), Preferences.userRoot().get("password", null), Preferences.userRoot().get("role", null));
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
