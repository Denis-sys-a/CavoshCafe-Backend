package com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.entities;

import com.cavosh.api_cafe.modules.promociones.domain.model.TipoDescuento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "codigos_promocionales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodigoPromocionalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_descuento", nullable = false)
    private TipoDescuento tipoDescuento;

    @Column(name = "valor_descuento", nullable = false)
    private BigDecimal valorDescuento;

    @Column(name = "valido_desde", nullable = false)
    private LocalDateTime validoDesde;

    @Column(name = "valido_hasta", nullable = false)
    private LocalDateTime validoHasta;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private boolean activo = true;

    /**
     * Límite global de usos del cupón. NULL = sin límite.
     */
    @Column(name = "uso_maximo")
    private Integer usoMaximo;

    @Column(name = "usos_actuales", nullable = false)
    @Builder.Default
    private int usosActuales = 0;

    /**
     * Límite de veces que un mismo usuario puede usar este cupón. NULL = sin límite por usuario.
     */
    @Column(name = "uso_maximo_por_usuario")
    @Builder.Default
    private Integer usoMaximoPorUsuario = 1;
}
