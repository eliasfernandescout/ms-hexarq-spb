package com.kraftbrains.mshexarqspb.adapter.input.rest;

import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.FoodOrderRequestDTO;
import com.kraftbrains.mshexarqspb.adapter.input.rest.mapper.FoodOrderMapper;
import com.kraftbrains.mshexarqspb.domain.port.input.PlaceOrderUsecase;
import com.kraftbrains.mshexarqspb.domain.port.input.TrackOrderUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PlaceOrderUsecase placeOrderUsecase;
    private final TrackOrderUsecase trackOrderUsecase;

    public OrderController(PlaceOrderUsecase placeOrderUsecase, TrackOrderUsecase trackOrderUsecase) {
        this.placeOrderUsecase = placeOrderUsecase;
        this.trackOrderUsecase = trackOrderUsecase;
    }
    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody FoodOrderRequestDTO order) {
        placeOrderUsecase.placeOrder(FoodOrderMapper.toDomain(order));
        System.out.println("--INPUT ADAPTER EXECUTED--");
        return ResponseEntity.ok("Order placed");

    }



    @GetMapping("/track/{orderId}")
    public ResponseEntity<String> trackOrder(@PathVariable String orderId) {
        System.out.println("--INPUT ADAPTER EXECUTED--");
        return ResponseEntity.ok("Status: " + trackOrderUsecase.trackOrder(orderId));
    }
}
