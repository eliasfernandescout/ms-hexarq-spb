package com.kraftbrains.mshexarqspb.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kraftbrains.mshexarqspb.domain.model.StatusAval;
import com.kraftbrains.mshexarqspb.domain.model.TipoAval;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta para Aval
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvalResponseDTO {

    private UUID id;
    private String numeroTitulo;
    private String avalista;
    private String avalizado;
    private TipoAval tipoAval;
    private BigDecimal valorAval;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataAval;

    private String observacoes;
    private StatusAval status;
}

