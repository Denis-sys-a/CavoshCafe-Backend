package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos.ProductDTO;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos.ProductResponseDTO;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductCategoryEntity;
import com.cavosh.api_cafe.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll(
            @RequestParam(required = false) ProductCategoryEntity category,
            @RequestParam(required = false) String search) {

        if (category != null) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(productService.searchProducts(search));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductDTO dto) {
        ProductResponseDTO created = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
