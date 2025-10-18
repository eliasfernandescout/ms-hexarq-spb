package com.kraftbrains.mshexarqspb.adapter.input.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraftbrains.mshexarqspb.domain.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventKafkaListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.topic.order-events:order-events}",
        groupId = "${kafka.consumer.group-id:order-service-group}"
    )
    public void handleOrderEvent(String message) {
        try {
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
            log.info("Received order event: {} for order: {}",
                       event.getEventType(), event.getOrderId());

            // Aqui você pode processar o evento conforme necessário
            processOrderEvent(event);

        } catch (Exception e) {
            log.error("Error processing order event", e);
        }
    }

    private void processOrderEvent(OrderEvent event) {
        // Lógica de processamento do evento
        // Por exemplo: atualizar cache, enviar notificações, etc.
        switch (event.getEventType()) {
            case ORDER_CREATED:
                log.info("Processing ORDER_CREATED event");
                break;
            case ORDER_CONFIRMED:
                log.info("Processing ORDER_CONFIRMED event");
                break;
            case ORDER_DELIVERED:
                log.info("Processing ORDER_DELIVERED event");
                break;
            case ORDER_CANCELLED:
                log.info("Processing ORDER_CANCELLED event");
                break;
            default:
                log.info("Processing event: {}", event.getEventType());
        }
    }
}
