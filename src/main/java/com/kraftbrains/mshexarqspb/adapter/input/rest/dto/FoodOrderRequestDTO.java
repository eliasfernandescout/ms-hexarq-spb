package com.kraftbrains.mshexarqspb.adapter.input.rest.dto;

import lombok.Data;

@Data
public class FoodOrderRequestDTO {
    private String orderId;
    private String customerName;
    private String restaurantName;
    private String item;
    private String status;

    // Constructors, Getters, Setters
}
