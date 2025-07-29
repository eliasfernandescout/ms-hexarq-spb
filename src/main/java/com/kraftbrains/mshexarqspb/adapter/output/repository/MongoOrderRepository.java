package com.kraftbrains.mshexarqspb.adapter.output.repository;


import com.kraftbrains.mshexarqspb.domain.dto.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.port.output.OrderRepositoryPort;

public class MongoOrderRepository implements OrderRepositoryPort {

    // inject mongo repository

    @Override
    public void saveOrder(FoodOrder order) {

    }

    @Override
    public String findById(String orderId) {
        return "";
    }
}
