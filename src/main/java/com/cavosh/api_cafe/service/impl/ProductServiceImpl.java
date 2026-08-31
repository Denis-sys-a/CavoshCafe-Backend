package com.cavosh.api_cafe.service.impl;

import com.cavosh.api_cafe.dto.ProductRequestDTO;
import com.cavosh.api_cafe.dto.ProductResponseDTO;
import com.cavosh.api_cafe.entity.Product;
import com.cavosh.api_cafe.entity.ProductCategory;
import com.cavosh.api_cafe.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.repository.ProductRepository;
import com.cavosh.api_cafe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(ProductCategory category) {
        return productRepository.findByCategoryAndActiveTrue(category)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return toResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .basePrice(dto.getBasePrice())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .active(true)
                .build();

        return toResponseDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product product = findProductOrThrow(id);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBasePrice(dto.getBasePrice());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(dto.getCategory());

        return toResponseDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .basePrice(product.getBasePrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .tipoProducto(product.getProductType())
                .build();
    }

}
