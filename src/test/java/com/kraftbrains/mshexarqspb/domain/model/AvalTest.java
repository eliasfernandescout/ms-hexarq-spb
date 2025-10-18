package com.kraftbrains.mshexarqspb.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para Aval
 */
class AvalTest {

    @Test
    void deveCriarAvalValido() {
        Aval aval = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .dataAval(LocalDateTime.now())
                .status(StatusAval.PENDENTE)
                .build();

        assertNotNull(aval);
        assertDoesNotThrow(aval::validar);
    }

    @Test
    void deveLancarExcecaoQuandoValorAvalForZero() {
        Aval aval = Aval.builder()
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(BigDecimal.ZERO)
                .build();

        assertThrows(IllegalArgumentException.class, aval::validar);
    }

    @Test
    void deveAprovarAvalPendente() {
        Aval aval = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .dataAval(LocalDateTime.now())
                .status(StatusAval.PENDENTE)
                .build();

        aval.aprovar();
        assertEquals(StatusAval.APROVADO, aval.getStatus());
    }

    @Test
    void naoDeveAprovarAvalCancelado() {
        Aval aval = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .dataAval(LocalDateTime.now())
                .status(StatusAval.CANCELADO)
                .build();

        assertThrows(IllegalStateException.class, aval::aprovar);
    }

    @Test
    void deveExecutarAvalAprovado() {
        Aval aval = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .dataAval(LocalDateTime.now())
                .status(StatusAval.APROVADO)
                .build();

        aval.executar();
        assertEquals(StatusAval.EXECUTADO, aval.getStatus());
    }

    @Test
    void naoDeveExecutarAvalPendente() {
        Aval aval = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .dataAval(LocalDateTime.now())
                .status(StatusAval.PENDENTE)
                .build();

        assertThrows(IllegalStateException.class, aval::executar);
    }

    @Test
    void naoDeveCancelarAvalExecutado() {
        Aval aval = Aval.builder()
                .id(UUID.randomUUID())
                .numeroTitulo("TIT-001")
                .avalista("Pedro Costa")
                .avalizado("João Silva")
                .tipoAval(TipoAval.TOTAL)
                .valorAval(new BigDecimal("10000.00"))
                .dataAval(LocalDateTime.now())
                .status(StatusAval.EXECUTADO)
                .build();

        assertThrows(IllegalStateException.class, aval::cancelar);
    }
}

