package com.kraftbrains.mshexarqspb.domain.port.output;

import com.kraftbrains.mshexarqspb.domain.dto.FoodOrder;

public interface OrderRepositoryPort {

    void saveOrder(FoodOrder order);
    String findById(String orderId);
}
