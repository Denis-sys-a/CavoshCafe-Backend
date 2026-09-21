package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.entities.CodigoVerificacionEntity;

@Repository
public interface SpringDataCodigoVerificacionRepository extends JpaRepository<CodigoVerificacionEntity, Long> {

    /**
     * Ultimo codigo vigente (no usado) emitido para un email.
     * Util para controlar reenvios / cooldown.
     */
    Optional<CodigoVerificacionEntity> findTopByEmailAndUsadoIsFalseOrderByFechaCreacionDesc(String email);

    /**
     * Ultimo codigo no usado que coincide con email + codigo ingresado.
     * La validacion de expiracion se hace en la capa de aplicacion.
     */
    Optional<CodigoVerificacionEntity> findTopByEmailAndCodigoAndUsadoIsFalseOrderByFechaCreacionDesc(
            String email, String codigo);
}
