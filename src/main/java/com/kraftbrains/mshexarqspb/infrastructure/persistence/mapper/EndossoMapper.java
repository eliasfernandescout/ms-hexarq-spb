package com.kraftbrains.mshexarqspb.infrastructure.persistence.mapper;

import com.kraftbrains.mshexarqspb.domain.model.Endosso;
import com.kraftbrains.mshexarqspb.infrastructure.persistence.entity.EndossoEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Endosso (domínio) e EndossoEntity (persistência)
 */
@Component
public class EndossoMapper {

    public EndossoEntity toEntity(Endosso endosso) {
        if (endosso == null) {
            return null;
        }

        return EndossoEntity.builder()
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

    public Endosso toDomain(EndossoEntity entity) {
        if (entity == null) {
            return null;
        }

        return Endosso.builder()
                .id(entity.getId())
                .numeroTitulo(entity.getNumeroTitulo())
                .endossante(entity.getEndossante())
                .endossatario(entity.getEndossatario())
                .tipoEndosso(entity.getTipoEndosso())
                .dataEndosso(entity.getDataEndosso())
                .observacoes(entity.getObservacoes())
                .status(entity.getStatus())
                .build();
    }
}

