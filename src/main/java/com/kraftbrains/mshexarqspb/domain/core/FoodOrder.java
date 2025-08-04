package com.kraftbrains.mshexarqspb.domain.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FoodOrder {
    private final String orderId;
    private final String customerName;
    private final String restaurantName;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String cancellationReason;
    private DeliveryAddress deliveryAddress;

    private FoodOrder(Builder builder) {
        // Validações de domínio
        if (builder.customerName == null || builder.customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio");
        }
        if (builder.restaurantName == null || builder.restaurantName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante não pode ser vazio");
        }
        if (builder.items == null || builder.items.isEmpty()) {
            throw new IllegalArgumentException("Pedido deve ter pelo menos um item");
        }
        if (builder.deliveryAddress == null) {
            throw new IllegalArgumentException("Endereço de entrega é obrigatório");
        }

        this.orderId = builder.orderId != null ? builder.orderId : UUID.randomUUID().toString();
        this.customerName = builder.customerName;
        this.restaurantName = builder.restaurantName;
        this.items = new ArrayList<>(builder.items);
        this.status = builder.status != null ? builder.status : OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.deliveryAddress = builder.deliveryAddress;
    }

    // Comportamentos de domínio
    public FoodOrder confirm() {
        validateStatusTransition(OrderStatus.CONFIRMED);
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public FoodOrder prepare() {
        validateStatusTransition(OrderStatus.PREPARING);
        this.status = OrderStatus.PREPARING;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public FoodOrder readyForDelivery() {
        validateStatusTransition(OrderStatus.READY_FOR_DELIVERY);
        this.status = OrderStatus.READY_FOR_DELIVERY;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public FoodOrder startDelivery() {
        validateStatusTransition(OrderStatus.OUT_FOR_DELIVERY);
        this.status = OrderStatus.OUT_FOR_DELIVERY;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public FoodOrder complete() {
        validateStatusTransition(OrderStatus.DELIVERED);
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public FoodOrder cancel(String reason) {
        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Não é possível cancelar um pedido já entregue");
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("É necessário fornecer um motivo para o cancelamento");
        }

        this.status = OrderStatus.CANCELLED;
        this.cancellationReason = reason;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    // Método auxiliar para validar transições de estado
    private void validateStatusTransition(OrderStatus newStatus) {
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Não é possível alterar um pedido cancelado");
        }

        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Não é possível alterar um pedido já entregue");
        }

        // Validar sequência lógica de estados
        if (newStatus.ordinal() <= this.status.ordinal() &&
            this.status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                "Transição de status inválida: " + this.status + " -> " + newStatus);
        }
    }

    // Métodos de domínio
    public double calculateTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public boolean canBeCancelled() {
        return this.status != OrderStatus.DELIVERED && this.status != OrderStatus.CANCELLED;
    }

    public int getTotalItems() {
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public boolean isInProgress() {
        return this.status != OrderStatus.DELIVERED && this.status != OrderStatus.CANCELLED;
    }

    // Getters - Sem setters para garantir imutabilidade
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    // Builder para construção imutável
    public static class Builder {
        private String orderId;
        private String customerName;
        private String restaurantName;
        private List<OrderItem> items = new ArrayList<>();
        private OrderStatus status;
        private DeliveryAddress deliveryAddress;

        public Builder withOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder withCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder withRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
            return this;
        }

        public Builder withItem(OrderItem item) {
            this.items.add(item);
            return this;
        }

        public Builder withItems(List<OrderItem> items) {
            this.items = new ArrayList<>(items);
            return this;
        }

        public Builder withStatus(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder withDeliveryAddress(DeliveryAddress address) {
            this.deliveryAddress = address;
            return this;
        }

        public FoodOrder build() {
            return new FoodOrder(this);
        }
    }

    // Enum para representar o status do pedido
    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PREPARING,
        READY_FOR_DELIVERY,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED
    }
}
