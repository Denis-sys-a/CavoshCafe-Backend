package com.cavosh.api_cafe.modules.auth.application.usecases;

public interface ValidarCodigoVerificacionCasoUso {

    /**
     * Valida el OTP recibido y lo marca como usado.
     *
     * @return true si el código era válido
     * @throws com.cavosh.api_cafe.modules.auth.domain.exception.CodigoVerificacionInvalidoException
     *         si el código no existe, ya fue usado o expiró
     */
    boolean ejecutar(String email, String codigo);
}