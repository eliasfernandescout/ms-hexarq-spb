package com.kraftbrains.mshexarqspb.application.port.output;

import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import java.util.List;

public interface OrderRepositoryPort {

    void saveOrder(FoodOrder order);
    FoodOrder findById(String orderId);
    List<FoodOrder> findAll();
}
