package com.kraftbrains.mshexarqspb.adapter.output.persistence;

import com.kraftbrains.mshexarqspb.adapter.output.persistence.entity.OrderEntity;
import com.kraftbrains.mshexarqspb.adapter.output.persistence.mapper.OrderPersistenceMapper;
import com.kraftbrains.mshexarqspb.adapter.output.persistence.repository.JpaOrderRepository;
import com.kraftbrains.mshexarqspb.application.port.output.OrderRepositoryPort;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public FoodOrder save(FoodOrder order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<FoodOrder> findById(String orderId) {
        return jpaRepository.findById(orderId)
                .map(mapper::toDomain);
    }

    @Override
    public List<FoodOrder> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<FoodOrder> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String orderId) {
        jpaRepository.deleteById(orderId);
    }
}
