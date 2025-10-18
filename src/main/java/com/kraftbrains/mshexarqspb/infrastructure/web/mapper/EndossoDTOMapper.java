package com.kraftbrains.mshexarqspb.infrastructure.web.mapper;

import com.kraftbrains.mshexarqspb.domain.model.Endosso;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.EndossoRequestDTO;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.EndossoResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper entre DTOs e modelo de domínio Endosso
 */
@Component
public class EndossoDTOMapper {

    public Endosso toDomain(EndossoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Endosso.builder()
                .numeroTitulo(dto.getNumeroTitulo())
                .endossante(dto.getEndossante())
                .endossatario(dto.getEndossatario())
                .tipoEndosso(dto.getTipoEndosso())
                .observacoes(dto.getObservacoes())
                .build();
    }

    public EndossoResponseDTO toResponseDTO(Endosso endosso) {
        if (endosso == null) {
            return null;
        }

        return EndossoResponseDTO.builder()
                .id(endosso.getId())
                .numeroTitulo(endosso.getNumeroTitulo())
                .endossante(endosso.getEndossante())
                .endossatario(endosso.getEndossatario())
                .tipoEndosso(endosso.getTipoEndosso())
                .dataEndosso(endosso.getDataEndosso())
                .observacoes(endosso.getObservacoes())
                .status(endosso.getStatus())
                .build();
    }
}

