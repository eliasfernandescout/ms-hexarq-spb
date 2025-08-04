package com.kraftbrains.mshexarqspb.adapter.input.rest;

import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.FoodOrderRequestDTO;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.FoodOrderResponseDTO;
import com.kraftbrains.mshexarqspb.adapter.input.rest.mapper.FoodOrderMapper;
import com.kraftbrains.mshexarqspb.application.port.input.PlaceOrderUsecase;
import com.kraftbrains.mshexarqspb.application.port.input.TrackOrderUsecase;
import com.kraftbrains.mshexarqspb.application.service.OrderService;
import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PlaceOrderUsecase placeOrderUsecase;
    private final TrackOrderUsecase trackOrderUsecase;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<FoodOrderResponseDTO> createOrder(@RequestBody FoodOrderRequestDTO requestDTO) {
        try {
            System.out.println("--INPUT ADAPTER EXECUTED--");

            // Converter DTO para objeto de domínio
            FoodOrder orderDomain = FoodOrderMapper.toDomain(requestDTO);

            // Chamar caso de uso para criar o pedido
            FoodOrder createdOrder = placeOrderUsecase.placeOrder(orderDomain);

            // Converter resultado de volta para DTO
            FoodOrderResponseDTO responseDTO = FoodOrderMapper.toResponseDTO(createdOrder);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar pedido: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FoodOrderResponseDTO> getOrderById(@PathVariable String orderId) {
        try {
            FoodOrder order = trackOrderUsecase.getOrderById(orderId);
            return ResponseEntity.ok(FoodOrderMapper.toResponseDTO(order));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<FoodOrderResponseDTO>> getAllOrders() {
        List<FoodOrderResponseDTO> orders = orderService.getAllOrders().stream()
                .map(FoodOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/track")
    public ResponseEntity<String> trackOrder(@PathVariable String orderId) {
        try {
            String status = trackOrderUsecase.trackOrder(orderId);
            return ResponseEntity.ok("Status do pedido: " + status);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<FoodOrderResponseDTO> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam String status) {
        try {
            FoodOrder updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(FoodOrderMapper.toResponseDTO(updatedOrder));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<FoodOrderResponseDTO> cancelOrder(
            @PathVariable String orderId,
            @RequestParam String reason) {
        try {
            FoodOrder cancelledOrder = orderService.cancelOrder(orderId, reason);
            return ResponseEntity.ok(FoodOrderMapper.toResponseDTO(cancelledOrder));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
