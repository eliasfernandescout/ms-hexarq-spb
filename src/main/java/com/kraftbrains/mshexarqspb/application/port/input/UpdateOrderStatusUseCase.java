package com.kraftbrains.mshexarqspb.application.port.input;

import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;

public interface UpdateOrderStatusUseCase {
    FoodOrder confirmOrder(String orderId);
    FoodOrder prepareOrder(String orderId);
    FoodOrder readyForDelivery(String orderId);
    FoodOrder deliverOrder(String orderId);
    FoodOrder cancelOrder(String orderId);
}

