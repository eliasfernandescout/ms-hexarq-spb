package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.application.port.input.UpdateOrderStatusUseCase;
import com.kraftbrains.mshexarqspb.application.port.output.NotificationServicePort;
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
public class UpdateOrderStatusUseCaseImpl implements UpdateOrderStatusUseCase {

    private final OrderRepositoryPort orderRepository;
    private final OrderEventPublisherPort eventPublisher;
    private final NotificationServicePort notificationService;

    @Override
    public FoodOrder confirmOrder(String orderId) {
        FoodOrder order = findOrder(orderId);
        order.confirm();
        FoodOrder savedOrder = orderRepository.save(order);

        publishEvent(orderId, OrderEventType.ORDER_CONFIRMED, "Order confirmed");
        notificationService.sendOrderConfirmation(savedOrder);

        return savedOrder;
    }

    @Override
    public FoodOrder prepareOrder(String orderId) {
        FoodOrder order = findOrder(orderId);
        order.prepare();
        FoodOrder savedOrder = orderRepository.save(order);

        publishEvent(orderId, OrderEventType.ORDER_PREPARING, "Order is being prepared");
        notificationService.sendOrderStatusUpdate(savedOrder);

        return savedOrder;
    }

    @Override
    public FoodOrder readyForDelivery(String orderId) {
        FoodOrder order = findOrder(orderId);
        order.readyForDelivery();
        FoodOrder savedOrder = orderRepository.save(order);

        publishEvent(orderId, OrderEventType.ORDER_READY_FOR_DELIVERY, "Order ready for delivery");
        notificationService.sendOrderStatusUpdate(savedOrder);

        return savedOrder;
    }

    @Override
    public FoodOrder deliverOrder(String orderId) {
        FoodOrder order = findOrder(orderId);
        order.deliver();
        FoodOrder savedOrder = orderRepository.save(order);

        publishEvent(orderId, OrderEventType.ORDER_DELIVERED, "Order delivered");
        notificationService.sendOrderStatusUpdate(savedOrder);

        return savedOrder;
    }

    @Override
    public FoodOrder cancelOrder(String orderId) {
        FoodOrder order = findOrder(orderId);
        order.cancel();
        FoodOrder savedOrder = orderRepository.save(order);

        publishEvent(orderId, OrderEventType.ORDER_CANCELLED, "Order cancelled");
        notificationService.sendOrderCancellation(savedOrder);

        return savedOrder;
    }

    private FoodOrder findOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    private void publishEvent(String orderId, OrderEventType eventType, String message) {
        OrderEvent event = new OrderEvent(
            UUID.randomUUID().toString(),
            orderId,
            eventType,
            message
        );
        eventPublisher.publishOrderEvent(event);
    }
}
