package com.cavosh.api_cafe.modules.auth.domain.ports.out;

import java.util.Optional;

import com.cavosh.api_cafe.modules.auth.domain.model.CodigoVerificacion;

/**
 * Puerto de salida de persistencia para los códigos OTP de verificación.
 * (Renombrado desde CodigoVerificacionRepositoryPort para alinear con la
 * convención pedida en la Fase 1 de Autenticación.)
 * La implementación concreta vive en infrastructure/adapters/out/persistence/adapters.
 */
public interface CodigoVerificacionRepository {

    CodigoVerificacion guardar(CodigoVerificacion codigo);

    /** Último código vigente (no usado) emitido para el correo. */
    Optional<CodigoVerificacion> buscarUltimoVigentePorEmail(String email);

    /** Último código no usado que coincide con correo + código ingresado. */
    Optional<CodigoVerificacion> buscarVigentePorEmailYCodigo(String email, String codigo);
}