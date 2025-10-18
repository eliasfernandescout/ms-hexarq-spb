package com.kraftbrains.mshexarqspb.application.port.output;

import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;

public interface NotificationServicePort {
    void sendOrderConfirmation(FoodOrder order);
    void sendOrderStatusUpdate(FoodOrder order);
    void sendOrderCancellation(FoodOrder order);
}

