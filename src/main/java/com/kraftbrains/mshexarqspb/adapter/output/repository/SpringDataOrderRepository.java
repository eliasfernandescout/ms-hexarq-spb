package com.kraftbrains.mshexarqspb.adapter.output.repository;


import com.kraftbrains.mshexarqspb.adapter.output.entity.JpaOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<JpaOrderEntity, String> {
}
