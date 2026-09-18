package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.productos.domain.model.Categoria;
import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.modules.productos.domain.ports.in.CategoriaCasoUso;
import com.cavosh.api_cafe.modules.productos.domain.ports.in.ProductoCasoUso;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos.ProductDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductoCasoUso productoCasoUso;
    private final CategoriaCasoUso categoriaCasoUso;

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoCasoUso.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoCasoUso.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productoCasoUso.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productoCasoUso.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoCasoUso.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok(categoriaCasoUso.obtenerTodas());
    }
}