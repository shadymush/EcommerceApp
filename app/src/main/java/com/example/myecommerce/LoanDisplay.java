package com.example.myecommerce;

public class LoanDisplay {
    private double amount;
    private String dueDate;
    private String status;

    // Default constructor for Firestore
    public LoanDisplay() {
    }

    public LoanDisplay(double amount, String dueDate, String status) {
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
