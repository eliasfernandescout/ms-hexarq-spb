package com.kraftbrains.mshexarqspb.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para Endosso
 */
class EndossoTest {

    @Test
    void deveCriarEndossoValido() {
        Endosso endosso = Endosso.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .dataEndosso(LocalDateTime.now())
                .status(StatusEndosso.PENDENTE)
                .build();

        assertNotNull(endosso);
        assertDoesNotThrow(endosso::validar);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroTituloForNulo() {
        Endosso endosso = Endosso.builder()
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .build();

        assertThrows(IllegalArgumentException.class, endosso::validar);
    }

    @Test
    void deveAprovarEndossoPendente() {
        Endosso endosso = Endosso.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .dataEndosso(LocalDateTime.now())
                .status(StatusEndosso.PENDENTE)
                .build();

        endosso.aprovar();
        assertEquals(StatusEndosso.APROVADO, endosso.getStatus());
    }

    @Test
    void naoDeveAprovarEndossoCancelado() {
        Endosso endosso = Endosso.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .dataEndosso(LocalDateTime.now())
                .status(StatusEndosso.CANCELADO)
                .build();

        assertThrows(IllegalStateException.class, endosso::aprovar);
    }

    @Test
    void deveCancelarEndossoPendente() {
        Endosso endosso = Endosso.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .dataEndosso(LocalDateTime.now())
                .status(StatusEndosso.PENDENTE)
                .build();

        endosso.cancelar();
        assertEquals(StatusEndosso.CANCELADO, endosso.getStatus());
    }

    @Test
    void naoDeveCancelarEndossoAprovado() {
        Endosso endosso = Endosso.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .endossante("João Silva")
                .endossatario("Maria Santos")
                .tipoEndosso(TipoEndosso.EM_PRETO)
                .dataEndosso(LocalDateTime.now())
                .status(StatusEndosso.APROVADO)
                .build();

        assertThrows(IllegalStateException.class, endosso::cancelar);
    }
}

