package com.kraftbrains.mshexarqspb.application.port.output;

import com.kraftbrains.mshexarqspb.domain.event.OrderEvent;

public interface OrderEventPublisherPort {
    void publishOrderEvent(OrderEvent event);
}

