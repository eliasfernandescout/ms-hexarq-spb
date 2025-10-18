package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.application.port.input.TrackOrderUseCase;
import com.kraftbrains.mshexarqspb.application.port.output.OrderRepositoryPort;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackOrderUseCaseImpl implements TrackOrderUseCase {

    private final OrderRepositoryPort orderRepository;

    @Override
    public FoodOrder getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
}
