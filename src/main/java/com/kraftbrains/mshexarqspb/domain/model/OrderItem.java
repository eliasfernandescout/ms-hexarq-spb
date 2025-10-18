package com.kraftbrains.mshexarqspb.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderItem {
    private String itemId;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String notes;

    public OrderItem(String productId, String productName, Integer quantity, BigDecimal price) {
        this.itemId = UUID.randomUUID().toString();
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void updateQuantity(Integer newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = newQuantity;
    }
}
