package com.cavosh.api_cafe.modules.auth.domain.exception;

/**
 * Se lanza cuando un código OTP de recuperación de contraseña no puede ser
 * aceptado: no existe, ya fue utilizado, o se solicitó un reenvío antes de
 * cumplirse el tiempo mínimo de espera.
 * <p>
 * Se traduce a HTTP 400 (BAD_REQUEST) en {@code GlobalExceptionHandler}.
 */
public class CodigoRecuperacionInvalidoException extends RuntimeException {
    public CodigoRecuperacionInvalidoException(String message) {
        super(message);
    }
}
