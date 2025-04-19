package com.urban_food.entity;

public class Admin {
    private String adminID;
    private String adminEmail;
    private String adminUsername;
    private String adminAddress;
    private String adminContact;
    private String adminPassword;

    public Admin() {
    }

    public Admin(String adminID, String adminEmail, String adminUsername, String adminAddress, String adminContact, String adminPassword) {
        this.adminID = adminID;
        this.adminEmail = adminEmail;
        this.adminUsername = adminUsername;
        this.adminAddress = adminAddress;
        this.adminContact = adminContact;
        this.adminPassword = adminPassword;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    public String getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(String adminContact) {
        this.adminContact = adminContact;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
