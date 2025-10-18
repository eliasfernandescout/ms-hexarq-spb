package com.kraftbrains.mshexarqspb.domain.port.out;

import com.kraftbrains.mshexarqspb.domain.model.Endosso;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port de saída para persistência de Endosso
 * Interface do domínio que será implementada pela infraestrutura
 */
public interface EndossoRepositoryPort {

    Endosso save(Endosso endosso);

    Optional<Endosso> findById(UUID id);

    List<Endosso> findByNumeroTitulo(String numeroTitulo);

    List<Endosso> findAll();

    void deleteById(UUID id);

    boolean existsByNumeroTitulo(String numeroTitulo);
}

