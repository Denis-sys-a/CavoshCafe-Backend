package com.cavosh.api_cafe.repository;

import com.cavosh.api_cafe.entity.Product;
import com.cavosh.api_cafe.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndActiveTrue(ProductCategory category);

    List<Product> findByActiveTrue();

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}
