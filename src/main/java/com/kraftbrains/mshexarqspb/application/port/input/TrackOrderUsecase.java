package com.kraftbrains.mshexarqspb.application.port.input;

import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;

public interface TrackOrderUsecase {

    String trackOrder(String orderId);

    FoodOrder getOrderById(String orderId);
}
