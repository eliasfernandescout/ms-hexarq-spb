package com.kraftbrains.mshexarqspb.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FoodOrder {
    private String orderId;
    private String customerId;
    private String customerName;
    private List<OrderItem> items;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deliveryAddress;

    public FoodOrder() {
        this.orderId = UUID.randomUUID().toString();
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }

    public FoodOrder(String customerId, String customerName, String deliveryAddress) {
        this();
        this.customerId = customerId;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
    }

    // Métodos de negócio (Modelo rico)
    public void addItem(OrderItem item) {
        validateItem(item);
        this.items.add(item);
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(String itemId) {
        this.items.removeIf(item -> item.getItemId().equals(itemId));
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        validateCanConfirm();
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void prepare() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order must be confirmed before preparing");
        }
        this.status = OrderStatus.PREPARING;
        this.updatedAt = LocalDateTime.now();
    }

    public void readyForDelivery() {
        if (this.status != OrderStatus.PREPARING) {
            throw new IllegalStateException("Order must be preparing before ready for delivery");
        }
        this.status = OrderStatus.READY_FOR_DELIVERY;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (this.status != OrderStatus.READY_FOR_DELIVERY) {
            throw new IllegalStateException("Order must be ready before delivering");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel a delivered order");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero");
        }
        if (item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Item price must be greater than zero");
        }
    }

    private void validateCanConfirm() {
        if (this.items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm order without items");
        }
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    public boolean isConfirmed() {
        return this.status == OrderStatus.CONFIRMED;
    }

    public boolean isDelivered() {
        return this.status == OrderStatus.DELIVERED;
    }

    public boolean isCancelled() {
        return this.status == OrderStatus.CANCELLED;
    }
}
