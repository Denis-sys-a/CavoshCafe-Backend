package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.repository;

import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.out.persistence.entities.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCarritoRepository extends JpaRepository<CarritoEntity, Long> {

    Optional<CarritoEntity> findByUsuarioId(Long usuarioId);
}
