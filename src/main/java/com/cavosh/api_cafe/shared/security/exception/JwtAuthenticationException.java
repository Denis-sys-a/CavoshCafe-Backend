package com.cavosh.api_cafe.shared.security.exception;

/**
 * Excepción base (no chequeada) para cualquier error relacionado con la
 * generación, validación o lectura de un token JWT.
 *
 * Se define como clase base para que, más adelante (Fase 2 - Parte 2:
 * filtro de seguridad / @ControllerAdvice), se puedan capturar todos los
 * errores de JWT con un único catch si así se requiere, o de forma
 * granular usando las subclases {@link JwtTokenExpiradoException} y
 * {@link JwtTokenInvalidoException}.
 */
public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
