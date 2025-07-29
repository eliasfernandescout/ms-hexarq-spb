package com.kraftbrains.mshexarqspb.domain.model;

import com.kraftbrains.mshexarqspb.domain.dto.FoodOrderDTO;

public class FoodOrderMapper {
    public static FoodOrderDTO toDTO(FoodOrder foodOrder) {
        if (foodOrder == null) return null;
        FoodOrderDTO dto = new FoodOrderDTO();
        dto.setOrderId(foodOrder.getOrderId());
        dto.setCustomerName(foodOrder.getCustomerName());
        dto.setRestaurantName(foodOrder.getRestaurantName());
        dto.setItem(foodOrder.getItem());
        dto.setStatus(foodOrder.getStatus());
        return dto;
    }

    public static FoodOrder toDomain(FoodOrderDTO dto) {
        if (dto == null) return null;
        return new FoodOrder(
            dto.getOrderId(),
            dto.getCustomerName(),
            dto.getRestaurantName(),
            dto.getItem(),
            dto.getStatus()
        );
    }
}

