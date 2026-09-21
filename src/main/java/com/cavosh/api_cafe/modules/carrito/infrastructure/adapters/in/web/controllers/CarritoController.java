package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.carrito.domain.model.Carrito;
import com.cavosh.api_cafe.modules.carrito.domain.ports.in.CarritoCasoUso;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.AgregarItemDTO;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<Carrito>> obtenerCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = carritoCasoUso.obtenerCarritoPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Carrito obtenido exitosamente", carrito));
    }

    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<ApiResponse<Carrito>> agregarItem(
            @PathVariable Long usuarioId,
            @Valid @RequestBody AgregarItemDTO dto) {
        Carrito carrito = carritoCasoUso.agregarItem(usuarioId, dto);
        return ResponseEntity.ok(ApiResponse.success("Producto agregado al carrito exitosamente", carrito));
    }

    @DeleteMapping("/{usuarioId}/items/{itemId}")
    public ResponseEntity<ApiResponse<Carrito>> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long itemId) {
        Carrito carrito = carritoCasoUso.eliminarItem(usuarioId, itemId);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado del carrito exitosamente", carrito));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<ApiResponse<Void>> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoCasoUso.vaciarCarrito(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Carrito vaciado exitosamente", null));
    }
}