package com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.repository;

import com.cavosh.api_cafe.modules.promociones.infrastructure.adapters.out.persistence.entities.CodigoPromocionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPromocionRepository extends JpaRepository<CodigoPromocionalEntity, Long> {

    Optional<CodigoPromocionalEntity> findByCodigoIgnoreCase(String codigo);
}
