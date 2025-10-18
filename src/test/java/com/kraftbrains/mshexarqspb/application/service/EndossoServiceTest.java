package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.domain.model.*;
import com.kraftbrains.mshexarqspb.domain.port.out.EndossoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para EndossoService
 */
@ExtendWith(MockitoExtension.class)
class EndossoServiceTest {

    @Mock
    private EndossoRepositoryPort endossoRepository;

    @InjectMocks
    private EndossoService endossoService;

    private Endosso endossoValido;

    @BeforeEach
    void setUp() {
        endossoValido = Endosso.builder()
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .observacoes("Teste")
                .build();
    }

    @Test
    void deveCriarEndossoComSucesso() {
        Endosso endossoSalvo = Endosso.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .dataEndosso(LocalDateTime.now())
                .observacoes("Teste")
                .status(StatusEndosso.PENDENTE)
                .build();

        when(endossoRepository.save(any(Endosso.class))).thenReturn(endossoSalvo);

        Endosso resultado = endossoService.criarEndosso(endossoValido);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(StatusEndosso.PENDENTE, resultado.getStatus());
        verify(endossoRepository, times(1)).save(any(Endosso.class));
    }

    @Test
    void deveLancarExcecaoAoCriarEndossoInvalido() {
        Endosso endossoInvalido = Endosso.builder().build();

        assertThrows(IllegalArgumentException.class, () -> {
            endossoService.criarEndosso(endossoInvalido);
        });

        verify(endossoRepository, never()).save(any(Endosso.class));
    }

    @Test
    void deveBuscarEndossoPorId() {
        UUID id = UUID.randomUUID();
        Endosso endosso = Endosso.builder()
                .id(id)
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .status(StatusEndosso.PENDENTE)
                .build();

        when(endossoRepository.findById(id)).thenReturn(Optional.of(endosso));

        Endosso resultado = endossoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(endossoRepository, times(1)).findById(id);
    }

    @Test
    void deveAprovarEndosso() {
        UUID id = UUID.randomUUID();
        Endosso endosso = Endosso.builder()
                .id(id)
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .status(StatusEndosso.PENDENTE)
                .build();

        when(endossoRepository.findById(id)).thenReturn(Optional.of(endosso));
        when(endossoRepository.save(any(Endosso.class))).thenReturn(endosso);

        Endosso resultado = endossoService.aprovarEndosso(id);

        assertNotNull(resultado);
        assertEquals(StatusEndosso.APROVADO, resultado.getStatus());
        verify(endossoRepository, times(1)).save(any(Endosso.class));
    }
}

