package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.productos.domain.model.Categoria;
import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.modules.productos.domain.ports.in.CategoriaCasoUso;
import com.cavosh.api_cafe.modules.productos.domain.ports.in.ProductoCasoUso;
import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos.ProductDTO;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerTodos() {
        List<Producto> productos = productoCasoUso.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.success("Productos obtenidos exitosamente", productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoCasoUso.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Producto obtenido exitosamente", producto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Producto>> crear(@Valid @RequestBody ProductDTO dto) {
        Producto producto = productoCasoUso.crear(dto);
        ApiResponse<Producto> response = ApiResponse.success(
                "Producto creado exitosamente", producto, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Producto>> actualizar(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        Producto producto = productoCasoUso.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado exitosamente", producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        productoCasoUso.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente", null));
    }

    @GetMapping("/categorias")
    public ResponseEntity<ApiResponse<List<Categoria>>> obtenerCategorias() {
        List<Categoria> categorias = categoriaCasoUso.obtenerTodas();
        return ResponseEntity.ok(ApiResponse.success("Categorías obtenidas exitosamente", categorias));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerDisponiblesPorSucursal(@PathVariable Long sucursalId) {
        List<Producto> productos = productoCasoUso.obtenerDisponiblesPorSucursal(sucursalId);
        return ResponseEntity.ok(ApiResponse.success("Catálogo de la sucursal obtenido exitosamente", productos));
    }
}