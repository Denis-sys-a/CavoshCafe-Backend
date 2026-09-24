package com.cavosh.api_cafe.shared.security.exception;

/**
 * Se lanza cuando un token JWT no puede procesarse de forma confiable:
 * firma inválida (posible manipulación), estructura malformada, formato
 * no soportado, o el valor recibido está vacío/nulo.
 */
public class JwtTokenInvalidoException extends JwtAuthenticationException {

    public JwtTokenInvalidoException(String message) {
        super(message);
    }

    public JwtTokenInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
