package com.example.myecommerce;

public class Loan {
    private String id;
    private String email;
    private double amount;
    private String loan_purpose;
    private String status;

    public Loan() {
        // Default constructor required for calls to DataSnapshot.getValue(Loan.class)
    }

    public Loan(String email, double amount, String loan_purpose, String status) {
        this.email = email;
        this.amount = amount;
        this.loan_purpose = loan_purpose;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
