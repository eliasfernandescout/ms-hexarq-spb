package com.kraftbrains.mshexarqspb.domain.event;

public enum OrderEventType {
    ORDER_CREATED,
    ORDER_CONFIRMED,
    ORDER_PREPARING,
    ORDER_READY_FOR_DELIVERY,
    ORDER_DELIVERED,
    ORDER_CANCELLED
}

