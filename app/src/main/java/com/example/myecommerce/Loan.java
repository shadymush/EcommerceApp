package com.example.myecommerce;

public class Loan {

    private String status;
    private String id;
    private String email;
    private double amount;
    private String loan_purpose;
    private String userId;

    private String dueDate;

    public Loan() {
        // Default constructor required for calls to DataSnapshot.getValue(Loan.class)
    }

    public Loan(String id, String email, double amount, String loan_purpose, String userId, String status, String dueDate) {
        this.id = id;
        this.email = email;
        this.amount = amount;
        this.loan_purpose = loan_purpose;
        this.userId = userId;
        this.status = status;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getLoan_purpose() {
        return loan_purpose;
    }

    public void setLoan_purpose(String loan_purpose) {
        this.loan_purpose = loan_purpose;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
