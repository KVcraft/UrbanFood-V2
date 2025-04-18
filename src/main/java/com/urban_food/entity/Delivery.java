package com.urban_food.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Delivery")
public class Delivery {
    @Id
    private String deliveryId;
    private String orderId;
    private String deliveryStatus;
    private String deliveryAddress;
    private java.sql.Date deliveryDate;

    // Constructors
    public Delivery() {}

    public Delivery(String deliveryId, String orderId, String deliveryStatus, String deliveryAddress, java.sql.Date deliveryDate) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
    }

    // Getters and Setters
    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public java.sql.Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(java.sql.Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
