package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.application.port.input.PlaceOrderUsecase;
import com.kraftbrains.mshexarqspb.application.port.input.TrackOrderUsecase;
import com.kraftbrains.mshexarqspb.application.port.output.OrderNotificationPort;
import com.kraftbrains.mshexarqspb.application.port.output.OrderRepositoryPort;
import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService implements PlaceOrderUsecase, TrackOrderUsecase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderNotificationPort orderNotificationPort;

    @Override
    @Transactional
    public FoodOrder placeOrder(FoodOrder order) {
        System.out.println("--CORE EXECUTED WITH INPUT PORT--");

        // Garantir que o pedido tenha um ID
        if (order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            // Em um cenário real, esta responsabilidade estaria no domínio e não no serviço
            // Mas como nosso domínio é imutável, precisamos criar um novo objeto
            FoodOrder newOrder = new FoodOrder.Builder()
                    .withOrderId(UUID.randomUUID().toString())
                    .withCustomerName(order.getCustomerName())
                    .withRestaurantName(order.getRestaurantName())
                    .withItems(order.getItems())
                    .withDeliveryAddress(order.getDeliveryAddress())
                    .build();

            // Confirmar o pedido (muda o status para CONFIRMED)
            newOrder = newOrder.confirm();

            // Salvar o pedido no repositório
            orderRepositoryPort.saveOrder(newOrder);

            // Notificar sobre o novo pedido
            orderNotificationPort.notifyOrderCreated(newOrder);

            return newOrder;
        } else {
            // Confirmar o pedido (muda o status para CONFIRMED)
            order = order.confirm();

            // Salvar o pedido no repositório
            orderRepositoryPort.saveOrder(order);

            // Notificar sobre o novo pedido
            orderNotificationPort.notifyOrderCreated(order);

            return order;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String trackOrder(String orderId) {
        System.out.println("--CORE EXECUTED WITH INPUT PORT--");
        try {
            FoodOrder order = orderRepositoryPort.findById(orderId);
            return order.getStatus().name();
        } catch (Exception e) {
            throw new NoSuchElementException("Pedido não encontrado com ID: " + orderId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FoodOrder getOrderById(String orderId) {
        return orderRepositoryPort.findById(orderId);
    }

    @Transactional(readOnly = true)
    public List<FoodOrder> getAllOrders() {
        return orderRepositoryPort.findAll();
    }

    @Transactional
    public FoodOrder updateOrderStatus(String orderId, String status) {
        FoodOrder order = getOrderById(orderId);

        FoodOrder updatedOrder;
        switch (status.toUpperCase()) {
            case "PREPARING":
                updatedOrder = order.prepare();
                break;
            case "READY_FOR_DELIVERY":
                updatedOrder = order.readyForDelivery();
                break;
            case "OUT_FOR_DELIVERY":
                updatedOrder = order.startDelivery();
                break;
            case "DELIVERED":
                updatedOrder = order.complete();
                break;
            case "CANCELLED":
                updatedOrder = order.cancel("Cancelado pela administração");
                break;
            default:
                throw new IllegalArgumentException("Status inválido: " + status);
        }

        orderRepositoryPort.saveOrder(updatedOrder);
        orderNotificationPort.notifyOrderStatusChange(updatedOrder);
        return updatedOrder;
    }

    @Transactional
    public FoodOrder cancelOrder(String orderId, String reason) {
        FoodOrder order = getOrderById(orderId);

        if (!order.canBeCancelled()) {
            throw new IllegalStateException("Este pedido não pode ser cancelado");
        }

        FoodOrder cancelledOrder = order.cancel(reason);
        orderRepositoryPort.saveOrder(cancelledOrder);
        orderNotificationPort.notifyOrderCancelled(cancelledOrder);
        return cancelledOrder;
    }
}
