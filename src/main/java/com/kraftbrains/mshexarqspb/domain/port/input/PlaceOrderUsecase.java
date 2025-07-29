package com.kraftbrains.mshexarqspb.domain.port.input;


import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;

public interface PlaceOrderUsecase {

    void placeOrder(FoodOrder order);
}
