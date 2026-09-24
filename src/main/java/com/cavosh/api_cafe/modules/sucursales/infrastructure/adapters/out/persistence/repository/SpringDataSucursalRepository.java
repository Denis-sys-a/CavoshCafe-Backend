package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.repository;

import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.entities.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSucursalRepository extends JpaRepository<SucursalEntity, Long> {
}
