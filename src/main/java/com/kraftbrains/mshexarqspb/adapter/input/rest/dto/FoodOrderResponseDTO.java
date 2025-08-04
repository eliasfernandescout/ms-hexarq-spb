package com.kraftbrains.mshexarqspb.adapter.input.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FoodOrderResponseDTO {
    private String orderId;
    private String customerName;
    private String restaurantName;
    private List<OrderItemDTO> items;
    private String status;
    private DeliveryAddressDTO deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String cancellationReason;
    private double totalAmount;
    private int totalItems;
}
