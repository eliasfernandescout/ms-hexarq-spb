package com.kraftbrains.mshexarqspb.adapter.input.rest.mapper;

import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.DeliveryAddressDTO;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.FoodOrderRequestDTO;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.FoodOrderResponseDTO;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.OrderItemDTO;
import com.kraftbrains.mshexarqspb.domain.core.DeliveryAddress;
import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.core.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class FoodOrderMapper {

    // Converte de objeto de domínio para DTO de resposta
    public static FoodOrderResponseDTO toResponseDTO(FoodOrder foodOrder) {
        if (foodOrder == null) return null;

        FoodOrderResponseDTO responseDTO = new FoodOrderResponseDTO();
        responseDTO.setOrderId(foodOrder.getOrderId());
        responseDTO.setCustomerName(foodOrder.getCustomerName());
        responseDTO.setRestaurantName(foodOrder.getRestaurantName());
        responseDTO.setStatus(foodOrder.getStatus().name());
        responseDTO.setCreatedAt(foodOrder.getCreatedAt());
        responseDTO.setUpdatedAt(foodOrder.getUpdatedAt());
        responseDTO.setCancellationReason(foodOrder.getCancellationReason());
        responseDTO.setTotalAmount(foodOrder.calculateTotal());
        responseDTO.setTotalItems(foodOrder.getTotalItems());

        // Mapear itens
        if (foodOrder.getItems() != null) {
            List<OrderItemDTO> itemDTOs = foodOrder.getItems().stream()
                    .map(FoodOrderMapper::toItemDTO)
                    .collect(Collectors.toList());
            responseDTO.setItems(itemDTOs);
        }

        // Mapear endereço
        if (foodOrder.getDeliveryAddress() != null) {
            responseDTO.setDeliveryAddress(toAddressDTO(foodOrder.getDeliveryAddress()));
        }

        return responseDTO;
    }

    // Converte de DTO de requisição para objeto de domínio
    public static FoodOrder toDomain(FoodOrderRequestDTO dto) {
        if (dto == null) return null;

        FoodOrder.Builder builder = new FoodOrder.Builder()
                .withOrderId(dto.getOrderId())
                .withCustomerName(dto.getCustomerName())
                .withRestaurantName(dto.getRestaurantName());

        // Adicionar status se fornecido
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                FoodOrder.OrderStatus status = FoodOrder.OrderStatus.valueOf(dto.getStatus());
                builder.withStatus(status);
            } catch (IllegalArgumentException e) {
                // Status inválido, usa o padrão (PENDING)
            }
        }

        // Adicionar itens
        if (dto.getItems() != null) {
            List<OrderItem> items = dto.getItems().stream()
                    .map(FoodOrderMapper::toDomainItem)
                    .collect(Collectors.toList());
            builder.withItems(items);
        }

        // Adicionar endereço
        if (dto.getDeliveryAddress() != null) {
            builder.withDeliveryAddress(toDomainAddress(dto.getDeliveryAddress()));
        }

        return builder.build();
    }

    // Conversores auxiliares para OrderItem
    private static OrderItemDTO toItemDTO(OrderItem item) {
        if (item == null) return null;

        OrderItemDTO dto = new OrderItemDTO();
        dto.setName(item.getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setObservations(item.getObservations());
        return dto;
    }

    private static OrderItem toDomainItem(OrderItemDTO dto) {
        if (dto == null) return null;

        return new OrderItem.Builder()
                .withName(dto.getName())
                .withQuantity(dto.getQuantity())
                .withPrice(dto.getPrice())
                .withObservations(dto.getObservations())
                .build();
    }

    // Conversores auxiliares para DeliveryAddress
    private static DeliveryAddressDTO toAddressDTO(DeliveryAddress address) {
        if (address == null) return null;

        DeliveryAddressDTO dto = new DeliveryAddressDTO();
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setComplement(address.getComplement());
        dto.setNeighborhood(address.getNeighborhood());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setReference(address.getReference());
        return dto;
    }

    private static DeliveryAddress toDomainAddress(DeliveryAddressDTO dto) {
        if (dto == null) return null;

        return new DeliveryAddress.Builder()
                .withStreet(dto.getStreet())
                .withNumber(dto.getNumber())
                .withComplement(dto.getComplement())
                .withNeighborhood(dto.getNeighborhood())
                .withCity(dto.getCity())
                .withState(dto.getState())
                .withZipCode(dto.getZipCode())
                .withReference(dto.getReference())
                .build();
    }
}
