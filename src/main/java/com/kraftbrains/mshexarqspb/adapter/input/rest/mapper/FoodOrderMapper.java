package com.kraftbrains.mshexarqspb.adapter.input.rest.mapper;

import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.FoodOrderRequestDTO;
import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;

public class FoodOrderMapper {
    public static FoodOrderRequestDTO toDTO(FoodOrder foodOrder) {
        if (foodOrder == null) return null;
        FoodOrderRequestDTO dto = new FoodOrderRequestDTO();
        dto.setOrderId(foodOrder.getOrderId());
        dto.setCustomerName(foodOrder.getCustomerName());
        dto.setRestaurantName(foodOrder.getRestaurantName());
        dto.setItem(foodOrder.getItem());
        dto.setStatus(foodOrder.getStatus());
        return dto;
    }

    public static FoodOrder toDomain(FoodOrderRequestDTO dto) {
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

