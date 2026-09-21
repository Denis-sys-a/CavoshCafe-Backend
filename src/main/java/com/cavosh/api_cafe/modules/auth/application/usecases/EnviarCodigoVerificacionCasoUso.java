package com.cavosh.api_cafe.modules.auth.application.usecases;

public interface EnviarCodigoVerificacionCasoUso {

    /**
     * Genera un OTP de 6 dígitos, lo persiste y lo envía al correo indicado.
     *
     * @throws com.cavosh.api_cafe.modules.auth.domain.exception.CodigoVerificacionInvalidoException
     *         si aún no se cumple el tiempo mínimo de espera para un reenvío
     */
    void ejecutar(String email);
}