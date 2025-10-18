package com.kraftbrains.mshexarqspb.infrastructure.persistence.adapter;

import com.kraftbrains.mshexarqspb.domain.model.Aval;
import com.kraftbrains.mshexarqspb.domain.port.out.AvalRepositoryPort;
import com.kraftbrains.mshexarqspb.infrastructure.persistence.mapper.AvalMapper;
import com.kraftbrains.mshexarqspb.infrastructure.persistence.repository.AvalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa o port de repositório do domínio
 * usando JPA para persistência
 */
@Component
@RequiredArgsConstructor
public class AvalRepositoryAdapter implements AvalRepositoryPort {

    private final AvalJpaRepository jpaRepository;
    private final AvalMapper mapper;

    @Override
    public Aval save(Aval aval) {
        var entity = mapper.toEntity(aval);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Aval> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Aval> findByNumeroTitulo(String numeroTitulo) {
        return jpaRepository.findByNumeroTitulo(numeroTitulo)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Aval> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNumeroTitulo(String numeroTitulo) {
        return jpaRepository.existsByNumeroTitulo(numeroTitulo);
    }
}

