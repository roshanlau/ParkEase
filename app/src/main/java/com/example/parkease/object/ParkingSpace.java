package com.example.parkease.object;

public class ParkingSpace {
    String ParkingSpaceID;
    String QRCode;
    String currentUser;
    Boolean status;
    String location;
    String price;

    public ParkingSpace() {
    }

    public ParkingSpace(String parkingSpaceID, String QRCode, Boolean status, String location, String price, String currentUser) {
        ParkingSpaceID = parkingSpaceID;
        this.QRCode = QRCode;
        this.status = status;
        this.location = location;
        this.price = price;
        this.currentUser = currentUser;
    }

    public String getParkingSpaceID() {
        return ParkingSpaceID;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setParkingSpaceID(String parkingSpaceID) {
        ParkingSpaceID = parkingSpaceID;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
