package com.kraftbrains.mshexarqspb.adapter.input.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FoodOrderRequestDTO {
    private String orderId;
    private String customerName;
    private String restaurantName;
    private List<OrderItemDTO> items;
    private String status;
    private DeliveryAddressDTO deliveryAddress;
}
