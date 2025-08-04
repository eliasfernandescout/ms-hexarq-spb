package com.kraftbrains.mshexarqspb.adapter.output.repository;


import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import com.kraftbrains.mshexarqspb.application.port.output.OrderRepositoryPort;

import java.util.List;
import java.util.NoSuchElementException;

public class MongoOrderRepositoryImpl implements OrderRepositoryPort {

    // inject mongo repository

    @Override
    public void saveOrder(FoodOrder order) {
        // TODO: Implement MongoDB save logic here
    }

    @Override
    public FoodOrder findById(String orderId) {
        // TODO: Implement MongoDB findById logic here
        throw new NoSuchElementException("Pedido não encontrado com ID: " + orderId);
        // Esta é uma implementação temporária que lança uma exceção
        // Em uma implementação real, você buscaria no MongoDB e retornaria o pedido
    }

    @Override
    public List<FoodOrder> findAll() {
        return List.of();
    }
}
