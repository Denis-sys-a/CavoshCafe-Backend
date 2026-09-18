package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.dto.*;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.AgregarItemDTO;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.CarritoResponseDTO;
import com.cavosh.api_cafe.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoResponseDTO> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsuario(usuarioId));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(@Valid @RequestBody AgregarItemDTO dto) {
        CarritoResponseDTO carrito = carritoService.agregarItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> actualizarItem(
            @PathVariable Long itemId, @Valid @RequestBody ActualizarCarritoItemRequestDTO dto) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(itemId, dto));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> eliminarItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(carritoService.eliminarItem(itemId));
    }
}
