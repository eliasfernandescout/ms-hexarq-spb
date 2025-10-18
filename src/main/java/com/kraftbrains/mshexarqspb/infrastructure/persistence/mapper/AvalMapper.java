package com.kraftbrains.mshexarqspb.infrastructure.persistence.mapper;

import com.kraftbrains.mshexarqspb.domain.model.Aval;
import com.kraftbrains.mshexarqspb.infrastructure.persistence.entity.AvalEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Aval (domínio) e AvalEntity (persistência)
 */
@Component
public class AvalMapper {

    public AvalEntity toEntity(Aval aval) {
        if (aval == null) {
            return null;
        }

        return AvalEntity.builder()
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

    public Aval toDomain(AvalEntity entity) {
        if (entity == null) {
            return null;
        }

        return Aval.builder()
                .id(entity.getId())
                .numeroTitulo(entity.getNumeroTitulo())
                .avalista(entity.getAvalista())
                .avalizado(entity.getAvalizado())
                .tipoAval(entity.getTipoAval())
                .valorAval(entity.getValorAval())
                .dataAval(entity.getDataAval())
                .observacoes(entity.getObservacoes())
                .status(entity.getStatus())
                .build();
    }
}

