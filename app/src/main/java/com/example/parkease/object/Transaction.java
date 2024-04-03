package com.example.parkease.object;

public class Transaction {
    private String transactionID;
    private String transactionType;
    private double transactionAmount;
    private String transactionTime;

    private String userID;

    public Transaction() {
    }

    public Transaction(String transactionID, String transactionType, double transactionAmount, String transactionTime, String userID) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.userID = userID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
