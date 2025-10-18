package com.kraftbrains.mshexarqspb.adapter.output.persistence.mapper;

import com.kraftbrains.mshexarqspb.adapter.output.persistence.entity.OrderEntity;
import com.kraftbrains.mshexarqspb.adapter.output.persistence.entity.OrderItemEntity;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.model.OrderItem;
import com.kraftbrains.mshexarqspb.domain.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderPersistenceMapper {

    public OrderEntity toEntity(FoodOrder order) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(order.getOrderId());
        entity.setCustomerId(order.getCustomerId());
        entity.setCustomerName(order.getCustomerName());
        entity.setStatus(order.getStatus().name());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        entity.setDeliveryAddress(order.getDeliveryAddress());

        if (order.getItems() != null) {
            order.getItems().forEach(item -> {
                OrderItemEntity itemEntity = toItemEntity(item);
                itemEntity.setOrder(entity);
                entity.getItems().add(itemEntity);
            });
        }

        return entity;
    }

    public OrderItemEntity toItemEntity(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setItemId(item.getItemId());
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setQuantity(item.getQuantity());
        entity.setPrice(item.getPrice());
        entity.setNotes(item.getNotes());
        return entity;
    }

    public FoodOrder toDomain(OrderEntity entity) {
        FoodOrder order = new FoodOrder();
        order.setOrderId(entity.getOrderId());
        order.setCustomerId(entity.getCustomerId());
        order.setCustomerName(entity.getCustomerName());
        order.setStatus(OrderStatus.valueOf(entity.getStatus()));
        order.setTotalAmount(entity.getTotalAmount());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUpdatedAt(entity.getUpdatedAt());
        order.setDeliveryAddress(entity.getDeliveryAddress());

        if (entity.getItems() != null) {
            order.setItems(
                entity.getItems().stream()
                    .map(this::toItemDomain)
                    .collect(Collectors.toList())
            );
        }

        return order;
    }

    public OrderItem toItemDomain(OrderItemEntity entity) {
        OrderItem item = new OrderItem();
        item.setItemId(entity.getItemId());
        item.setProductId(entity.getProductId());
        item.setProductName(entity.getProductName());
        item.setQuantity(entity.getQuantity());
        item.setPrice(entity.getPrice());
        item.setNotes(entity.getNotes());
        return item;
    }
}

