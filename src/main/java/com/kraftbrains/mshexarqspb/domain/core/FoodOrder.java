package com.kraftbrains.mshexarqspb.domain.core;

public class FoodOrder {
    private String orderId;
    private String customerName;
    private String restaurantName;
    private String item;
    private String status;

    public FoodOrder() {}

    public FoodOrder(String orderId, String customerName, String restaurantName, String item, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.restaurantName = restaurantName;
        this.item = item;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
