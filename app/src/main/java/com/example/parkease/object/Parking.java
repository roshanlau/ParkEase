package com.example.parkease.object;

import java.util.ArrayList;
import java.util.List;

public class Parking {
    String ParkingSpaceID;
    String currentUser;
    boolean status;
    double longitude, latitude;
    double price;
    String startTime, endTime;


    List<String> parkingHistory = new ArrayList<>();

    public Parking() {
    }

    public Parking(String parkingSpaceID, String currentUser, double longitude, double latitude, double price, boolean status, String startTime, String endTime) {
        ParkingSpaceID = parkingSpaceID;
        this.currentUser = currentUser;
        this.status = status;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public List<String> getParkingHistory() {
        return parkingHistory;
    }

    public void setParkingHistory(List<String> parkingHistory) {
        this.parkingHistory = parkingHistory;
    }

    public void addParkingHistory(String history){
        this.parkingHistory.add(history);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
