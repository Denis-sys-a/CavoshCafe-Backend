package com.cavosh.api_cafe.modules.productos.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Clave compuesta (producto_id, sucursal_id) de la tabla producto_sucursal_stock.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSucursalStockId implements Serializable {

    private Long productoId;
    private Long sucursalId;
}
