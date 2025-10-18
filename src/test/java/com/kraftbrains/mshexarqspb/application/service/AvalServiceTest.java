package com.kraftbrains.mshexarqspb.application.service;

import com.kraftbrains.mshexarqspb.domain.model.*;
import com.kraftbrains.mshexarqspb.domain.port.out.AvalRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AvalService
 */
@ExtendWith(MockitoExtension.class)
class AvalServiceTest {

    @Mock
    private AvalRepositoryPort avalRepository;

    @InjectMocks
    private AvalService avalService;

    private Aval avalValido;

    @BeforeEach
    void setUp() {
        avalValido = Aval.builder()
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .observacoes("Teste")
                .build();
    }

    @Test
    void deveCriarAvalComSucesso() {
        Aval avalSalvo = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .dataAval(LocalDateTime.now())
                .observacoes("Teste")
                .status(StatusAval.PENDENTE)
                .build();

        when(avalRepository.save(any(Aval.class))).thenReturn(avalSalvo);

        Aval resultado = avalService.criarAval(avalValido);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(StatusAval.PENDENTE, resultado.getStatus());
        verify(avalRepository, times(1)).save(any(Aval.class));
    }

    @Test
    void deveLancarExcecaoAoCriarAvalInvalido() {
        Aval avalInvalido = Aval.builder().build();

        assertThrows(IllegalArgumentException.class, () -> {
            avalService.criarAval(avalInvalido);
        });

        verify(avalRepository, never()).save(any(Aval.class));
    }

    @Test
    void deveBuscarAvalPorId() {
        UUID id = UUID.randomUUID();
        Aval aval = Aval.builder()
                .id(id)
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .status(StatusAval.PENDENTE)
                .build();

        when(avalRepository.findById(id)).thenReturn(Optional.of(aval));

        Aval resultado = avalService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(avalRepository, times(1)).findById(id);
    }

    @Test
    void deveAprovarAval() {
        UUID id = UUID.randomUUID();
        Aval aval = Aval.builder()
                .id(id)
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .status(StatusAval.PENDENTE)
                .build();

        when(avalRepository.findById(id)).thenReturn(Optional.of(aval));
        when(avalRepository.save(any(Aval.class))).thenReturn(aval);

        Aval resultado = avalService.aprovarAval(id);

        assertNotNull(resultado);
        assertEquals(StatusAval.APROVADO, resultado.getStatus());
        verify(avalRepository, times(1)).save(any(Aval.class));
    }

    @Test
    void deveExecutarAvalAprovado() {
        UUID id = UUID.randomUUID();
        Aval aval = Aval.builder()
                .id(id)
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .status(StatusAval.APROVADO)
                .build();

        when(avalRepository.findById(id)).thenReturn(Optional.of(aval));
        when(avalRepository.save(any(Aval.class))).thenReturn(aval);

        Aval resultado = avalService.executarAval(id);

        assertNotNull(resultado);
        assertEquals(StatusAval.EXECUTADO, resultado.getStatus());
        verify(avalRepository, times(1)).save(any(Aval.class));
    }
}

