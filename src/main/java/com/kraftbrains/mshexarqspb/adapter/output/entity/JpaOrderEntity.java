package com.kraftbrains.mshexarqspb.adapter.output.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class JpaOrderEntity {

    @Id
    private String orderId;

    private String customerName;
    private String restaurantName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String cancellationReason;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<JpaOrderItemEntity> items = new ArrayList<>();

    @Embedded
    private JpaDeliveryAddressEntity deliveryAddress;
}
