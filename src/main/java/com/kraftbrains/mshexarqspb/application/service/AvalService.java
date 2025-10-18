package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.domain.model.Aval;
import com.kraftbrains.mshexarqspb.domain.model.StatusAval;
import com.kraftbrains.mshexarqspb.domain.port.in.AvalUseCasePort;
import com.kraftbrains.mshexarqspb.domain.port.out.AvalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Serviço de aplicação para Aval
 * Implementa os casos de uso e orquestra as operações de domínio
 */
@Service
@RequiredArgsConstructor
public class AvalService implements AvalUseCasePort {

    private final AvalRepositoryPort avalRepository;

    @Override
    @Transactional
    public Aval criarAval(Aval aval) {
        aval.validar();

        Aval novoAval = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo(aval.getNumeroTitulo())
                .avalista(aval.getAvalista())
                .avalizado(aval.getAvalizado())
                .tipoAval(aval.getTipoAval())
                .valorAval(aval.getValorAval())
                .dataAval(LocalDateTime.now())
                .observacoes(aval.getObservacoes())
                .status(StatusAval.PENDENTE)
                .build();

        return avalRepository.save(novoAval);
    }

    @Override
    public Aval buscarPorId(UUID id) {
        return avalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aval não encontrado com ID: " + id));
    }

    @Override
    public List<Aval> buscarPorNumeroTitulo(String numeroTitulo) {
        return avalRepository.findByNumeroTitulo(numeroTitulo);
    }

    @Override
    public List<Aval> listarTodos() {
        return avalRepository.findAll();
    }

    @Override
    @Transactional
    public Aval aprovarAval(UUID id) {
        Aval aval = buscarPorId(id);
        aval.aprovar();
        return avalRepository.save(aval);
    }

    @Override
    @Transactional
    public Aval executarAval(UUID id) {
        Aval aval = buscarPorId(id);
        aval.executar();
        return avalRepository.save(aval);
    }

    @Override
    @Transactional
    public Aval cancelarAval(UUID id) {
        Aval aval = buscarPorId(id);
        aval.cancelar();
        return avalRepository.save(aval);
    }

    @Override
    @Transactional
    public void deletarAval(UUID id) {
        if (!avalRepository.findById(id).isPresent()) {
            throw new RuntimeException("Aval não encontrado com ID: " + id);
        }
        avalRepository.deleteById(id);
    }
}

