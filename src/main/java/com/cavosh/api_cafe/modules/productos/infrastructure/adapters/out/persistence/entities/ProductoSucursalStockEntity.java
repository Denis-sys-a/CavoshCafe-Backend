package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities;

import com.cavosh.api_cafe.modules.productos.domain.model.ProductoSucursalStockId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Disponibilidad de un producto en una sucursal puntual.
 * No usa @ManyToOne hacia ProductEntity/SucursalEntity porque, a la fecha,
 * esas entidades JPA de este proyecto todavía no están implementadas
 * (son clases vacías); se referencian por ID plano para no acoplar esta
 * mejora a piezas que aún no existen.
 */
@Entity
@Table(name = "producto_sucursal_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoSucursalStockEntity {

    @EmbeddedId
    private ProductoSucursalStockId id;

    @Column(name = "disponible", nullable = false)
    @Builder.Default
    private boolean disponible = true;
}
