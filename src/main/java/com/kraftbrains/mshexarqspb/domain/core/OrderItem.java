package com.kraftbrains.mshexarqspb.domain.core;

import java.util.Objects;

/**
 * Objeto de valor que representa um item do pedido
 */
public class OrderItem {
    private final String name;
    private final int quantity;
    private final double price;
    private final String observations;

    private OrderItem(Builder builder) {
        // Validações de domínio
        if (builder.name == null || builder.name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do item não pode ser vazio");
        }
        if (builder.quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (builder.price <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        this.name = builder.name;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.observations = builder.observations;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getObservations() {
        return observations;
    }

    public double getSubtotal() {
        return price * quantity;
    }

    // Value Object deve ser imutável e implementar equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                Double.compare(orderItem.price, price) == 0 &&
                Objects.equals(name, orderItem.name) &&
                Objects.equals(observations, orderItem.observations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, price, observations);
    }

    @Override
    public String toString() {
        return quantity + "x " + name + " - R$" + price +
               (observations != null && !observations.isEmpty() ? " (" + observations + ")" : "");
    }

    // Builder para construção imutável
    public static class Builder {
        private String name;
        private int quantity;
        private double price;
        private String observations;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder withObservations(String observations) {
            this.observations = observations;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
