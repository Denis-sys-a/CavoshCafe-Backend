package com.cavosh.api_cafe.modules.auth.domain.exception;

/**
 * Se lanza cuando un código OTP de verificación no puede ser aceptado:
 * no existe, ya fue utilizado, expiró, o se solicitó un reenvío antes
 * de cumplirse el tiempo mínimo de espera.
 * <p>
 * Se traduce a HTTP 400 (BAD_REQUEST) en {@code GlobalExceptionHandler}.
 */
public class CodigoVerificacionInvalidoException extends RuntimeException {
    public CodigoVerificacionInvalidoException(String message) {
        super(message);
    }
}