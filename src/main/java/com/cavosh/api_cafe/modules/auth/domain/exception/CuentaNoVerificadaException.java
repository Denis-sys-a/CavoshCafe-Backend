package com.cavosh.api_cafe.modules.auth.domain.exception;

/**
 * Se lanza al intentar iniciar sesión con una cuenta local cuyo correo
 * todavía no ha sido verificado mediante el código OTP.
 * <p>
 * Se traduce a HTTP 403 (FORBIDDEN) en {@code GlobalExceptionHandler}.
 */
public class CuentaNoVerificadaException extends RuntimeException {
    public CuentaNoVerificadaException(String message) {
        super(message);
    }
}
