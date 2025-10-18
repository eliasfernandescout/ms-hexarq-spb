package com.kraftbrains.mshexarqspb.adapter.output.service;

import com.kraftbrains.mshexarqspb.application.port.output.NotificationServicePort;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceAdapter implements NotificationServicePort {

    @Override
    public void sendOrderConfirmation(FoodOrder order) {
        log.info("Sending order confirmation to customer {} for order {}",
                order.getCustomerName(), order.getOrderId());

        // Implementação real: integração com serviço de email/SMS
        // Exemplo: emailService.send(order.getCustomerEmail(), "Order Confirmed", emailBody);

        log.info("Order confirmation sent successfully");
    }

    @Override
    public void sendOrderStatusUpdate(FoodOrder order) {
        log.info("Sending status update to customer {} for order {}. Status: {}",
                order.getCustomerName(), order.getOrderId(), order.getStatus());

        // Implementação real: integração com serviço de notificação
        // Exemplo: pushNotificationService.send(order.getCustomerId(), message);

        log.info("Status update sent successfully");
    }

    @Override
    public void sendOrderCancellation(FoodOrder order) {
        log.info("Sending cancellation notification to customer {} for order {}",
                order.getCustomerName(), order.getOrderId());

        // Implementação real: integração com serviço de notificação

        log.info("Cancellation notification sent successfully");
    }
}
