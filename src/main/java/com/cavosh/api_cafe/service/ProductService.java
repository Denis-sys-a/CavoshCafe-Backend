package com.cavosh.api_cafe.service;

import com.cavosh.api_cafe.dto.ProductRequestDTO;
import com.cavosh.api_cafe.dto.ProductResponseDTO;
import com.cavosh.api_cafe.entity.ProductCategory;
import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> getProductsByCategory(ProductCategory category);

    List<ProductResponseDTO> searchProducts(String name);

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO createProduct(ProductRequestDTO dto);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);

    void deleteProduct(Long id);

}
