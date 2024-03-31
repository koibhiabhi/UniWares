package com.example.uniwares.Models;

public class OrderModel {
    private String productTitle;
    private String price;
    private String sellerName;
    private String imageUrl;
    private String userUid;

    public OrderModel() {
        // Default constructor required for Firebase
    }

    public OrderModel(String productTitle, String price, String sellerName, String imageUrl, String userUid) {
        this.productTitle = productTitle;
        this.price = price;
        this.sellerName = sellerName;
        this.imageUrl = imageUrl;
        this.userUid = userUid;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }


}