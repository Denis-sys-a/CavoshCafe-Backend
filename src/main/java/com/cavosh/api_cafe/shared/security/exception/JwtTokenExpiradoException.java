package com.cavosh.api_cafe.shared.security.exception;

/**
 * Se lanza cuando un token JWT es sintácticamente válido y su firma es
 * correcta, pero su fecha de expiración (claim "exp") ya pasó.
 */
public class JwtTokenExpiradoException extends JwtAuthenticationException {

    public JwtTokenExpiradoException(String message) {
        super(message);
    }

    public JwtTokenExpiradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
