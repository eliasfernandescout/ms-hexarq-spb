package com.kraftbrains.mshexarqspb.adapter.input.rest;

import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.CreateOrderRequest;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.OrderResponse;
import com.kraftbrains.mshexarqspb.adapter.input.rest.mapper.OrderRestMapper;
import com.kraftbrains.mshexarqspb.application.port.input.CreateOrderUseCase;
import com.kraftbrains.mshexarqspb.application.port.input.TrackOrderUseCase;
import com.kraftbrains.mshexarqspb.application.port.input.UpdateOrderStatusUseCase;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final TrackOrderUseCase trackOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final OrderRestMapper mapper;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        FoodOrder order = mapper.toDomain(request);
        FoodOrder createdOrder = createOrderUseCase.createOrder(order);
        OrderResponse response = mapper.toResponse(createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        FoodOrder order = trackOrderUseCase.getOrderById(orderId);
        OrderResponse response = mapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        FoodOrder order = updateOrderStatusUseCase.confirmOrder(orderId);
        OrderResponse response = mapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/prepare")
    public ResponseEntity<OrderResponse> prepareOrder(@PathVariable String orderId) {
        FoodOrder order = updateOrderStatusUseCase.prepareOrder(orderId);
        OrderResponse response = mapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/ready")
    public ResponseEntity<OrderResponse> readyForDelivery(@PathVariable String orderId) {
        FoodOrder order = updateOrderStatusUseCase.readyForDelivery(orderId);
        OrderResponse response = mapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable String orderId) {
        FoodOrder order = updateOrderStatusUseCase.deliverOrder(orderId);
        OrderResponse response = mapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        FoodOrder order = updateOrderStatusUseCase.cancelOrder(orderId);
        OrderResponse response = mapper.toResponse(order);
        return ResponseEntity.ok(response);
    }
}
