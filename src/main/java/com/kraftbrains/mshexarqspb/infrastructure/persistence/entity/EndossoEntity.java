package com.kraftbrains.mshexarqspb.infrastructure.persistence.entity;

import com.kraftbrains.mshexarqspb.domain.model.StatusEndosso;
import com.kraftbrains.mshexarqspb.domain.model.TipoEndosso;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade JPA para persistência de Endosso
 */
@Entity
@Table(name = "endosso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndossoEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "numero_titulo", nullable = false, length = 100)
    private String numeroTitulo;

    @Column(name = "endossante", nullable = false, length = 200)
    private String endossante;

    @Column(name = "endossatario", nullable = false, length = 200)
    private String endossatario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_endosso", nullable = false)
    private TipoEndosso tipoEndosso;

    @Column(name = "data_endosso", nullable = false)
    private LocalDateTime dataEndosso;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEndosso status;
}

