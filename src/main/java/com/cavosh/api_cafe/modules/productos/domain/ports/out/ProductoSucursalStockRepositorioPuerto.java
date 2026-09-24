package com.cavosh.api_cafe.modules.productos.domain.ports.out;

import java.util.List;

/**
 * Puerto de salida para consultar/actualizar la disponibilidad de productos por sucursal
 * (tabla producto_sucursal_stock).
 */
public interface ProductoSucursalStockRepositorioPuerto {

    /**
     * IDs de productos marcados como disponibles en la sucursal dada.
     * Un producto sin fila registrada para esa sucursal se considera disponible
     * por defecto (no se ha restringido explícitamente).
     */
    List<Long> obtenerIdsDisponiblesPorSucursal(Long sucursalId);

    /**
     * IDs de productos marcados explícitamente como NO disponibles en la sucursal dada.
     */
    List<Long> obtenerIdsNoDisponiblesPorSucursal(Long sucursalId);

    boolean estaDisponible(Long productoId, Long sucursalId);

    void actualizarDisponibilidad(Long productoId, Long sucursalId, boolean disponible);
}
