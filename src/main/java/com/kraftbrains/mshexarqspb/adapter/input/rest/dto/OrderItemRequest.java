package com.kraftbrains.mshexarqspb.adapter.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String notes;
}
