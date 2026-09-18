package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.in.ConsultarPedidoCasoUso;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.in.CrearPedidoCasoUso;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos.CrearPedidoRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final CrearPedidoCasoUso crearPedidoCasoUso;
    private final ConsultarPedidoCasoUso consultarPedidoCasoUso;

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody CrearPedidoRequestDTO dto) {
        return ResponseEntity.ok(crearPedidoCasoUso.crearPedido(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultarPedidoCasoUso.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(consultarPedidoCasoUso.obtenerPorUsuario(usuarioId));
    }
}