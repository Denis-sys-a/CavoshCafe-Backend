package com.cavosh.api_cafe.controller;

import com.cavosh.api_cafe.dto.ProductResponseDTO;
import com.cavosh.api_cafe.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @PostMapping
    public ResponseEntity<Void> agregar(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId) {
        favoritoService.agregarAFavoritos(usuarioId, productoId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminar(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId) {
        favoritoService.eliminarDeFavoritos(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProductResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(favoritoService.obtenerFavoritosPorUsuario(usuarioId));
    }
}
