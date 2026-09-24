package com.cavosh.api_cafe.modules.promociones.domain.exception;

/**
 * Se lanza cuando un código promocional existe pero no puede aplicarse:
 * está inactivo, fuera de vigencia, o superó su límite de usos (global o por usuario).
 */
public class CuponInvalidoException extends RuntimeException {
    public CuponInvalidoException(String message) {
        super(message);
    }
}
