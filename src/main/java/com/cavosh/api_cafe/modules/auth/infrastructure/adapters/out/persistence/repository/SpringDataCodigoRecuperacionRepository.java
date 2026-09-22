package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.entities.CodigoRecuperacionEntity;

@Repository
public interface SpringDataCodigoRecuperacionRepository extends JpaRepository<CodigoRecuperacionEntity, Long> {

    /**
     * Ultimo registro vigente (no usado) emitido para un correo.
     * Util para controlar reenvios / cooldown en forgot-password.
     */
    Optional<CodigoRecuperacionEntity> findTopByCorreoAndUsadoIsFalseOrderByFechaCreacionDesc(String correo);

    /**
     * Ultimo registro no usado que coincide con correo + codigo OTP ingresado.
     * La validacion de expiracion se hace en la capa de aplicacion.
     */
    Optional<CodigoRecuperacionEntity> findTopByCorreoAndCodigoAndUsadoIsFalseOrderByFechaCreacionDesc(
            String correo, String codigo);

    /**
     * Ultimo registro no usado, ya verificado, que coincide con correo + resetToken.
     * La validacion de expiracion del token se hace en la capa de aplicacion.
     */
    Optional<CodigoRecuperacionEntity> findTopByCorreoAndTokenAndVerificadoIsTrueAndUsadoIsFalseOrderByFechaCreacionDesc(
            String correo, String token);
}
