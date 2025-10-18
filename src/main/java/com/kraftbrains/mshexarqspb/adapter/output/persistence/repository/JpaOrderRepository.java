package com.kraftbrains.mshexarqspb.adapter.output.persistence.repository;

import com.kraftbrains.mshexarqspb.adapter.output.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByCustomerId(String customerId);
}

