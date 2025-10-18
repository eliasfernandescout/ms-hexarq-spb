package com.kraftbrains.mshexarqspb.application.port.input;

import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;

public interface CreateOrderUseCase {
    FoodOrder createOrder(FoodOrder order);
}

