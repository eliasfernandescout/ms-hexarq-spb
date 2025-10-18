package com.kraftbrains.mshexarqspb.infrastructure.web.mapper;

import com.kraftbrains.mshexarqspb.domain.model.Aval;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.AvalRequestDTO;
import com.kraftbrains.mshexarqspb.infrastructure.web.dto.AvalResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper entre DTOs e modelo de domínio Aval
 */
@Component
public class AvalDTOMapper {

    public Aval toDomain(AvalRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Aval.builder()
                .numeroTitulo(dto.getNumeroTitulo())
                .avalista(dto.getAvalista())
                .avalizado(dto.getAvalizado())
                .tipoAval(dto.getTipoAval())
                .valorAval(dto.getValorAval())
                .observacoes(dto.getObservacoes())
                .build();
    }

    public AvalResponseDTO toResponseDTO(Aval aval) {
        if (aval == null) {
            return null;
        }

        return AvalResponseDTO.builder()
                .id(aval.getId())
                .numeroTitulo(aval.getNumeroTitulo())
                .avalista(aval.getAvalista())
                .avalizado(aval.getAvalizado())
                .tipoAval(aval.getTipoAval())
                .valorAval(aval.getValorAval())
                .dataAval(aval.getDataAval())
                .observacoes(aval.getObservacoes())
                .status(aval.getStatus())
                .build();
    }
}

