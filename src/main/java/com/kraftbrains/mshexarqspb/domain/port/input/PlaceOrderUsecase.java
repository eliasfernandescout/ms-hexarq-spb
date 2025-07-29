package com.kraftbrains.mshexarqspb.domain.port.input;


import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;

public interface PlaceOrderUsecase {

    void placeOrder(FoodOrder order);
}
