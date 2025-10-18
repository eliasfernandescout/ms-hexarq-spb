package com.kraftbrains.mshexarqspb.adapter.input.rest.mapper;

import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.CreateOrderRequest;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.OrderItemRequest;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.OrderItemResponse;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.OrderResponse;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderRestMapper {

    public FoodOrder toDomain(CreateOrderRequest request) {
        FoodOrder order = new FoodOrder(
            request.getCustomerId(),
            request.getCustomerName(),
            request.getDeliveryAddress()
        );

        if (request.getItems() != null) {
            request.getItems().forEach(itemRequest -> {
                OrderItem item = toOrderItem(itemRequest);
                order.addItem(item);
            });
        }

        return order;
    }

    public OrderItem toOrderItem(OrderItemRequest request) {
        OrderItem item = new OrderItem(
            request.getProductId(),
            request.getProductName(),
            request.getQuantity(),
            request.getPrice()
        );
        item.setNotes(request.getNotes());
        return item;
    }

    public OrderResponse toResponse(FoodOrder order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setCustomerId(order.getCustomerId());
        response.setCustomerName(order.getCustomerName());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setDeliveryAddress(order.getDeliveryAddress());

        if (order.getItems() != null) {
            response.setItems(
                order.getItems().stream()
                    .map(this::toItemResponse)
                    .collect(Collectors.toList())
            );
        }

        return response;
    }

    public OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setItemId(item.getItemId());
        response.setProductId(item.getProductId());
        response.setProductName(item.getProductName());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSubtotal(item.getSubtotal());
        response.setNotes(item.getNotes());
        return response;
    }
}

