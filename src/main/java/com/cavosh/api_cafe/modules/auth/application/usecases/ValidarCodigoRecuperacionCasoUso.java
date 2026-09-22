package com.cavosh.api_cafe.modules.auth.application.usecases;

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.VerifyResetCodeResponseDTO;

public interface ValidarCodigoRecuperacionCasoUso {

    /**
     * Valida el OTP de recuperación recibido, lo marca como verificado y
     * emite un token de reseteo temporal de vida corta.
     *
     * @return el resetToken y su tiempo de vigencia en minutos
     * @throws com.cavosh.api_cafe.modules.auth.domain.exception.CodigoRecuperacionInvalidoException
     *         si el código no existe o ya fue usado
     * @throws com.cavosh.api_cafe.modules.auth.domain.exception.CodigoRecuperacionExpiradoException
     *         si el código existía pero ya expiró
     */
    VerifyResetCodeResponseDTO ejecutar(String email, String codigo);
}
