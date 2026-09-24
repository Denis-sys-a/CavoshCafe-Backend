package com.cavosh.api_cafe.modules.promociones.domain.exception;

/**
 * Se lanza cuando se busca un código promocional que no existe.
 */
public class PromocionNoEncontradaException extends RuntimeException {
    public PromocionNoEncontradaException(String message) {
        super(message);
    }
}
