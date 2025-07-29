package com.kraftbrains.mshexarqspb.adapter.output.repository;


import com.kraftbrains.mshexarqspb.adapter.output.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {
}
