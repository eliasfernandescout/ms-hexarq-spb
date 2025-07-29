package com.kraftbrains.mshexarqspb.adapter.output.repository;


import com.kraftbrains.mshexarqspb.domain.dto.FoodOrderDTO;
import com.kraftbrains.mshexarqspb.domain.port.output.OrderRepositoryPort;

public class MongoOrderRepository implements OrderRepositoryPort {

    // inject mongo repository

    @Override
    public void saveOrder(FoodOrderDTO order) {

    }

    @Override
    public String findById(String orderId) {
        return "";
    }
}
