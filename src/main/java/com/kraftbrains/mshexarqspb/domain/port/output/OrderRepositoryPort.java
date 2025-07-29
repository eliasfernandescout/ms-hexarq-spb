package com.kraftbrains.mshexarqspb.domain.port.output;

import com.kraftbrains.mshexarqspb.domain.dto.FoodOrderDTO;

public interface OrderRepositoryPort {

    void saveOrder(FoodOrderDTO order);
    String findById(String orderId);
}
