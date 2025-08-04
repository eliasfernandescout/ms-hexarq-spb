package com.kraftbrains.mshexarqspb.adapter.input.rest.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String name;
    private int quantity;
    private double price;
    private String observations;
}
