package com.kraftbrains.mshexarqspb.infrastructure.persistence.repository;

import com.kraftbrains.mshexarqspb.infrastructure.persistence.entity.EndossoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositório JPA para EndossoEntity
 */
@Repository
public interface EndossoJpaRepository extends JpaRepository<EndossoEntity, UUID> {

    List<EndossoEntity> findByNumeroTitulo(String numeroTitulo);

    boolean existsByNumeroTitulo(String numeroTitulo);
}

