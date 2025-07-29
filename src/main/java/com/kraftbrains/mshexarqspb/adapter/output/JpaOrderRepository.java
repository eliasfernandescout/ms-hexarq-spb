package com.kraftbrains.mshexarqspb.adapter.output;


import com.kraftbrains.mshexarqspb.adapter.output.entity.JpaOrderEntity;
import com.kraftbrains.mshexarqspb.adapter.output.repository.SpringDataOrderRepository;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrderMapper;
import com.kraftbrains.mshexarqspb.domain.port.output.OrderRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaOrderRepository implements OrderRepositoryPort {

    @Autowired
    private SpringDataOrderRepository repository;

    @Override
    public void saveOrder(FoodOrder order) {
        System.out.println("--OUTPUT ADAPTER EXECUTED WITH OUTPUT PORT--");
        repository.save(mapToEntity(order));
    }

    @Override
    public String findById(String orderId) {
        JpaOrderEntity entity = repository.findById(orderId).orElseThrow();
        System.out.println("--OUTPUT ADAPTER EXECUTED WITH OUTPUT PORT--");
        return mapToDomain(entity).getStatus();
    }

    private JpaOrderEntity mapToEntity(FoodOrder order) {
        JpaOrderEntity entity = new JpaOrderEntity();
        entity.setOrderId(order.getOrderId());
        entity.setCustomerName(order.getCustomerName());
        entity.setRestaurantName(order.getRestaurantName());
        entity.setItem(order.getItem());
        entity.setStatus(order.getStatus());
        return entity;
    }

    private FoodOrder mapToDomain(JpaOrderEntity entity) {
        return new FoodOrder(
            entity.getOrderId(),
            entity.getCustomerName(),
            entity.getRestaurantName(),
            entity.getItem(),
            entity.getStatus()
        );
    }
}
