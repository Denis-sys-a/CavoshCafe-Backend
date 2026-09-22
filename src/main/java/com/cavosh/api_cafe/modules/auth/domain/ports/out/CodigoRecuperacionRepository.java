package com.cavosh.api_cafe.modules.auth.domain.ports.out;

import java.util.Optional;

import com.cavosh.api_cafe.modules.auth.domain.model.CodigoRecuperacion;

/**
 * Puerto de salida de persistencia para los códigos OTP de recuperación
 * de contraseña.
 * La implementación concreta vive en infrastructure/adapters/out/persistence/adapters.
 */
public interface CodigoRecuperacionRepository {

    CodigoRecuperacion guardar(CodigoRecuperacion codigo);

    /** Último código vigente (no usado) emitido para el correo; usado para el cooldown de reenvío. */
    Optional<CodigoRecuperacion> buscarUltimoVigentePorEmail(String email);

    /** Último código no usado que coincide con correo + OTP ingresado (paso 2: verify-reset-code). */
    Optional<CodigoRecuperacion> buscarVigentePorEmailYCodigo(String email, String codigo);

    /** Último registro no usado, ya verificado, que coincide con correo + resetToken (paso 3: reset-password). */
    Optional<CodigoRecuperacion> buscarVigentePorEmailYToken(String email, String token);
}
