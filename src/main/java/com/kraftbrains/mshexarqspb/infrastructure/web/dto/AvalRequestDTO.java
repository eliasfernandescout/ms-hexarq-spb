package com.kraftbrains.mshexarqspb.infrastructure.web.dto;

import com.kraftbrains.mshexarqspb.domain.model.TipoAval;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO de requisição para criação de Aval
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvalRequestDTO {

    private String numeroTitulo;
    private String avalista;
    private String avalizado;
    private TipoAval tipoAval;
    private BigDecimal valorAval;
    private String observacoes;
}

