package com.cavosh.api_cafe.modules.auth.domain.exception;

/**
 * Caso específico de {@link CodigoRecuperacionInvalidoException}: el OTP de
 * recuperación existía y correspondía al correo indicado, pero su tiempo de
 * vigencia ya venció.
 * <p>
 * Es subclase de {@code CodigoRecuperacionInvalidoException} para que el
 * manejador global (HTTP 400) la siga capturando automáticamente, a la vez
 * que permite distinguirla puntualmente si el llamador lo necesita.
 */
public class CodigoRecuperacionExpiradoException extends CodigoRecuperacionInvalidoException {
    public CodigoRecuperacionExpiradoException(String message) {
        super(message);
    }
}
