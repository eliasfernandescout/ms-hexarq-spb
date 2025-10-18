package com.kraftbrains.mshexarqspb.application.port.input;

import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;

public interface TrackOrderUseCase {
    FoodOrder getOrderById(String orderId);
}

