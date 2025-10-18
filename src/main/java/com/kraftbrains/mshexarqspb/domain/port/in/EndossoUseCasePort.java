package com.kraftbrains.mshexarqspb.domain.port.in;

import com.kraftbrains.mshexarqspb.domain.model.Endosso;

import java.util.List;
import java.util.UUID;

/**
 * Port de entrada para casos de uso de Endosso
 * Define as operações disponíveis para a camada de aplicação
 */
public interface EndossoUseCasePort {

    Endosso criarEndosso(Endosso endosso);

    Endosso buscarPorId(UUID id);

    List<Endosso> buscarPorNumeroTitulo(String numeroTitulo);

    List<Endosso> listarTodos();

    Endosso aprovarEndosso(UUID id);

    Endosso cancelarEndosso(UUID id);

    void deletarEndosso(UUID id);
}

