package com.example.parkease.object;

public class Notification {
    String notificationID;
    String userID;
    String message;
    String time;

    public Notification(String notificationID, String userID, String message, String time) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.message = message;
        this.time = time;
    }

    public Notification() {
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
