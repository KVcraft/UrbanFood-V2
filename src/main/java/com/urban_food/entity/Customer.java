package com.urban_food.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Customer")
public class Customer {
    @Id
    private String customerID;
    private String customerUsername;
    private String customerEmail;
    private String customerAddress;
    private String customerContact;
    private String customerPassword;

    // Constructors
    public Customer() {
    }

    public Customer(String customerID, String customerUsername, String customerEmail,
                    String customerAddress, String customerContact, String customerPassword) {
        this.customerID = customerID;
        this.customerUsername = customerUsername;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.customerContact = customerContact;
        this.customerPassword = customerPassword;
    }

    // Getters and Setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", customerUsername='" + customerUsername + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerContact='" + customerContact + '\'' +
                ", customerPassword='" + customerPassword + '\'' +
                '}';
    }
}
