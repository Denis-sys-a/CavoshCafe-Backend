package com.cavosh.api_cafe.controller;

import com.cavosh.api_cafe.dto.CheckoutRequestDTO;
import com.cavosh.api_cafe.dto.PedidoResponseDTO;
import com.cavosh.api_cafe.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponseDTO> checkout(@Valid @RequestBody CheckoutRequestDTO dto) {
        PedidoResponseDTO pedido = pedidoService.crearPedidoDesdeCarrito(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> historial(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.obtenerHistorialPorUsuario(usuarioId));
    }
}
