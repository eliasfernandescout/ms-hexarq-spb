package com.kraftbrains.mshexarqspb.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kraftbrains.mshexarqspb.domain.model.StatusEndosso;
import com.kraftbrains.mshexarqspb.domain.model.TipoEndosso;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta para Endosso
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndossoResponseDTO {

    private UUID id;
    private String numeroTitulo;
    private String endossante;
    private String endossatario;
    private TipoEndosso tipoEndosso;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataEndosso;

    private String observacoes;
    private StatusEndosso status;
}

