package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Tabla {@code producto_opcion_valores}: un valor elegible de un grupo de
 * opciones (Small/Medium/Large, Leche de avena...). Su ID es el que el carrito
 * recibe en {@code opcionesValoresIds}.
 */
@Entity
@Table(name = "producto_opcion_valores")
@Getter
@Setter
@NoArgsConstructor
public class ProductoOpcionValorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_opcion_id", nullable = false)
    private ProductoOpcionEntity opcion;

    @Column(name = "nombre_valor", nullable = false, length = 50)
    private String nombreValor;

    @Column(name = "modificador_precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal modificadorPrecio = BigDecimal.ZERO;

    @Column(name = "es_predeterminado", nullable = false)
    private boolean predeterminado = false;
}
