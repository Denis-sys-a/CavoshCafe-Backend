package com.cavosh.api_cafe.modules.auth.domain.ports.out;

import java.util.Optional;

import com.cavosh.api_cafe.modules.auth.domain.model.CodigoVerificacion;

/**
 * Puerto de salida de persistencia para los códigos OTP de verificación.
 * La implementación concreta (Spring Data / JPA) vive en
 * infrastructure/adapters/out/persistence/adapters.
 */
public interface CodigoVerificacionRepositoryPort {

    CodigoVerificacion guardar(CodigoVerificacion codigo);

    /** Último código vigente (no usado) emitido para el correo. */
    Optional<CodigoVerificacion> buscarUltimoVigentePorEmail(String email);

    /** Último código no usado que coincide con correo + código ingresado. */
    Optional<CodigoVerificacion> buscarVigentePorEmailYCodigo(String email, String codigo);
}