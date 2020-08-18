package com.tapan.grocydelivery.models;

public class DeliveryModel {
    String userImage, userName, userAddress, shopImage, shopName, shopAddress, orderDateTime, mapLocation, orderDate, orderDeliveryStatus, pickStatus;
    String orderNumberId;
    String documentId;

    public DeliveryModel() {
    }

    public DeliveryModel(String userImage, String userName, String userAddress, String shopImage, String shopName, String shopAddress, String orderDateTime, String orderNumberId, String documentId, String mapLocation, String orderDate, String orderDeliveryStatus, String pickStatus) {
        this.userImage = userImage;
        this.userName = userName;
        this.userAddress = userAddress;
        this.shopImage = shopImage;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.orderDateTime = orderDateTime;
        this.orderNumberId = orderNumberId;
        this.documentId = documentId;
        this.mapLocation = mapLocation;
        this.orderDate = orderDate;
        this.orderDeliveryStatus = orderDeliveryStatus;
        this.pickStatus = pickStatus;

    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getShopImage() {
        return shopImage;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getOrderNumberId() {
        return orderNumberId;
    }

    public String getMapLocation() {
        return mapLocation;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderDeliveryStatus() {
        return orderDeliveryStatus;
    }

    public String getPickStatus() {
        return pickStatus;
    }
}
