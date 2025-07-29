package com.kraftbrains.mshexarqspb.domain.port.output;

import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import java.util.List;

public interface OrderRepositoryPort {

    void saveOrder(FoodOrder order);
    String findById(String orderId);
    List<FoodOrder> findAll();
}
