package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.productos.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Producto>> obtenerFavoritos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping("/{usuarioId}/{productoId}")
    public ResponseEntity<Void> agregarFavorito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{usuarioId}/{productoId}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        return ResponseEntity.noContent().build();
    }
}