package com.cavosh.api_cafe.modules.carrito.domain.model;

import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {

    private Long id;
    private Producto producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    /**
     * IDs de producto_opcion_valores elegidos para este ítem (talla, tipo de leche, etc.).
     * Dos ítems del mismo producto se consideran "el mismo" (y por lo tanto se
     * consolidan) solo si este conjunto coincide exactamente.
     */
    @Builder.Default
    private Set<Long> opcionesValoresIds = new HashSet<>();
}