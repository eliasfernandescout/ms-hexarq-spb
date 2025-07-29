package com.kraftbrains.mshexarqspb.domain.service;


import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.port.input.PlaceOrderUsecase;
import com.kraftbrains.mshexarqspb.domain.port.input.TrackOrderUsecase;
import com.kraftbrains.mshexarqspb.domain.port.output.OrderRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService implements PlaceOrderUsecase, TrackOrderUsecase {

    private final OrderRepositoryPort orderRepositoryPort;

//    public OrderService(OrderRepositoryPort orderRepositoryPort) {
//        this.orderRepositoryPort = orderRepositoryPort;
//    }

    @Override
    public void placeOrder(FoodOrder order) {
        order.setStatus("ORDER PLACED");
        System.out.println("--CORE EXECUTED WITH INPUT PORT--");
        orderRepositoryPort.saveOrder(order);
    }

    @Override
    public String trackOrder(String orderId) {
        System.out.println("--CORE EXECUTED WITH INPUT PORT--");
        return orderRepositoryPort.findById(orderId);
    }
}
