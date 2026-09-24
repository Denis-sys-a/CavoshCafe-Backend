package com.cavosh.api_cafe.modules.promociones.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promocion {

    private Long id;
    private String codigo;
    private String descripcion;
    private BigDecimal descuento;
    private TipoDescuento tipoDescuento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean activo;

    /**
     * Límite global de usos del cupón. NULL = sin límite.
     */
    private Integer usoMaximo;

    /**
     * Cantidad de veces que el cupón ya fue consumido (global).
     */
    @Builder.Default
    private int usosActuales = 0;

    /**
     * Límite de veces que un mismo usuario puede usar este cupón. NULL = sin límite por usuario.
     */
    private Integer usoMaximoPorUsuario;

    public boolean esVálida() {
        LocalDateTime ahora = LocalDateTime.now();
        return activo && ahora.isAfter(fechaInicio) && ahora.isBefore(fechaFin);
    }

    /**
     * Indica si el cupón todavía tiene cupo global disponible.
     */
    public boolean tieneCupoGlobalDisponible() {
        return usoMaximo == null || usosActuales < usoMaximo;
    }

    /**
     * Indica si, dado cuántas veces ya lo usó un usuario puntual, aún puede volver a usarlo.
     */
    public boolean puedeUsarUsuario(int usosPreviosDelUsuario) {
        return usoMaximoPorUsuario == null || usosPreviosDelUsuario < usoMaximoPorUsuario;
    }
}
