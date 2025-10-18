package com.kraftbrains.mshexarqspb.application.port.output;

import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    FoodOrder save(FoodOrder order);
    Optional<FoodOrder> findById(String orderId);
    List<FoodOrder> findByCustomerId(String customerId);
    List<FoodOrder> findAll();
    void delete(String orderId);
}

