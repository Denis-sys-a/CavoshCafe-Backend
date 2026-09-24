package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Un valor de personalización elegido dentro de un carrito_item
 * (ej. "Tamaño: Medium", "Leche de avena").
 */
@Entity
@Table(name = "carrito_item_opciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItemOpcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "carrito_item_id", nullable = false)
    private CarritoItemEntity carritoItem;

    @Column(name = "producto_opcion_valor_id", nullable = false)
    private Long productoOpcionValorId;
}
