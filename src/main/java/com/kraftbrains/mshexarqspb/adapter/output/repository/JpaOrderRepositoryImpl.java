package com.kraftbrains.mshexarqspb.adapter.output.repository;

import com.kraftbrains.mshexarqspb.adapter.output.entity.JpaDeliveryAddressEntity;
import com.kraftbrains.mshexarqspb.adapter.output.entity.JpaOrderEntity;
import com.kraftbrains.mshexarqspb.adapter.output.entity.JpaOrderItemEntity;
import com.kraftbrains.mshexarqspb.application.port.output.OrderRepositoryPort;
import com.kraftbrains.mshexarqspb.domain.core.DeliveryAddress;
import com.kraftbrains.mshexarqspb.domain.core.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.core.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class JpaOrderRepositoryImpl implements OrderRepositoryPort {

    @Autowired
    private SpringDataOrderRepository repository;

    @Override
    public void saveOrder(FoodOrder order) {
        System.out.println("--OUTPUT ADAPTER EXECUTED WITH OUTPUT PORT--");
        repository.save(mapToEntity(order));
    }

    @Override
    public FoodOrder findById(String orderId) {
        JpaOrderEntity entity = repository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado com ID: " + orderId));
        System.out.println("--OUTPUT ADAPTER EXECUTED WITH OUTPUT PORT--");
        return mapToDomain(entity);
    }

    @Override
    public List<FoodOrder> findAll() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private JpaOrderEntity mapToEntity(FoodOrder order) {
        if (order == null) return null;

        JpaOrderEntity entity = new JpaOrderEntity();
        entity.setOrderId(order.getOrderId());
        entity.setCustomerName(order.getCustomerName());
        entity.setRestaurantName(order.getRestaurantName());
        entity.setStatus(order.getStatus().name());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        entity.setCancellationReason(order.getCancellationReason());

        // Mapear itens
        List<JpaOrderItemEntity> itemEntities = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                JpaOrderItemEntity itemEntity = new JpaOrderItemEntity();
                itemEntity.setName(item.getName());
                itemEntity.setQuantity(item.getQuantity());
                itemEntity.setPrice(item.getPrice());
                itemEntity.setObservations(item.getObservations());
                itemEntities.add(itemEntity);
            }
        }
        entity.setItems(itemEntities);

        // Mapear endereço
        if (order.getDeliveryAddress() != null) {
            DeliveryAddress address = order.getDeliveryAddress();
            JpaDeliveryAddressEntity addressEntity = new JpaDeliveryAddressEntity();
            addressEntity.setStreet(address.getStreet());
            addressEntity.setNumber(address.getNumber());
            addressEntity.setComplement(address.getComplement());
            addressEntity.setNeighborhood(address.getNeighborhood());
            addressEntity.setCity(address.getCity());
            addressEntity.setState(address.getState());
            addressEntity.setZipCode(address.getZipCode());
            addressEntity.setReference(address.getReference());
            entity.setDeliveryAddress(addressEntity);
        }

        return entity;
    }

    private FoodOrder mapToDomain(JpaOrderEntity entity) {
        if (entity == null) return null;

        // Criar builder para FoodOrder
        FoodOrder.Builder builder = new FoodOrder.Builder()
                .withOrderId(entity.getOrderId())
                .withCustomerName(entity.getCustomerName())
                .withRestaurantName(entity.getRestaurantName());

        // Mapear status
        if (entity.getStatus() != null) {
            try {
                FoodOrder.OrderStatus status = FoodOrder.OrderStatus.valueOf(entity.getStatus());
                builder.withStatus(status);
            } catch (IllegalArgumentException e) {
                // Status inválido, usa o padrão (PENDING)
            }
        }

        // Mapear itens
        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
            List<OrderItem> items = entity.getItems().stream()
                    .map(this::mapItemToDomain)
                    .collect(Collectors.toList());
            builder.withItems(items);
        }

        // Mapear endereço
        if (entity.getDeliveryAddress() != null) {
            builder.withDeliveryAddress(mapAddressToDomain(entity.getDeliveryAddress()));
        }

        return builder.build();
    }

    private OrderItem mapItemToDomain(JpaOrderItemEntity itemEntity) {
        if (itemEntity == null) return null;

        return new OrderItem.Builder()
                .withName(itemEntity.getName())
                .withQuantity(itemEntity.getQuantity())
                .withPrice(itemEntity.getPrice())
                .withObservations(itemEntity.getObservations())
                .build();
    }

    private DeliveryAddress mapAddressToDomain(JpaDeliveryAddressEntity addressEntity) {
        if (addressEntity == null) return null;

        return new DeliveryAddress.Builder()
                .withStreet(addressEntity.getStreet())
                .withNumber(addressEntity.getNumber())
                .withComplement(addressEntity.getComplement())
                .withNeighborhood(addressEntity.getNeighborhood())
                .withCity(addressEntity.getCity())
                .withState(addressEntity.getState())
                .withZipCode(addressEntity.getZipCode())
                .withReference(addressEntity.getReference())
                .build();
    }
}
