package com.cavosh.api_cafe.modules.auth.domain.exception;

/**
 * Se lanza en el paso final ({@code reset-password}) cuando el
 * {@code resetToken} recibido es inválido, ya fue usado, o expiró.
 * <p>
 * Se traduce a HTTP 400 (BAD_REQUEST) en {@code GlobalExceptionHandler}.
 */
public class ResetTokenInvalidoException extends RuntimeException {
    public ResetTokenInvalidoException(String message) {
        super(message);
    }
}
