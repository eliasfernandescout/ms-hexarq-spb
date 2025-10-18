package com.kraftbrains.mshexarqspb.domain.port.out;

import com.kraftbrains.mshexarqspb.domain.model.Aval;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port de saída para persistência de Aval
 * Interface do domínio que será implementada pela infraestrutura
 */
public interface AvalRepositoryPort {

    Aval save(Aval aval);

    Optional<Aval> findById(UUID id);

    List<Aval> findByNumeroTitulo(String numeroTitulo);

    List<Aval> findAll();

    void deleteById(UUID id);

    boolean existsByNumeroTitulo(String numeroTitulo);
}

