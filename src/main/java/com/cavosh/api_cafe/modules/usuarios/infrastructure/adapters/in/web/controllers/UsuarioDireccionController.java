package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.in.ConsultarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.in.RegistrarDireccionCasoUso;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos.DireccionRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioDireccionController {

    private final ConsultarUsuarioCasoUso consultarUsuarioCasoUso;
    private final RegistrarDireccionCasoUso registrarDireccionCasoUso;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultarUsuarioCasoUso.obtenerPorId(id));
    }

    @PostMapping("/{usuarioId}/direcciones")
    public ResponseEntity<Direccion> agregarDireccion(
            @PathVariable Long usuarioId,
            @Valid @RequestBody DireccionRequestDTO dto) {
        return ResponseEntity.ok(registrarDireccionCasoUso.agregarDireccion(usuarioId, dto));
    }

    @GetMapping("/{usuarioId}/direcciones")
    public ResponseEntity<List<Direccion>> obtenerDirecciones(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(registrarDireccionCasoUso.obtenerDireccionesPorUsuario(usuarioId));
    }
}