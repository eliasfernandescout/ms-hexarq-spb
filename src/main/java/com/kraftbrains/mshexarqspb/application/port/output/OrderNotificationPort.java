package com.kraftbrains.mshexarqspb.application.port.output;

import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;

public interface OrderNotificationPort {
    void notifyOrderCreated(FoodOrder order);
    void notifyOrderStatusChange(FoodOrder order);
    void notifyOrderCancelled(FoodOrder order);
}
