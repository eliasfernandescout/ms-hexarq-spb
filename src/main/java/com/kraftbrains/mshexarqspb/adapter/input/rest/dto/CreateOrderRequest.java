package com.kraftbrains.mshexarqspb.adapter.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String customerId;
    private String customerName;
    private String deliveryAddress;
    private List<OrderItemRequest> items;
}
