package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.repository;

import com.cavosh.api_cafe.modules.productos.domain.model.ProductoSucursalStockId;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductoSucursalStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataProductoSucursalStockRepository
        extends JpaRepository<ProductoSucursalStockEntity, ProductoSucursalStockId> {

    List<ProductoSucursalStockEntity> findByIdSucursalIdAndDisponibleTrue(Long sucursalId);

    List<ProductoSucursalStockEntity> findByIdSucursalId(Long sucursalId);
}
