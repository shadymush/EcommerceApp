package com.example.myecommerce;

public class Items {
    private String productName;

    private String productImage;
    private double price;

    public Items(String productName, String productImage, double price) {
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Items{" +
                "productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", price=" + price +
                '}';
    }
}