package com.cavosh.api_cafe.modules.pedidos.domain.exception;

/**
 * Se lanza cuando se intenta crear un pedido con metodoEntrega = DELIVERY
 * sin haber indicado una dirección de entrega (direccionId).
 */
public class DireccionRequeridaException extends RuntimeException {
    public DireccionRequeridaException(String message) {
        super(message);
    }
}
