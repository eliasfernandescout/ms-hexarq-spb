package com.kraftbrains.mshexarqspb.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String eventId;
    private String orderId;
    private OrderEventType eventType;
    private LocalDateTime occurredAt;
    private String payload;

    public OrderEvent(String eventId, String orderId, OrderEventType eventType, String payload) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = LocalDateTime.now();
    }
}
