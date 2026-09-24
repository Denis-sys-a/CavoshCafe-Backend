package com.cavosh.api_cafe.modules.pedidos.domain.enums;

/**
 * Ciclo de vida de un pedido. Los valores coinciden con el ENUM de la columna
 * {@code pedidos.estado} definido en {@code bd/cavosh_db.sql}.
 */
public enum EstadoPedido {
    CREADO,
    EN_PREPARACION,
    LISTO,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO
}
