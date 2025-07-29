package com.kraftbrains.mshexarqspb.adapter.input.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.FoodOrderRequestDTO;
import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import com.kraftbrains.mshexarqspb.adapter.input.rest.mapper.FoodOrderMapper;
import com.kraftbrains.mshexarqspb.domain.port.input.PlaceOrderUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaConsumer {

    @Autowired
    private PlaceOrderUsecase placeOrderUseCase;

    //@KafkaListener(topics = "food-order-topic", groupId = "order-group")
    public void consume(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FoodOrderRequestDTO orderDTO = mapper.readValue(message, FoodOrderRequestDTO.class);
        FoodOrder order = FoodOrderMapper.toDomain(orderDTO);
        placeOrderUseCase.placeOrder(order);
        System.out.println("Order placed via Kafka");
    }

}
