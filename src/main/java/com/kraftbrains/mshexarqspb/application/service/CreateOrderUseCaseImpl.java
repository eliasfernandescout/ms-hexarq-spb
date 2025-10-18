package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.application.port.input.CreateOrderUseCase;
import com.kraftbrains.mshexarqspb.application.port.output.OrderEventPublisherPort;
import com.kraftbrains.mshexarqspb.application.port.output.OrderRepositoryPort;
import com.kraftbrains.mshexarqspb.domain.event.OrderEvent;
import com.kraftbrains.mshexarqspb.domain.event.OrderEventType;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final OrderEventPublisherPort eventPublisher;

    @Override
    public FoodOrder createOrder(FoodOrder order) {
        // Validação de negócio
        validateOrder(order);

        // Salvar pedido
        FoodOrder savedOrder = orderRepository.save(order);

        // Publicar evento
        OrderEvent event = new OrderEvent(
            UUID.randomUUID().toString(),
            savedOrder.getOrderId(),
            OrderEventType.ORDER_CREATED,
            "Order created for customer: " + savedOrder.getCustomerName()
        );
        eventPublisher.publishOrderEvent(event);

        return savedOrder;
    }

    private void validateOrder(FoodOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getCustomerId() == null || order.getCustomerId().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
    }
}
