package com.kraftbrains.mshexarqspb.adapter.output.notification;

import com.kraftbrains.mshexarqspb.application.port.output.OrderNotificationPort;
import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationAdapter implements OrderNotificationPort {

    @Override
    public void notifyOrderCreated(FoodOrder order) {
        // Simulação de notificação - em um cenário real, aqui poderíamos enviar:
        // - Email para o cliente
        // - Notificação push para o restaurante
        // - Mensagem para um tópico Kafka
        System.out.println("NOTIFICAÇÃO: Novo pedido criado - ID: " + order.getOrderId() +
                          " para o cliente " + order.getCustomerName() +
                          " no restaurante " + order.getRestaurantName());
    }

    @Override
    public void notifyOrderStatusChange(FoodOrder order) {
        // Simulação de notificação de mudança de status
        System.out.println("NOTIFICAÇÃO: Pedido " + order.getOrderId() +
                          " atualizado para status " + order.getStatus() +
                          " para o cliente " + order.getCustomerName());
    }

    @Override
    public void notifyOrderCancelled(FoodOrder order) {
        // Simulação de notificação de cancelamento
        System.out.println("NOTIFICAÇÃO: Pedido " + order.getOrderId() +
                          " CANCELADO. Motivo: " + order.getCancellationReason() +
                          ". Cliente: " + order.getCustomerName());
    }
}
