package com.kraftbrains.mshexarqspb.adapter.output.repository;


import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.port.output.OrderRepositoryPort;

import java.util.List;

public class MongoOrderRepository implements OrderRepositoryPort {

    // inject mongo repository

    @Override
    public void saveOrder(FoodOrder order) {
        // TODO: Implement MongoDB save logic here
    }

    @Override
    public String findById(String orderId) {
        return "";
    }

    @Override
    public List<FoodOrder> findAll() {
        return List.of();
    }
}
