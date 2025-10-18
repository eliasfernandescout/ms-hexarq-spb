package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.domain.model.Endosso;
import com.kraftbrains.mshexarqspb.domain.model.StatusEndosso;
import com.kraftbrains.mshexarqspb.domain.port.in.EndossoUseCasePort;
import com.kraftbrains.mshexarqspb.domain.port.out.EndossoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Serviço de aplicação para Endosso
 * Implementa os casos de uso e orquestra as operações de domínio
 */
@Service
@RequiredArgsConstructor
public class EndossoService implements EndossoUseCasePort {

    private final EndossoRepositoryPort endossoRepository;

    @Override
    @Transactional
    public Endosso criarEndosso(Endosso endosso) {
        endosso.validar();

        Endosso novoEndosso = Endosso.builder()
                .id(UUID.randomUUID())
                .numeroTitulo(endosso.getNumeroTitulo())
                .endossante(endosso.getEndossante())
                .endossatario(endosso.getEndossatario())
                .tipoEndosso(endosso.getTipoEndosso())
                .dataEndosso(LocalDateTime.now())
                .observacoes(endosso.getObservacoes())
                .status(StatusEndosso.PENDENTE)
                .build();

        return endossoRepository.save(novoEndosso);
    }

    @Override
    public Endosso buscarPorId(UUID id) {
        return endossoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endosso não encontrado com ID: " + id));
    }

    @Override
    public List<Endosso> buscarPorNumeroTitulo(String numeroTitulo) {
        return endossoRepository.findByNumeroTitulo(numeroTitulo);
    }

    @Override
    public List<Endosso> listarTodos() {
        return endossoRepository.findAll();
    }

    @Override
    @Transactional
    public Endosso aprovarEndosso(UUID id) {
        Endosso endosso = buscarPorId(id);
        endosso.aprovar();
        return endossoRepository.save(endosso);
    }

    @Override
    @Transactional
    public Endosso cancelarEndosso(UUID id) {
        Endosso endosso = buscarPorId(id);
        endosso.cancelar();
        return endossoRepository.save(endosso);
    }

    @Override
    @Transactional
    public void deletarEndosso(UUID id) {
        if (!endossoRepository.findById(id).isPresent()) {
            throw new RuntimeException("Endosso não encontrado com ID: " + id);
        }
        endossoRepository.deleteById(id);
    }
}

