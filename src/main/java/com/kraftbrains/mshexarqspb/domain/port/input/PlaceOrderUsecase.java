package com.kraftbrains.mshexarqspb.domain.port.input;


import com.kraftbrains.mshexarqspb.domain.dto.FoodOrderDTO;

public interface PlaceOrderUsecase {

    void placeOrder(FoodOrderDTO order);
}
