package com.kraftbrains.mshexarqspb.infrastructure.web.dto;

import com.kraftbrains.mshexarqspb.domain.model.TipoEndosso;
import lombok.*;

/**
 * DTO de requisição para criação de Endosso
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndossoRequestDTO {

    private String numeroTitulo;
    private String endossante;
    private String endossatario;
    private TipoEndosso tipoEndosso;
    private String observacoes;
}

