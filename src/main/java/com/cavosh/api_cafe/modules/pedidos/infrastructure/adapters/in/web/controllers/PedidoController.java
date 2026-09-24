package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import com.cavosh.api_cafe.modules.pedidos.domain.model.Pedido;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.in.ConsultarPedidoCasoUso;
import com.cavosh.api_cafe.modules.pedidos.domain.ports.in.CrearPedidoCasoUso;
import com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos.CrearPedidoRequestDTO;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
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

    private final CrearPedidoCasoUso crearPedidoCasoUso;
    private final ConsultarPedidoCasoUso consultarPedidoCasoUso;

    @PostMapping
    public ResponseEntity<ApiResponse<Pedido>> crearPedido(@Valid @RequestBody CrearPedidoRequestDTO dto) {
        Pedido pedido = crearPedidoCasoUso.crearPedido(dto);
        ApiResponse<Pedido> response = ApiResponse.success(
                "Pedido creado exitosamente", pedido, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pedido>> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = consultarPedidoCasoUso.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Pedido obtenido exitosamente", pedido));
    }

    // "authentication.principal" es el CustomUserDetails que carga el
    // JwtAuthenticationFilter; expone "id" mediante su getId()
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO') or #usuarioId == authentication.principal.id")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Pedido>>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        List<Pedido> pedidos = consultarPedidoCasoUso.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Pedidos obtenidos exitosamente", pedidos));
    }
}