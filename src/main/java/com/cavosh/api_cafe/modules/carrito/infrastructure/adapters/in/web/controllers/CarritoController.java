package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.carrito.domain.model.Carrito;
import com.cavosh.api_cafe.modules.carrito.domain.ports.in.CarritoCasoUso;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.AgregarItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoCasoUso carritoCasoUso;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoCasoUso.obtenerCarritoPorUsuario(usuarioId));
    }

    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<Carrito> agregarItem(
            @PathVariable Long usuarioId,
            @Valid @RequestBody AgregarItemDTO dto) {
        return ResponseEntity.ok(carritoCasoUso.agregarItem(usuarioId, dto));
    }

    @DeleteMapping("/{usuarioId}/items/{itemId}")
    public ResponseEntity<Carrito> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(carritoCasoUso.eliminarItem(usuarioId, itemId));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoCasoUso.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}