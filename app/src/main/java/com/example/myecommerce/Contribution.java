package com.example.myecommerce;

public class Contribution {
    private String id;
    private String name;
    private String description;
    private String postedBy;
    private String paymentDetails;

    public Contribution() {
        // Default constructor required for calls to DataSnapshot.getValue(Contribution.class)
    }

    public Contribution(String id, String name, String description, String postedBy, String paymentDetails) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.postedBy = postedBy;
        this.paymentDetails = paymentDetails;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }
}
