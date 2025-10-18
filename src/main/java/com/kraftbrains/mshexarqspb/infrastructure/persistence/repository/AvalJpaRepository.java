package com.kraftbrains.mshexarqspb.infrastructure.persistence.repository;

import com.kraftbrains.mshexarqspb.infrastructure.persistence.entity.AvalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositório JPA para AvalEntity
 */
@Repository
public interface AvalJpaRepository extends JpaRepository<AvalEntity, UUID> {

    List<AvalEntity> findByNumeroTitulo(String numeroTitulo);

    boolean existsByNumeroTitulo(String numeroTitulo);
}

