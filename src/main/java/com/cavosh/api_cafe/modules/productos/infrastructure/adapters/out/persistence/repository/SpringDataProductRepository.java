package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.repository;

import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductCategoryEntity;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategoria(ProductCategoryEntity categoria);
}
