package com.kraftbrains.mshexarqspb.infrastructure.persistence.entity;

import com.kraftbrains.mshexarqspb.domain.model.StatusAval;
import com.kraftbrains.mshexarqspb.domain.model.TipoAval;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade JPA para persistência de Aval
 */
@Entity
@Table(name = "aval")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvalEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "numero_titulo", nullable = false, length = 100)
    private String numeroTitulo;

    @Column(name = "avalista", nullable = false, length = 200)
    private String avalista;

    @Column(name = "avalizado", nullable = false, length = 200)
    private String avalizado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_aval", nullable = false)
    private TipoAval tipoAval;

    @Column(name = "valor_aval", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAval;

    @Column(name = "data_aval", nullable = false)
    private LocalDateTime dataAval;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAval status;
}

