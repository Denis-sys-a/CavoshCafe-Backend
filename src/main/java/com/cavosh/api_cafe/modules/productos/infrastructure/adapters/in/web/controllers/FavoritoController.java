package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    @GetMapping("/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerFavoritos(@PathVariable Long usuarioId) {
        List<Producto> favoritos = Collections.emptyList();
        return ResponseEntity.ok(ApiResponse.success("Favoritos obtenidos exitosamente", favoritos));
    }

    @PostMapping("/{usuarioId}/{productoId}")
    public ResponseEntity<ApiResponse<Void>> agregarFavorito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        ApiResponse<Void> response = ApiResponse.success(
                "Producto agregado a favoritos exitosamente", null, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{usuarioId}/{productoId}")
    public ResponseEntity<ApiResponse<Void>> eliminarFavorito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado de favoritos exitosamente", null));
    }
}