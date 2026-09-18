package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos.DireccionRequestDTO;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos.UsuarioResponseDTO;
import com.cavosh.api_cafe.service.UsuarioDireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios/me/direcciones")
@RequiredArgsConstructor
public class UsuarioDireccionController {

    private final UsuarioDireccionService usuarioDireccionService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(usuarioDireccionService.obtenerDireccionesPorUsuario(correo));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            Authentication authentication,
            @Valid @RequestBody DireccionRequestDTO dto) {
        String correo = authentication.getName();
        UsuarioResponseDTO creada = usuarioDireccionService.crearDireccion(correo, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(Authentication authentication, @PathVariable Long id) {
        String correo = authentication.getName();
        usuarioDireccionService.eliminarDireccion(correo, id);
        return ResponseEntity.noContent().build();
    }
}