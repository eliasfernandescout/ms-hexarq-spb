package com.kraftbrains.mshexarqspb.application.port.input;


import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;

public interface PlaceOrderUsecase {

    FoodOrder placeOrder(FoodOrder order);
}
