package com.kraftbrains.mshexarqspb.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de domínio representando um Aval
 * Ato cambial de garantia pessoal do pagamento do título
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Aval {

    private UUID id;
    private String numeroTitulo;
    private String avalista; // Quem dá o aval
    private String avalizado; // Quem recebe o aval (devedor garantido)
    private TipoAval tipoAval;
    private BigDecimal valorAval;
    private LocalDateTime dataAval;
    private String observacoes;
    private StatusAval status;

    public void validar() {
        if (numeroTitulo == null || numeroTitulo.isBlank()) {
            throw new IllegalArgumentException("Número do título é obrigatório");
        }
        if (avalista == null || avalista.isBlank()) {
            throw new IllegalArgumentException("Avalista é obrigatório");
        }
        if (avalizado == null || avalizado.isBlank()) {
            throw new IllegalArgumentException("Avalizado é obrigatório");
        }
        if (tipoAval == null) {
            throw new IllegalArgumentException("Tipo de aval é obrigatório");
        }
        if (valorAval == null || valorAval.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do aval deve ser maior que zero");
        }
    }

    public void aprovar() {
        if (this.status == StatusAval.CANCELADO) {
            throw new IllegalStateException("Não é possível aprovar um aval cancelado");
        }
        if (this.status == StatusAval.EXECUTADO) {
            throw new IllegalStateException("Não é possível aprovar um aval já executado");
        }
        this.status = StatusAval.APROVADO;
    }

    public void executar() {
        if (this.status != StatusAval.APROVADO) {
            throw new IllegalStateException("Apenas avais aprovados podem ser executados");
        }
        this.status = StatusAval.EXECUTADO;
    }

    public void cancelar() {
        if (this.status == StatusAval.EXECUTADO) {
            throw new IllegalStateException("Não é possível cancelar um aval executado");
        }
        this.status = StatusAval.CANCELADO;
    }
}
