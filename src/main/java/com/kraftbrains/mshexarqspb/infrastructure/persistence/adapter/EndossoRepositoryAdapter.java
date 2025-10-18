package com.kraftbrains.mshexarqspb.infrastructure.persistence.adapter;

import com.kraftbrains.mshexarqspb.domain.model.Endosso;
import com.kraftbrains.mshexarqspb.domain.port.out.EndossoRepositoryPort;
import com.kraftbrains.mshexarqspb.infrastructure.persistence.mapper.EndossoMapper;
import com.kraftbrains.mshexarqspb.infrastructure.persistence.repository.EndossoJpaRepository;
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
public class EndossoRepositoryAdapter implements EndossoRepositoryPort {

    private final EndossoJpaRepository jpaRepository;
    private final EndossoMapper mapper;

    @Override
    public Endosso save(Endosso endosso) {
        var entity = mapper.toEntity(endosso);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Endosso> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Endosso> findByNumeroTitulo(String numeroTitulo) {
        return jpaRepository.findByNumeroTitulo(numeroTitulo)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Endosso> findAll() {
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

