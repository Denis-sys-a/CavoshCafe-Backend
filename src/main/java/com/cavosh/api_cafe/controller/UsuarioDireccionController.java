package com.cavosh.api_cafe.controller;

import com.cavosh.api_cafe.dto.UsuarioDireccionRequestDTO;
import com.cavosh.api_cafe.dto.UsuarioDireccionResponseDTO;
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
    public ResponseEntity<List<UsuarioDireccionResponseDTO>> listar(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(usuarioDireccionService.obtenerDireccionesPorUsuario(correo));
    }

    @PostMapping
    public ResponseEntity<UsuarioDireccionResponseDTO> crear(
            Authentication authentication,
            @Valid @RequestBody UsuarioDireccionRequestDTO dto) {
        String correo = authentication.getName();
        UsuarioDireccionResponseDTO creada = usuarioDireccionService.crearDireccion(correo, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(Authentication authentication, @PathVariable Long id) {
        String correo = authentication.getName();
        usuarioDireccionService.eliminarDireccion(correo, id);
        return ResponseEntity.noContent().build();
    }
}