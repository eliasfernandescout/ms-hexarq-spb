package com.kraftbrains.mshexarqspb.domain.dto;

import lombok.Data;

@Data
public class FoodOrderDTO {
    private String orderId;
    private String customerName;
    private String restaurantName;
    private String item;
    private String status;

    // Constructors, Getters, Setters
}
