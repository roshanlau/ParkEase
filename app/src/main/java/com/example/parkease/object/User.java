package com.example.parkease.object;

import com.example.parkease.R;

public class User {
    private String userId;
    private String userName;
    private String userPhoneNumber;
    private String userEmail;
    private String userPassword;
    private String userICNumber;
    private String userDOB;
    private String userBalance;

    public User(String userId, String userName, String userPhoneNumber, String userEmail, String userPassword, String userICNumber, String userDOB, String userBalance) {
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userICNumber = userICNumber;
        this.userDOB = userDOB;
        this.userBalance = userBalance;
    }

    public User() {
    }

    public String getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(String userBalance) {
        this.userBalance = userBalance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserICNumber() {
        return userICNumber;
    }

    public void setUserICNumber(String userICNumber) {
        this.userICNumber = userICNumber;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }
}
