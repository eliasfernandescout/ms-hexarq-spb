package com.kraftbrains.mshexarqspb.adapter.output.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraftbrains.mshexarqspb.application.port.output.OrderEventPublisherPort;
import com.kraftbrains.mshexarqspb.domain.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventPublisher implements OrderEventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.order-events:order-events}")
    private String orderEventsTopic;

    @Override
    public void publishOrderEvent(OrderEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(orderEventsTopic, event.getOrderId(), eventJson);
            log.info("Published event to Kafka: {} for order: {}",
                       event.getEventType(), event.getOrderId());
        } catch (JsonProcessingException e) {
            log.error("Error serializing order event", e);
            throw new RuntimeException("Failed to publish order event", e);
        }
    }
}
