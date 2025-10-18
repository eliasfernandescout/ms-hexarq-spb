package com.kraftbrains.mshexarqspb.domain.port.in;

import com.kraftbrains.mshexarqspb.domain.model.Aval;

import java.util.List;
import java.util.UUID;

/**
 * Port de entrada para casos de uso de Aval
 * Define as operações disponíveis para a camada de aplicação
 */
public interface AvalUseCasePort {

    Aval criarAval(Aval aval);

    Aval buscarPorId(UUID id);

    List<Aval> buscarPorNumeroTitulo(String numeroTitulo);

    List<Aval> listarTodos();

    Aval aprovarAval(UUID id);

    Aval executarAval(UUID id);

    Aval cancelarAval(UUID id);

    void deletarAval(UUID id);
}

