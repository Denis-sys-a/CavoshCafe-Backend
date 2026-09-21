package com.cavosh.api_cafe.modules.auth.domain.exception;

/**
 * Caso específico de {@link CodigoVerificacionInvalidoException}: el código
 * existía y correspondía al correo indicado, pero su tiempo de vigencia ya
 * venció.
 * <p>
 * Es subclase de {@code CodigoVerificacionInvalidoException} para que el
 * manejador global existente (HTTP 400) la siga capturando automáticamente,
 * permitiendo a la vez distinguirla puntualmente si el llamador lo necesita
 * (p. ej. para mostrar "solicita un nuevo código" en vez de "código inválido").
 */
public class CodigoVerificacionExpiradoException extends CodigoVerificacionInvalidoException {
    public CodigoVerificacionExpiradoException(String message) {
        super(message);
    }
}
